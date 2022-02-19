/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ws;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.origami.sigef.common.util.JsonDateDeserializer;
import com.origami.sigef.common.util.Utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@javax.ejb.Singleton(name = "irisEjb")
@ApplicationScoped
public class IrisEjb implements IrisService {

    /**
     * **
     * Se está enviando una credenciales de prueba para que me permita acceder a
     * los servicios de ws
     */
    protected Gson gson;
    protected GsonBuilder builder;
    private static String pasw = "ULdI1c8qukgxcjANqIwh";
    private static String user = "9G4u8q0GnXBkNY7LCBuF";

    public IrisEjb() {
        builder = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz")
                .registerTypeAdapter(Date.class, new JsonDateDeserializer());
        gson = builder.create();
    }

    @Override
    public Object methodGET(String url, Class resultClazz) {
        try {
            System.out.println("url: " + url);
            RestTemplate restTemplate = new RestTemplate(Utils.getClientHttpRequestFactory(user, pasw));
            URI uri = new URI(url);
            ResponseEntity<Object> response = restTemplate.getForEntity(uri, resultClazz);
            return response.getBody();
        } catch (URISyntaxException | RestClientException e) {
            System.out.println(e);
//            e.printStackTrace();
            //Logger.getLogger(IrisEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    public List methodListGET(String url, Class resultClazz) {
        System.out.println("url: " + url);
        try {
            RestTemplate restTemplate = new RestTemplate(Utils.getClientHttpRequestFactory(user, pasw));
            Object[] obj = (Object[]) restTemplate.getForObject(new URI(url), resultClazz);
            if (obj != null) {
                return Arrays.asList(obj);
            } else {
                return null;
            }
        } catch (URISyntaxException | RestClientException e) {
            Logger.getLogger(IrisEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    public Object methodPOST(Object data, String url, Class resultClass) {
        System.out.println("url: " + url);
        String creds = String.format("%s:%s", user, pasw);
        String auth = "Basic " + Base64.getEncoder().encodeToString(creds.getBytes());

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(gson.toJson(data), "UTF-8"));
        httpPost.setHeader("Content-type", "application/json; charset=utf-8");
        httpPost.setHeader("Authorization", auth);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<HttpResponse> futureResponse = executorService.submit(() -> httpClient.execute(httpPost));
        try {
            HttpResponse httpResponse = futureResponse.get(30, TimeUnit.SECONDS);
            if (httpResponse != null) {
                StringBuilder sb;
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"))) {
                    String inputLine;
                    sb = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        sb.append(inputLine);
                    }
                }
                try {
                    return gson.fromJson(sb.toString(), resultClass);
                } catch (JsonSyntaxException e) {
                    Logger.getLogger(IrisEjb.class.getName()).log(Level.SEVERE, null, e);
                    return sb.toString();
                }
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException | TimeoutException | IOException ex) {
            Logger.getLogger(IrisEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List methodListPOST(List data, String url, Class resultClass) {
        System.out.println("url: " + url);
        String creds = String.format("%s:%s", user, pasw);
        String auth = "Basic " + Base64.getEncoder().encodeToString(creds.getBytes());

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(gson.toJson(data), "UTF-8"));
        httpPost.setHeader("Content-type", "application/json; charset=utf-8");
        httpPost.setHeader("Authorization", auth);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<HttpResponse> futureResponse = executorService.submit(() -> httpClient.execute(httpPost));
        try {
            HttpResponse httpResponse = futureResponse.get(30, TimeUnit.SECONDS);
            if (httpResponse != null) {
                StringBuilder sb;
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"))) {
                    String inputLine;
                    sb = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        sb.append(inputLine);
                    }
                }
                try {
                    Object[] obj = (Object[]) gson.fromJson(sb.toString(), resultClass);
                    if (obj != null) {
                        return Arrays.asList(obj);
                    } else {
                        return null;
                    }
                } catch (JsonSyntaxException e) {
                    Logger.getLogger(IrisEjb.class.getName()).log(Level.SEVERE, null, e);
                    return null;
                }
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException | TimeoutException | IOException ex) {
            Logger.getLogger(IrisEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List methodListPOST(Object data, String url, Class resultClass) {
        System.out.println("url: " + url);
        String creds = String.format("%s:%s", user, pasw);
        String auth = "Basic " + Base64.getEncoder().encodeToString(creds.getBytes());

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(gson.toJson(data), "UTF-8"));
        httpPost.setHeader("Content-type", "application/json; charset=utf-8");
        httpPost.setHeader("Authorization", auth);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<HttpResponse> futureResponse = executorService.submit(() -> httpClient.execute(httpPost));
        try {
            HttpResponse httpResponse = futureResponse.get(30, TimeUnit.SECONDS);
            if (httpResponse != null) {
                StringBuilder sb;
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"))) {
                    String inputLine;
                    sb = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        sb.append(inputLine);
                    }
                }
                try {
                    Object[] obj = (Object[]) gson.fromJson(sb.toString(), resultClass);
                    if (obj != null) {
                        return Arrays.asList(obj);
                    } else {
                        return null;
                    }
                } catch (JsonSyntaxException e) {
                    Logger.getLogger(IrisEjb.class.getName()).log(Level.SEVERE, null, e);
                    return null;
                }
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException | TimeoutException | IOException ex) {
            Logger.getLogger(IrisEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Object methodPUT(Object data, String url, Class resultClass) {
        System.out.println("url: " + url);
        String creds = String.format("%s:%s", user, pasw);
        String auth = "Basic " + Base64.getEncoder().encodeToString(creds.getBytes());

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPut httpPut = new HttpPut(url);
        httpPut.setEntity(new StringEntity(gson.toJson(data), "UTF-8"));
        httpPut.setHeader("Content-type", "application/json; charset=utf-8");
        httpPut.setHeader("Authorization", auth);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<HttpResponse> futureResponse = executorService.submit(() -> httpClient.execute(httpPut));
        try {
            HttpResponse httpResponse = futureResponse.get(30, TimeUnit.SECONDS);
            if (httpResponse != null) {
                StringBuilder sb;
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(httpResponse.getEntity().getContent()))) {
                    String inputLine;
                    sb = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        sb.append(inputLine);
                    }
                }
                return gson.fromJson(sb.toString(), resultClass);
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException | TimeoutException | IOException ex) {
            Logger.getLogger(IrisEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List methodListPUT(List data, String url, Class resultClass) {
        System.out.println("url: " + url);
        String creds = String.format("%s:%s", user, pasw);
        String auth = "Basic " + Base64.getEncoder().encodeToString(creds.getBytes());

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPut httpPut = new HttpPut(url);
        httpPut.setEntity(new StringEntity(gson.toJson(data), "UTF-8"));
        httpPut.setHeader("Content-type", "application/json; charset=utf-8");
        httpPut.setHeader("Authorization", auth);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<HttpResponse> futureResponse = executorService.submit(() -> httpClient.execute(httpPut));
        try {
            HttpResponse httpResponse = futureResponse.get(30, TimeUnit.SECONDS);
            if (httpResponse != null) {
                StringBuilder sb;
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"))) {
                    String inputLine;
                    sb = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        sb.append(inputLine);
                    }
                }
                try {
                    Object[] obj = (Object[]) gson.fromJson(sb.toString(), resultClass);
                    if (obj != null) {
                        return Arrays.asList(obj);
                    } else {
                        return null;
                    }
                } catch (JsonSyntaxException e) {
                    Logger.getLogger(IrisEjb.class.getName()).log(Level.SEVERE, null, e);
                    return null;
                }
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException | TimeoutException | IOException ex) {
            Logger.getLogger(IrisEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Object methodPOST(String url, Class resultClass) {
        System.out.println("url: " + url);
        String creds = String.format("%s:%s", user, pasw);
        String auth = "Basic " + Base64.getEncoder().encodeToString(creds.getBytes());

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-type", "application/json; charset=utf-8");
        httpPost.setHeader("Authorization", auth);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<HttpResponse> futureResponse = executorService.submit(() -> httpClient.execute(httpPost));
        try {
            HttpResponse httpResponse = futureResponse.get(30, TimeUnit.SECONDS);
            if (httpResponse != null) {
                StringBuilder sb;
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"))) {
                    String inputLine;
                    sb = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        sb.append(inputLine);
                    }
                }
                try {
                    return gson.fromJson(sb.toString(), resultClass);
                } catch (JsonSyntaxException e) {
                    Logger.getLogger(IrisEjb.class.getName()).log(Level.SEVERE, null, e);
                    return sb.toString();
                }
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException | TimeoutException | IOException ex) {
            Logger.getLogger(IrisEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List methodListPOST(String url, Class resultClass) {
        System.out.println("url: " + url);
        String creds = String.format("%s:%s", user, pasw);
        String auth = "Basic " + Base64.getEncoder().encodeToString(creds.getBytes());

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-type", "application/json; charset=utf-8");
        httpPost.setHeader("Authorization", auth);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<HttpResponse> futureResponse = executorService.submit(() -> httpClient.execute(httpPost));
        try {
            HttpResponse httpResponse = futureResponse.get(30, TimeUnit.SECONDS);
            if (httpResponse != null) {
                StringBuilder sb;
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"))) {
                    String inputLine;
                    sb = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        sb.append(inputLine);
                    }
                }
                try {
                    Object[] obj = (Object[]) gson.fromJson(sb.toString(), resultClass);
                    if (obj != null) {
                        return Arrays.asList(obj);
                    } else {
                        return null;
                    }
                } catch (JsonSyntaxException e) {
                    Logger.getLogger(IrisEjb.class.getName()).log(Level.SEVERE, null, e);
                    return null;
                }
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException | TimeoutException | IOException ex) {
            Logger.getLogger(IrisEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
