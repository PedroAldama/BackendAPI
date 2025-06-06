package com.backendapi.controllers;

import com.backendapi.dto.responsedto.DTOBagResponse;
import com.backendapi.services.bag.BagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/bag")
@RequiredArgsConstructor
public class BagController {
    private final BagService bagService;

    @GetMapping
    public DTOBagResponse getMyBag() {
        return bagService.showBag();
    }

    @GetMapping("/money/check")
    public long checkMoney(){
        return bagService.checkMoney();
    }

}
