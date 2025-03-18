package io.hikarilan.nerabbs.services.search.grpc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import io.hikarilan.nerabbs.lib.services.search.grpc.*;
import io.hikarilan.nerabbs.services.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@GrpcService
@Validated
public class GrpcSearchService extends SearchGrpc.SearchImplBase {

    private final SearchService searchService;

    private final ObjectMapper objectMapper;

    @SuppressWarnings("unchecked")
    @Override
    public void addDocuments(AddDocumentsRequest request, StreamObserver<Empty> responseObserver) {
        List<Map<String, Object>> documents = new ArrayList<>();
        for (String document : request.getDocumentList()) {
            try {
                documents.add(objectMapper.readValue(document, Map.class));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        searchService.addDocuments(request.getIndex(), documents, request.hasPrimaryKey() ? request.getPrimaryKey() : null);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();

    }

    @Override
    public void deleteDocuments(DeleteDocumentsRequest request, StreamObserver<Empty> responseObserver) {
        searchService.deleteDocuments(request.getIndex(), request.getPrimaryKeyList());
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void searchDocument(SearchDocumentRequest request, StreamObserver<SearchDocumentResponse> responseObserver) {
        var resp = searchService.searchDocument(request.getIndex(), request.getQuery(), request.hasOffset() ? request.getOffset() : 0, request.hasLimit() ? request.getLimit() : 20);
        responseObserver.onNext(SearchDocumentResponse.newBuilder().addAllHits(resp.stream().map(it -> {
            try {
                return objectMapper.writeValueAsString(it);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList()).build());
        responseObserver.onCompleted();
    }
}
