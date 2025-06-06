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

    /**
     * @author Pedro Aldama
     * @return Set obtiene la lista de deseos de los items
     */
    @Override
    public Set<String> getWishList() {
        return getWishListFromRepository(getUser()).getEvolutionItems();
    }

    /**
     * @author Pedro Aldama
     * @param item el nombre del item a agregar
     * @return String retorna un mensaje de acuerdo al tipo de item y si este es agregado a
     * la wishlist o no, dependiendo si es valido o si existe su grupo
     */
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
    /**
     * @author Pedro Aldama
     * @param item el nombre del item a eliminar
     * @return String elimina el item de la lista si existe, si no se
     * retorna un mensaje diciendo que no esta en wishlist
     */
    @Override
    public String removeWishList( String item) {
        WishList wishList = getWishListFromRepository(getUser());
        if (wishList.getEvolutionItems().remove(item)){
            wishListRepository.save(wishList);
            return "Item from WishList was removed";
        }
        return "You have already removed a wishlist this item";
    }
    /**
     * @author Pedro Aldama
     * @return String mensaje de creacion de la wishlist o mensaje de que ya existe
     */
    @Override
    public String createWishList() {
        if(wishListRepository.findById(getUser()).isPresent()){
            return "WishList already exists";
        }
        WishList wishList = WishList.builder().id(getUser()).evolutionItems(new HashSet<>()).build();
        wishListRepository.save(wishList);
        return "Congratulations!! You have a new WishList!";
    }

    /**
     * @author Pedro Aldama
     * @param item el nombre del item a comprobar
     * @return boolean retorna si el item se encuentra en la WishList
     */
    @Override
    public boolean isOnWishList( String item) {
        WishList wishList = getWishListFromRepository(getUser());
        return wishList.getEvolutionItems().contains(item);
    }
    /**
     * @author Pedro Aldama
     * @return List retorna todas las wishlist, se pensaba para que al actualizar
     * la tienda, se notificara a cada usuario si algun item de su wishlist
     * estaba disponible para su compra
     */
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
