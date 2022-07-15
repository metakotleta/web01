package ru.netology;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class Request {

    private String method;
    private String fullpath;
    private String body;
    private String path;
    private String query;

    public Request(String method, String fullpath) {
        this.method = method;
        this.fullpath = fullpath;
        if (fullpath.contains("?")) {
            path = fullpath.substring(0, fullpath.indexOf("?"));
            query = fullpath.substring(fullpath.indexOf("?") + 1);
        } else {
            path = fullpath;
        }

    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getBody() {
        return body;
    }

    public String getQuery() {
        return query;
    }

    public List<NameValuePair> getQueryList() {
        return URLEncodedUtils.parse(query, StandardCharsets.UTF_8, '&');
    }
}
