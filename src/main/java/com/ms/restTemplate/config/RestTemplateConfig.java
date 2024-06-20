package com.ms.restTemplate.config;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.util.TimeValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(){

        //RequestConfig
        RequestConfig requestConfig=RequestConfig.custom()
                .setConnectionRequestTimeout(5000, TimeUnit.MILLISECONDS)
                .setResponseTimeout(5000,TimeUnit.MILLISECONDS)
                .build();
        ConnectionConfig connectionConfig=ConnectionConfig.custom()
                .setConnectTimeout(5000,TimeUnit.MILLISECONDS)
                .setSocketTimeout(5000,TimeUnit.MILLISECONDS)
                .build();
        SocketConfig socketConfig= SocketConfig.custom()
                .setSoTimeout(5000,TimeUnit.MILLISECONDS)
                .build();
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager= PoolingHttpClientConnectionManagerBuilder.create()
                .setMaxConnTotal(10)
                .setMaxConnPerRoute(4)
                .setDefaultConnectionConfig(connectionConfig)
                .setDefaultSocketConfig(socketConfig)
                .build();
        HttpClient httpClient= HttpClientBuilder.create()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setDefaultRequestConfig(requestConfig)
                .evictExpiredConnections()
                .evictIdleConnections(TimeValue.ofMilliseconds(5000))
                .build();
        ClientHttpRequestFactory clientHttpRequestFactory=new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(clientHttpRequestFactory);
    }
}
