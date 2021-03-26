package org.geektimes.rest.demo;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

class PrettyDemo{
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

public class RestClientDemo {

    public static void main(String[] args) {
        Client client = ClientBuilder.newClient();
        Response response = client
                .target("http://localhost:9090/crumbIssuer/api/json?pretty=true")      // WebTarget
                .request() // Invocation.Builder
                .get();                                     //  Response

        PrettyDemo content = response.readEntity(PrettyDemo.class);

        System.out.println(content);

    }
}
