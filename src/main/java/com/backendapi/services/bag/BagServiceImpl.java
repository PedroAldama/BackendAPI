package com.backendapi.services.bag;

import com.backendapi.documents.Bag;
import com.backendapi.dto.responsedto.DTOBagResponse;
import com.backendapi.exceptions.BagException;
import com.backendapi.repositories.BagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.backendapi.utils.Mapper.bagToDTOBagResponse;
import static com.backendapi.utils.SecurityUtils.getAuthenticatedUsername;

/**
 * @author Pedro Aldama
 * Servicio que se encarga de la mochila del jugador
 */
@Service
@RequiredArgsConstructor
public class BagServiceImpl implements BagService{

    private final BagRepository bagRepository;

    /**
     * @author Pedro Aldama
     * @return retorna un DTOBagResponse que consta de Money, y Map de consumableItems
     * Map de evolution Items

     */
    @Override
    @Transactional(readOnly = true)
    public DTOBagResponse showBag() {
        return bagToDTOBagResponse(bagRepository.findById(Objects.requireNonNull(getAuthenticatedUsername()))
                .orElseThrow(BagException.BagNotFoundException::new));
    }
    /**
     * @author Pedro Aldama
     * @param type Tipo de item que se agregara, puede ser Consumable o Evolution
     * @param item Nombre del item que se quiere asignar a la mochila
     *
     * @return String retorna un mensaje con la opcion de agregado, o si no fue posible agregar

     */

    @Override
    @Transactional
    public String addItemToBag(String type, String item) {
        Bag bag = getBag();
        if(type.equalsIgnoreCase("Consumable")) {
            bag.getConsumableItems().putIfAbsent(item, 0);
            bag.getConsumableItems().put(item, bag.getConsumableItems().get(item) + 1);
            bagRepository.save(bag);
            return item + " has been added to Consumable items in the bag";

        } else if(type.equalsIgnoreCase("Evolution")) {
            bag.getEvolutionItems().putIfAbsent(item, 0);
            bag.getEvolutionItems().put(item, bag.getEvolutionItems().get(item) + 1);
            bagRepository.save(bag);
            return item + " has been added to Evolution items in the bag";
        }

        return "Item no valid to add to the bag";
    }
    /**
     * @author Pedro Aldama
     * @param type Tipo de item que se agregara, puede ser Consumable o Evolution
     * @param item Nombre del item que se quiere asignar a la mochila
     *
     * @return boolean la funcion se encarga de remover un item de la mochila
     * esto pasa cuando se le da a un pokemon y necesitamos solo el boolean

     */

    @Override
    @Transactional
    public boolean removeItemFromBag(String type, String item) {
        Bag bag = getBag();
        Map<String,Integer> items = type.equalsIgnoreCase("consumable")
                ? bag.getConsumableItems() : bag.getEvolutionItems();

        if(items.containsKey(item)) {
            items.put(item, items.get(item) - 1);
            if(items.get(item) == 0) {
                items.remove(item);
            }
                bagRepository.save(bag);
                return true;
        }
        return false;
    }
    /**
     * @author Pedro Aldama
     * @param type Tipo de item que se agregara, puede ser Consumable o Evolution
     * @param item Nombre del item que se quiere asignar a la mochila
     *
     * @return String la funcion se encarga de remover un item de la mochila
     * esto pasa cuando se le da a un pokemon, aqui ademas retorna un mensaje

     */

    @Override
    @Transactional
    public String useItem(String type, String item) {
        Bag bag = getBag();
        Map<String,Integer> items = type.equalsIgnoreCase("consumable")
                ? bag.getConsumableItems() : bag.getEvolutionItems();

        if(items.containsKey(item)) {
            items.put(item, items.get(item) - 1);
            if(items.get(item) == 0) {
                items.remove(item);
                return "item " + item + " has been removed from the bag";
            }
                bagRepository.save(bag);
                return "Now you have " + items.get(item) + " in the bag";
        }
        return "You don't have " + item + " in the bag";
    }
    /**
     * @author Pedro Aldama
     *
     * @return long la cantidad actual de dinero que tenemos en la mochila
     */
    @Override
    @Transactional(readOnly = true)
    public long checkMoney() {
        return getBag().getMoney();
    }
    /**
     * @author Pedro Aldama
     * @param amount la cantidad que se modificara Money en nuestra mochila
     * @param operation el tipo de operacion que se realizara a Money, add para agregar
     *                  sub para restar
     *
     * @return long la cantidad actual de dinero que tenemos en la mochila despues de la operacion
     */
    @Override
    @Transactional
    public long setNewValance( long amount, String operation) {
        Bag bag = getBag();
        if(operation.equalsIgnoreCase("add")) {
            bag.setMoney(bag.getMoney() + amount);
        }else if(operation.equalsIgnoreCase("sub")) {
            if(bag.getMoney() < amount) {
                //throw new Exception( "You don't have enough money");
                return bag.getMoney() - amount;
            }
            bag.setMoney(bag.getMoney() - amount);
        }
        bagRepository.save(bag);
        return bag.getMoney();
    }

    /**
     * @author Pedro Aldama
     * @param user El nombre del usuario de quien se creara, en este caso si necesita que se le pase
     *             el nombre porque se realiza cuando se registra un usuario nuevo
     *
     */
    @Override
    @Transactional
    public String createBag(String user) {
        bagRepository.save(Bag.builder().id(user)
                        .consumableItems(new HashMap<String, Integer>())
                        .evolutionItems(new HashMap<String, Integer>())
                .build());
        return "You have a new Bag!";
    }

    private Bag getBag() {
        return bagRepository.findById(Objects.requireNonNull(getAuthenticatedUsername()))
                .orElseThrow(BagException.BagNotFoundException::new);
    }

}
