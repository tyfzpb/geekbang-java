package org.geektimes.mybatis.spring.boot.sample;

import org.geektimes.mybatis.spring.boot.sample.mapper.CityMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SampleMybatisApplication implements CommandLineRunner {

    private final CityMapper cityMapper;

    public SampleMybatisApplication(CityMapper cityMapper) {

        this.cityMapper = cityMapper;

    }

    public static void main(String[] args) {
        SpringApplication.run(SampleMybatisApplication.class, args);
    }

    @Override
    public void run(String... args) {
        this.cityMapper.getAll().stream().forEach(System.out::println);
    }
}
