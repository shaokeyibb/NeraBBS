package io.hikarilan.nerabbs.services.search.controller;

import io.hikarilan.nerabbs.services.search.data.vo.HitVo;
import io.hikarilan.nerabbs.services.search.service.TrendingService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/trending")
@Validated
public class TrendingController {

    private final TrendingService trendingService;

    @GetMapping("/{topic}")
    @ResponseBody
    public List<HitVo> getTrending(@PathVariable String topic, @RequestParam(defaultValue = "10") int limit) {
        return trendingService.getTrending(topic, limit);
    }

}
