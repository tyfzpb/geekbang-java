package org.geektimes.projects.user.domain;

import org.geektimes.projects.user.validator.bean.validation.Password;
import org.geektimes.projects.user.validator.bean.validation.group.UpdateGroup;

import javax.persistence.*;
import javax.validation.constraints.*;
import javax.validation.groups.Default;
import java.io.Serializable;
import java.util.Objects;

import static javax.persistence.GenerationType.AUTO;

/**
 * 用户领域对象
 *
 * @since 1.0
 */
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = AUTO)
    @NotNull(groups = UpdateGroup.class,message="ID不能为空")
    @Min(value = 1,groups=UpdateGroup.class,message="ID必须大于0")
    private Long id;

    @Column
    @NotNull(message = "姓名不能为空",groups = {UpdateGroup.class, Default.class})
    private String name;

    @Column
    @Password(groups = {UpdateGroup.class, Default.class})
    private String password;

    @Column
    @Email(message = "Email格式不正确",groups = {UpdateGroup.class,Default.class})
    private String email;

    @Column
    @Pattern(regexp = "^[1][3-9]\\d{9}$", message = "手机号格式不正确",groups = {UpdateGroup.class, Default.class})
    private String phoneNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(password, user.password) && Objects.equals(email, user.email) && Objects.equals(phoneNumber, user.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, password, email, phoneNumber);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
