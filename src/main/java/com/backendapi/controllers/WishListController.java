package com.backendapi.controllers;

import com.backendapi.services.whislist.WishListService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/wishlist")
@AllArgsConstructor
public class WishListController {
    private WishListService wishListService;

    @GetMapping
    public Set<String> myWishList() {
        return wishListService.getWishList("Hikari");
    }
    @PostMapping("/create")
    public String create(){
        return wishListService.createWishList("Hikari");
    }
    @GetMapping("/verify")
    public boolean verify(@RequestParam String item){
        return wishListService.isOnWishList("Hikari",item);
    }
    @PostMapping("/add")
    public String add(@RequestParam String item){
        return wishListService.addWishList("Hikari",item);
    }
    @DeleteMapping("/delete")
    public String delete(@RequestParam String item){
        return wishListService.removeWishList("Hikari",item);
    }
}
