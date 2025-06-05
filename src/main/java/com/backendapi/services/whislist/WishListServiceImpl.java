package com.backendapi.services.whislist;

import com.backendapi.documents.WishList;
import com.backendapi.repositories.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static com.backendapi.utils.VerifyItemType.verifyItemType;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishListRepository;

    @Override
    public Set<String> getWishList(String user) {
        return getWishListFromRepository(user).getEvolutionItems();
    }

    @Override
    public String addWishList(String user, String item) {
        if(!verifyItemType(item).equals("Evolution")) {
            return "Invalid item";
        }
        WishList wishList = getWishListFromRepository(user);
        if (wishList.getEvolutionItems().add(item)){
            wishListRepository.save(wishList);
            return "WishList added";
        }
        return "You have already added a wishlist this item";
    }

    @Override
    public String removeWishList(String user, String item) {
        WishList wishList = getWishListFromRepository(user);
        if (wishList.getEvolutionItems().remove(item)){
            wishListRepository.save(wishList);
            return "Item from WishList was removed";
        }
        return "You have already removed a wishlist this item";
    }

    @Override
    public String createWishList(String user) {
        if(wishListRepository.findById(user).isPresent()){
            return "WishList already exists";
        }
        WishList wishList = WishList.builder().id(user).evolutionItems(new HashSet<>()).build();
        wishListRepository.save(wishList);
        return "Congratulations!! You have a new WishList!";
    }

    @Override
    public boolean isOnWishList(String user, String item) {
        WishList wishList = getWishListFromRepository(user);
        return wishList.getEvolutionItems().contains(item);
    }

    private WishList getWishListFromRepository(String user) {
        return wishListRepository.findById(user).orElseThrow();
    }

}
