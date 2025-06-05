package com.backendapi.services.bag;

import com.backendapi.dto.responsedto.DTOBagResponse;

public interface BagService {
    DTOBagResponse showBag(String user);
    String addItemToBag(String user, String type, String item);
    boolean removeItemFromBag(String user, String type, String item);
    long checkMoney(String user);
    long setNewValance(String user, long amount, String operation);
    String createBag(String user);
    String useItem(String user, String type, String item);
}
