package com.backendapi.services.bag;

import com.backendapi.dto.responsedto.DTOBagResponse;

public interface BagService {
    DTOBagResponse showBag();
    String addItemToBag( String type, String item);
    boolean removeItemFromBag( String type, String item);
    long checkMoney();
    long setNewValance( long amount, String operation);
    String createBag(String user);
    String useItem(String type, String item);
}
