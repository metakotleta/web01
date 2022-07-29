package ru.netology;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class Request {

    private String method;
    private String fullpath;
    private String body;
    private String path;
    private String query;
    private List<NameValuePair> parsedParams;

    public Request(String method, String fullpath) {
        this.method = method;
        this.fullpath = fullpath;
        if (fullpath.contains("?")) {
            path = fullpath.substring(0, fullpath.indexOf("?"));
            query = fullpath.substring(fullpath.indexOf("?") + 1);
            parsedParams = URLEncodedUtils.parse(query, StandardCharsets.UTF_8, '&');
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
        return parsedParams;
    }

    public List<NameValuePair> getQueryParam(String param) {
        return parsedParams.stream().filter(p -> p.getName().equals(param)).collect(Collectors.toList());
    }
}
