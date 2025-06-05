package com.backendapi.services.shop;

import com.backendapi.dto.responsedto.DTOShopResponse;

public interface ShopService {
    DTOShopResponse showShop();
    String buySomething(String user, String type,String product);
    void updateShop();
}
