package com.solidgate.framework.api.core;

import org.openqa.selenium.remote.http.HttpClient;
import org.openqa.selenium.remote.http.HttpMethod;
import org.openqa.selenium.remote.http.HttpRequest;
import org.openqa.selenium.remote.http.HttpResponse;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Supplier;

public class HttpClientOperations {

    private final HttpClient client;
    private final String baseUrl;

    public HttpClientOperations(String url) {
        this.baseUrl = url;
        this.client = createHttpClient();
    }

    public HttpResponse postOperation(String baseUrl, String url, String publicKey, String signature, Supplier<InputStream> content) {
        HttpRequest httpRequest = new HttpRequest(HttpMethod.POST, baseUrl + url);
        addBasicHeaders(httpRequest, publicKey, signature);
        httpRequest.setContent(content);
        return client.execute(httpRequest);
    }

    private HttpClient createHttpClient() {
        try {
            return HttpClient.Factory.createDefault().createClient(new URL(baseUrl));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid base URL: " + baseUrl, e);
        }
    }

    private HttpRequest addBasicHeaders(HttpRequest httpRequest, String publicKey, String signature) {
        httpRequest.addHeader("merchant", publicKey);
        httpRequest.addHeader("signature", signature);
        return httpRequest;
    }
}
