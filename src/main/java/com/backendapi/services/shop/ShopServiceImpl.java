package com.backendapi.services.shop;

import com.backendapi.documents.Shop;
import com.backendapi.dto.responsedto.DTOShopResponse;
import com.backendapi.repositories.ShopRepository;
import com.backendapi.services.bag.BagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.backendapi.utils.Mapper.shopToDTOShopResponse;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService{

    private final ShopRepository shopRepository;
    private final BagService bagService;

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
    public void updateShop() {
        //call to redis to update new products every X time
    }
    private Shop getShop(){
        return shopRepository.findById("Shop").orElseThrow();
    }
}
