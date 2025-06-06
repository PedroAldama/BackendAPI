package com.backendapi.controllers;

import com.backendapi.dto.responsedto.DTOShopResponse;
import com.backendapi.services.shop.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {
    private final ShopService shopService;

    @GetMapping
    public ResponseEntity<DTOShopResponse> getShop() {
        return ResponseEntity.ok().body(shopService.showShop());
    }

    @PostMapping("/buy")
    public ResponseEntity<String> getShop(@RequestParam String type, @RequestParam String item) {
        return ResponseEntity.ok().body(shopService.buySomething(type,item));
    }
}
