package org.geektimes.projects.spring.cloud.config.server;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

public class FileSystemPropertySourceRefresher implements ApplicationListener<ApplicationReadyEvent>, InitializingBean, DisposableBean, ApplicationContextAware {

    private final static String ENCODING = "UTF-8";
    private final FileSystemPropertySourceLocator fileSystemPropertySourceLocator;
    private final Resource propertiesResource;
    private WatchService watchService;
    private ApplicationContext applicationContext;
    private boolean isStart = true;
    private final AtomicBoolean ready = new AtomicBoolean(false);


    public FileSystemPropertySourceRefresher(FileSystemPropertySourceLocator locator) {
        this.fileSystemPropertySourceLocator = locator;
        this.propertiesResource = fileSystemPropertySourceLocator.getPropertiesResource();
    }


    private void startWath() throws InterruptedException {
        if (this.propertiesResource.isFile()) { // 判断是否为文件
            // 获取对应文件系统中的文件
            try {
                File propertiesFile = this.propertiesResource.getFile();
                Path propertiesFilePath = propertiesFile.toPath();
                String propertiesFileName = propertiesFilePath.getFileName().toString();
                // 获取当前 OS 文件系统
                FileSystem fileSystem = FileSystems.getDefault();
                this.watchService = fileSystem.newWatchService();
                // 获取资源文件所在的目录
                Path dirPath = propertiesFilePath.getParent();
                // 注册 WatchService 到 dirPath，并且关心修改事件
                dirPath.register(watchService, ENTRY_MODIFY);
                // 处理资源文件变化（异步）
                processPropertiesChanged(propertiesFileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 处理资源文件变化（异步）
     *
     * @param propertiesFileName
     */
    private void processPropertiesChanged(String propertiesFileName) throws InterruptedException {
        Executors.newSingleThreadScheduledExecutor().execute(() -> {
            while (isStart) {
                WatchKey watchKey = null;
                try {
                    watchKey = watchService.take(); // take 发生阻塞
                    // watchKey 是否有效

                    if (watchKey.isValid()) {
                        for (WatchEvent event : watchKey.pollEvents()) {
                            // 事件发生源是相对路径
                            Path fileRelativePath = (Path) event.context();
                            System.out.println(fileRelativePath + " on chage " + event.kind());
                            if (propertiesFileName.equals(fileRelativePath.getFileName().toString())) {
                                applicationContext.publishEvent(new RefreshEvent(this, null, "Refresh fileSystemPropertySource config"));
                            }
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    if (watchKey != null) {
                        watchKey.reset(); // 重置 WatchKey
                    }
                }

            }
        });
    }


    @Override
    public void destroy() throws Exception {
        isStart = false;
        if (watchService != null) {
            watchService.close();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        if (this.ready.compareAndSet(false, true)) {
            System.out.println("applicationReadyEvent = " + applicationReadyEvent);
            try {
                startWath();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}