package org.geektimes.rest.client;

import org.geektimes.rest.core.DefaultResponse;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * HTTP POST Method {@link Invocation}
 *
 * @since
 */
class HttpPostInvocation implements Invocation {

    private final URI uri;

    private final URL url;

    private final MultivaluedMap<String, Object> headers;

    private final Entity<?> entity;


    public HttpPostInvocation(URI uri, MultivaluedMap<String, Object> headers, Entity<?> entity) {
        this.uri = uri;
        this.headers = headers;
        this.entity = entity;
        try {
            this.url = uri.toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Invocation property(String name, Object value) {
        return this;
    }

    @Override
    public Response invoke() {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(HttpMethod.POST);
            setRequestHeaders(connection);

            if (this.entity != null) {
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setUseCaches(false);
                connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                connection.connect();
                Object body = entity.getEntity();
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
                writer.write(body.toString());
                writer.close();
            }

            // TODO Set the cookies
            int statusCode = connection.getResponseCode();

            DefaultResponse response = new DefaultResponse();
            response.setConnection(connection);
            response.setStatus(statusCode);
            return response;


        } catch (IOException e) {
            // TODO Error handler
        }
        return null;
    }


    private void setRequestHeaders(HttpURLConnection connection) {
        for (Map.Entry<String, List<Object>> entry : headers.entrySet()) {
            String headerName = entry.getKey();
            for (Object headerValue : entry.getValue()) {
                connection.setRequestProperty(headerName, headerValue.toString());
            }
        }
    }

    @Override
    public <T> T invoke(Class<T> responseType) {
        Response response = invoke();
        return response.readEntity(responseType);
    }

    @Override
    public <T> T invoke(GenericType<T> responseType) {
        Response response = invoke();
        return response.readEntity(responseType);
    }

    @Override
    public Future<Response> submit() {
        return null;
    }

    @Override
    public <T> Future<T> submit(Class<T> responseType) {
        return null;
    }

    @Override
    public <T> Future<T> submit(GenericType<T> responseType) {
        return null;
    }

    @Override
    public <T> Future<T> submit(InvocationCallback<T> callback) {
        return null;
    }
}
