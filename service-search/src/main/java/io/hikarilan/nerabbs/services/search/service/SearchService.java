package io.hikarilan.nerabbs.services.search.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.SearchRequest;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final Client client;

    private final ObjectMapper objectMapper;

    @SneakyThrows
    public void addDocuments(String index, Collection<Map<String, Object>> documents, @Nullable String primaryKey) {
        var task = client.index(index).addDocuments(objectMapper.writeValueAsString(documents), primaryKey);
        client.waitForTask(task.getTaskUid());
    }

    public void deleteDocuments(String index, Collection<String> primaryKeys) {
        primaryKeys.stream().map(it -> client.index(index).deleteDocument(it)).forEach(task -> client.waitForTask(task.getTaskUid()));
    }

    public ArrayList<HashMap<String, Object>> searchDocument(String index, String query, int offset, int limit) {
        return client.getIndex(index).search(new SearchRequest(query).setOffset(offset).setLimit(limit).setShowMatchesPosition(true)).getHits();
    }

}
