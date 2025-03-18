package io.hikarilan.nerabbs.services.search.controller;

import io.hikarilan.nerabbs.services.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

@RequiredArgsConstructor
@RestController
@RequestMapping("/search")
@Validated
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/{index}/{query}")
    public ArrayList<HashMap<String, Object>> search(@PathVariable String index, @PathVariable String query, @RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "20") int limit) {
        return searchService.searchDocument(index, query, offset, limit);
    }

}
