package org.geektimes.rest.demo;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.Objects;

class PrettyDemo {
    private String classes;
    private String crumb;
    private String crumbRequestField;

    public String getCrumb() {
        return crumb;
    }

    public void setCrumb(String crumb) {
        this.crumb = crumb;
    }

    public String getCrumbRequestField() {
        return crumbRequestField;
    }

    public void setCrumbRequestField(String crumbRequestField) {
        this.crumbRequestField = crumbRequestField;
    }

    public String get_class() {
        return classes;
    }

    public void set_class(String _class) {
        this.classes = _class;
    }

    @Override
    public String toString() {
        return "prettyDemo{" +
                "classes='" + classes + '\'' +
                ", crumb='" + crumb + '\'' +
                ", crumbRequestField='" + crumbRequestField + '\'' +
                '}';
    }
}


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

    public static void main(String[] args) throws Throwable{
        Client client = ClientBuilder.newClient();
        Response response = client
                .target("http://localhost:9090/crumbIssuer/api/json?pretty=true")      // WebTarget
                .request() // Invocation.Builder
                .get();                                     //  Response

        PrettyDemo content = response.readEntity(PrettyDemo.class);

        System.out.println(content);
        testGet();
        testPost();

    }

    public static  void testGet(){
        Client client = ClientBuilder.newClient();
        PrettyDemo content = client
                .target("http://localhost:9090/crumbIssuer/api/json?pretty=true")      // WebTarget
                .request() // Invocation.Builder
                .get(PrettyDemo.class);                                     //  Response

        //PrettyDemo content = response.readEntity(PrettyDemo.class);

        System.out.println(content);
    }

    public static void testPost() throws Throwable{
        Client client = ClientBuilder.newClient();

        User user = new User();
        user.setEmail("sdt@164.com");
        user.setName("123434");
        user.setPassword("sfre333");
        user.setPhoneNumber("14525003333");

        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        String  result = client.target("http://localhost:8080/user-web/registry")
                        .request().post(Entity.json(userJson),String.class);

        System.out.println(result);
    }
}
