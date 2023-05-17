package com.example.ligazakon.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories
public class ElasticsearchConfig extends ElasticsearchConfiguration {

    private final String url;
    private final Integer timeoutSocket;
    private final Integer timeoutConnection;

    public ElasticsearchConfig(@Value("${elasticsearch.url}") String url,
                               @Value("${elasticsearch.timeout.socket}") Integer socket,
                               @Value("${elasticsearch.timeout.connection}") Integer connection) {
        this.url = url;
        this.timeoutSocket = socket;
        this.timeoutConnection = connection;
    }

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(url)
                .withConnectTimeout(timeoutSocket)
                .withSocketTimeout(timeoutConnection)
                .build();
    }
}
