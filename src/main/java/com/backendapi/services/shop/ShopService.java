package com.backendapi.services.shop;

import com.backendapi.dto.responsedto.DTOShopResponse;

public interface ShopService {
    DTOShopResponse showShop();
    String buySomething(String type,String product);
    void updateShop();
}
