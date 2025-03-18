package io.hikarilan.nerabbs.services.search.configuration;

import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MeilisearchConfiguration {

    @Value("${meilisearch.endpoint}")
    private String endpoint;

    @Value("${meilisearch.masterKey}")
    private String masterKey;

    @Bean
    public Client getClient() {
        var client = new Client(new Config(endpoint, masterKey));
        var keys = client.getKeys().getResults();
        var adminKey = keys[keys.length - 1];
        return new Client(new Config(endpoint, adminKey.getKey()));
    }

}
