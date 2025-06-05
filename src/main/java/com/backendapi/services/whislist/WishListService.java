package com.backendapi.services.whislist;

import java.util.Set;

public interface WishListService {
    Set<String> getWishList(String user);
    String addWishList(String user, String item);
    String removeWishList(String user, String item);
    String createWishList(String user);
    boolean isOnWishList(String user, String item);
}
