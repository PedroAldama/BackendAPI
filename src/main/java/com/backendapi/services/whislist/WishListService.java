package com.backendapi.services.whislist;

import com.backendapi.documents.WishList;

import java.util.List;
import java.util.Set;

public interface WishListService {
    Set<String> getWishList();
    String addWishList(String item);
    String removeWishList(String item);
    String createWishList();
    boolean isOnWishList(String item);
    List<WishList> getAllWishList();
}
