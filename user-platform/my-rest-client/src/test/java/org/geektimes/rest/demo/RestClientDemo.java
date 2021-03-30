package org.geektimes.rest.demo;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import java.io.Serializable;
import java.util.Objects;

class User implements Serializable {
    private Long id;

    private String name;

    private String password;

    private String email;

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

public class RestClientDemo {

    public static void main(String[] args) throws Throwable {
        System.out.println("----------------------GET--------------------------");
        testGet();
        System.out.println("----------------------POST-------------------------");
        testPost();

    }

    public static void testGet() {
        Client client = ClientBuilder.newClient();
        String content = client
                .target("http://localhost:8080/jolokia")      // WebTarget
                .request() // Invocation.Builder
                .get(String.class);                                     //  Response

        System.out.println(content);
    }

    public static void testPost() throws Throwable {
        Client client = ClientBuilder.newClient();

        User user = new User();
        user.setEmail("sdt@164.com");
        user.setName("123434");
        user.setPassword("sfre333");
        user.setPhoneNumber("14525003333");

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        User result = client.target("http://localhost:8080/testRestPost")
                .request().post(Entity.json(userJson), User.class);

        System.out.println(result);
    }
}
