package org.geektimes.projects.user.service.impl;

import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.repository.DatabaseUserRepository;
import org.geektimes.projects.user.repository.UserRepository;
import org.geektimes.projects.user.service.UserService;
import org.geektimes.projects.user.sql.DBConnectionManager;
import org.geektimes.projects.user.sql.LocalTransactional;
import org.geektimes.projects.user.validator.bean.validation.group.UpdateGroup;
import org.geektimes.util.MD5Utils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;


public class UserServiceImpl implements UserService {

    private final static String SALT = "01dfac889";

    @Resource(name = "bean/EntityManager")
    private EntityManager entityManager;

    @Resource(name = "bean/Validator")
    private Validator validator;

    @Resource(name = "bean/DBConnectionManager")
    private DBConnectionManager dbConnectionManager;

    private UserRepository userRepository;

    @PostConstruct
    public void initUserRepository(){
        userRepository = new DatabaseUserRepository(dbConnectionManager);
    }

    @Override
    // 默认需要事务
    @LocalTransactional
    public boolean register(User user) {
        if(user == null){
            return false;
        }
        String password = MD5Utils.getMD5Hex(user.getPassword(),SALT);
        user.setPassword(password);
        //return userRepository.save(user);
        entityManager = entityManager.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        try {
            entityManager.persist(user);
        }catch(ConstraintViolationException cve){
            Set<ConstraintViolation<?>> cvs = cve.getConstraintViolations();
            for (ConstraintViolation<?> cv : cvs) {
                System.out.println("------------------------------------------------");
                System.out.println("Violation: " + cv.getMessage());
                System.out.println("Entity: " + cv.getRootBeanClass().getSimpleName());
                // The violation occurred on a leaf bean (embeddable)
                if (cv.getLeafBean() != null && cv.getRootBean() != cv.getLeafBean()) {
                    System.out.println("Embeddable: " + cv.getLeafBean().getClass().getSimpleName());
                }
                System.out.println("Attribute: " + cv.getPropertyPath());
                System.out.println("Invalid value: " + cv.getInvalidValue());
            }
            //TODO
            return false;

        }

        entityManager.getTransaction().commit();

        return true;
        // 调用其他方法方法
        //update(user); // 涉及事务
        // register 方法和 update 方法存在于同一线程
        // register 方法属于 Outer 事务（逻辑）
        // update 方法属于 Inner 事务（逻辑）
        // Case 1 : 两个方法均涉及事务（并且传播行为和隔离级别相同）
        // 两者共享一个物理事务，但存在两个逻辑事务
        // 利用 ThreadLocal 管理一个物理事务（Connection）

        // rollback 情况 1 : update 方法（Inner 事务），它无法主动去调用 rollback 方法
        // 设置 rollback only 状态，Inner TX(rollback only)，说明 update 方法可能存在执行异常或者触发了数据库约束
        // 当 Outer TX 接收到 Inner TX 状态，它来执行 rollback
        // A -> B -> C -> D -> E 方法调用链条
        // A (B,C,D,E) 内联这些方法，合成大方法
        // 关于物理事务是哪个方法创建
        // 其他调用链路事务传播行为是一致时，都是逻辑事务

        // Case 2: register 方法是 PROPAGATION_REQUIRED（事务创建者），update 方法 PROPAGATION_REQUIRES_NEW
        // 这种情况 update 方法也是事务创建者
        // update 方法 rollback-only 状态不会影响 Outer TX，Outer TX 和 Inner TX 是两个物理事务

        // Case 3: register 方法是 PROPAGATION_REQUIRED（事务创建者），update 方法 PROPAGATION_NESTED
        // 这种情况 update 方法同样共享了 register 方法物理事务，并且通过 Savepoint 来实现局部提交和回滚

        // after process
        // transaction.commit();
        //return true;
    }

    private boolean validate(User user){
        Set<ConstraintViolation<User>> constraintViolationSet = validator.validate(user, UpdateGroup.class);
        constraintViolationSet.forEach(name -> System.out.println(name.getMessage()));
        return constraintViolationSet == null || constraintViolationSet.isEmpty();
    }

    @Override
    public boolean deregister(User user) {
        return false;
    }

    @Override
    public boolean update(User user) {
        String password = MD5Utils.getMD5Hex(user.getPassword(),SALT);
        user.setPassword(password);
        entityManager.merge(user);
        return true;
        //return userRepository.update(user);
    }

    @Override
    public User queryUserById(Long id) {
        return entityManager.find(User.class,id);
       //return userRepository.getById(id);
    }

    @Override
    public User queryUserByNameAndPassword(String name, String password) {
        password = MD5Utils.getMD5Hex(password,SALT);
        List<User> users = entityManager.createQuery("select u from User u where u.name = :name and u.password = :password",User.class)
                .setParameter("name",name).setParameter("password",password).getResultList();
        if(users != null && !users.isEmpty()){
            return users.get(0);
        }
        return null;
        //return userRepository.getByNameAndPassword(name,password);
        //return entityManager.find()
    }

    @Override
    public boolean checkUserName(String name) {
        List<User> users = entityManager.createQuery("select u from User u where u.name = :name",User.class)
                        .setParameter("name",name)
                        .getResultList();
//        Optional<List<User>> optionalUsers = Optional.ofNullable(users);
//        return optionalUsers.isPresent();
        //User user = userRepository.getByName(name);
        if(users == null || users.isEmpty()){
            return true;
        }
        return false;
    }
}
