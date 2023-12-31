package io.hikarilan.nerabbs.services.oss.controller;

import io.hikarilan.nerabbs.common.BizConstants;
import io.hikarilan.nerabbs.common.exception.UnauthorizedException;
import io.hikarilan.nerabbs.services.oss.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Controller
@RequestMapping("/storages")
public class StorageController {

    private final StorageService storageService;

    @SneakyThrows
    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestHeader(BizConstants.USER_ID_HEADER) long userID, @RequestParam MultipartFile file) {
        if (userID == BizConstants.USER_ID_UNAUTHORIZED)
            throw new UnauthorizedException();

        return ResponseEntity.ok(storageService.uploadFile(userID, file).object());
    }

    @SneakyThrows
    @GetMapping("/{object}")
    public ResponseEntity<Resource> getFile(@PathVariable String object) {
        var resource = storageService.getFileAndState(object);

        try (var file = resource.file()) {
            var res = new ByteArrayResource(file.readAllBytes());

            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf(resource.fileStat().contentType()))
                    .contentLength(resource.fileStat().size())
                    .eTag(resource.fileStat().etag())
                    .lastModified(resource.fileStat().lastModified())
                    .body(res);
        }
    }
}
