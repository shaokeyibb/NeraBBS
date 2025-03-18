package io.hikarilan.nerabbs.services.search.controller;

import io.hikarilan.nerabbs.services.search.data.vo.HitVo;
import io.hikarilan.nerabbs.services.search.service.HitService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/hit")
@Validated
public class HitController {


    private final HitService hitService;

    @GetMapping("/{topic}/{key}")
    @ResponseBody
    public HitVo getHitCount(@PathVariable String topic, @PathVariable String key) {
        return hitService.getHitCount(topic, key);
    }

}
