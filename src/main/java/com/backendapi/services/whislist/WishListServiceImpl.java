package com.backendapi.services.whislist;

import com.backendapi.documents.WishList;
import com.backendapi.repositories.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static com.backendapi.utils.VerifyItemType.verifyItemType;
import static com.backendapi.utils.SecurityUtils.getAuthenticatedUsername;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishListRepository;

    @Override
    public Set<String> getWishList() {
        return getWishListFromRepository(getUser()).getEvolutionItems();
    }

    @Override
    public String addWishList(String item) {
        if(!verifyItemType(item).equals("Evolution")) {
            return "Invalid item";
        }
        WishList wishList = getWishListFromRepository(getUser());
        if (wishList.getEvolutionItems().add(item)){
            wishListRepository.save(wishList);
            return "WishList added";
        }
        return "You have already added a wishlist this item";
    }

    @Override
    public String removeWishList( String item) {
        WishList wishList = getWishListFromRepository(getUser());
        if (wishList.getEvolutionItems().remove(item)){
            wishListRepository.save(wishList);
            return "Item from WishList was removed";
        }
        return "You have already removed a wishlist this item";
    }

    @Override
    public String createWishList() {
        if(wishListRepository.findById(getUser()).isPresent()){
            return "WishList already exists";
        }
        WishList wishList = WishList.builder().id(getUser()).evolutionItems(new HashSet<>()).build();
        wishListRepository.save(wishList);
        return "Congratulations!! You have a new WishList!";
    }

    @Override
    public boolean isOnWishList( String item) {
        WishList wishList = getWishListFromRepository(getUser());
        return wishList.getEvolutionItems().contains(item);
    }

    @Override
    public List<WishList> getAllWishList() {
        return wishListRepository.findAll();
    }

    private WishList getWishListFromRepository(String user) {
        return wishListRepository.findById(user).orElseThrow();
    }
    private String getUser(){
        return getAuthenticatedUsername();
    }


}
