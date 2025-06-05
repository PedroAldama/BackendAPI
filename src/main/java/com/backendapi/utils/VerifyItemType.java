package com.backendapi.utils;

import java.util.Set;

public class VerifyItemType {
    private static Set<String> consumable =  Set.of("cheri-berry","chesto-berry","pecha-berry","rawst-berry","aspear-berry"
    ,"leppa-berry","oran-berry","persim-berry","lum-berry","sitrus-berry");


    private static Set<String> evolution = Set.of("sun-stone",
            "moon-stone",
            "fire-stone",
            "thunder-stone",
            "water-stone",
            "leaf-stone",
            "shiny-stone",
            "dusk-stone",
            "dawn-stone",
            "oval-stone",
            "dragon-scale",
            "up-grade",
            "protector",
            "electirizer",
            "magmarizer",
            "dubious-disc",
            "reaper-cloth",
            "prism-scale",
            "whipped-dream",
            "sachet",
            "ice-stone",
            "strawberry-sweet",
            "love-sweet",
            "berry-sweet",
            "clover-sweet",
            "flower-sweet",
            "star-sweet",
            "ribbon-sweet",
            "sweet-apple",
            "tart-apple",
            "cracked-pot",
            "chipped-pot",
            "malicious-armor",
            "syrupy-apple",
            "unremarkable-teacup",
            "masterpiece-teacup",
            "linking-cord",
            "black-augurite",
            "peat-block");

    public static String verifyItemType(String name){
        if(consumable.contains(name)) return "Consumable";
        if(evolution.contains(name)) return "Evolution";
        return "No type found";
    }


}
