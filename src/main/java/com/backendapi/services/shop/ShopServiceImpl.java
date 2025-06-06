package com.backendapi.services.shop;

import com.backendapi.documents.Shop;
import com.backendapi.dto.responsedto.DTOShopResponse;
import com.backendapi.exceptions.PokeApiException;
import com.backendapi.repositories.ShopRepository;
import com.backendapi.services.bag.BagService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

import static com.backendapi.utils.Mapper.shopToDTOShopResponse;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService{

    private final ShopRepository shopRepository;
    private final BagService bagService;
    private final WebClient webClient;

    @Override
    public DTOShopResponse showShop() {
        Shop shop = getShop();
        return shopToDTOShopResponse(shop);
    }

    @Override
    @Transactional
    public String buySomething(String type, String product) {
        Shop shop = getShop();
        Map<String,Integer> items;
        if(type.equalsIgnoreCase("consumable")){
            items = shop.getConsumableItems();
        } else if (type.equalsIgnoreCase("evolution")) {
            items = shop.getEvolutionItems();
        } else {
            return "That is not a valid type";
        }
        if(!items.containsKey(product)) return "That is not a valid product";

        long userMoney = bagService.checkMoney();

        if(userMoney < items.get(product)) return "You don't have enough money";

        bagService.addItemToBag(type,product);
        bagService.setNewValance(items.get(product),"sub");

        items.remove(product);
        shopRepository.save(shop);

        return "You have bought " + product + ", your new valance is " + bagService.checkMoney();
    }

    @Override
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateShop() {
        //call to api to update new products every X time in mongo
        Shop shop = getShop();
        HashMap<String,Integer> consumableItems = getRandomItemsWithRandomValue("medicine",5);
        HashMap<String,Integer> evolutionItems = getRandomItemsWithRandomValue("evolution",5);
        shop.setConsumableItems(consumableItems);
        shop.setEvolutionItems(evolutionItems);
        shopRepository.save(shop);
    }
    private Shop getShop(){
        Optional<Shop> shop = shopRepository.findById("Shop");
        if(shop.isPresent()) return shop.get();

        Shop.builder().id("Shop").consumableItems(new HashMap<>()).evolutionItems(new HashMap<>()).build();
        return shopRepository.save(Shop.builder().id("Shop").build());
    }

    private HashMap<String, Integer> getRandomItemsWithRandomValue(String categoryName, int count) {
        String url = "/item-category/" + categoryName + "/";

        ItemCategoryResponse response = webClient.get()
                .uri(url)
                .retrieve()
                .onStatus(
                        status -> status.value() == 404,
                        clientResponse -> Mono.error(new RuntimeException("Category Not found"))
                )
                .bodyToMono(ItemCategoryResponse.class)
                .block();

        if (response == null || response.items == null) {
            return new HashMap<>();
        }

        List<String> allItems = response.items.stream()
                .map(item -> item.name)
                .collect(Collectors.toList());

        Collections.shuffle(allItems);

        HashMap<String, Integer> resultMap = new HashMap<>();

        for (String itemName : allItems.stream().limit(count).toList()) {
            int randomValue = 10 + (int)(Math.random() * 91);
            resultMap.put(itemName, randomValue);
        }

        return resultMap;
    }
    public static class ItemCategoryResponse {
        public List<Item> items;
    }

    public static class Item {
        public String name;
        public String url;
    }

}
