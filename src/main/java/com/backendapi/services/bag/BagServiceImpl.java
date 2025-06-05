package com.backendapi.services.bag;

import com.backendapi.documents.Bag;
import com.backendapi.dto.responsedto.DTOBagResponse;
import com.backendapi.repositories.BagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static com.backendapi.utils.Mapper.bagToDTOBagResponse;
@Service
@RequiredArgsConstructor
public class BagServiceImpl implements BagService{

    private final BagRepository bagRepository;

    @Override
    @Transactional(readOnly = true)
    public DTOBagResponse showBag(String user) {
        return bagToDTOBagResponse(bagRepository.findById(user).orElseThrow());
    }

    @Override
    @Transactional
    public String addItemToBag(String user, String type, String item) {
        Bag bag = getBag(user);
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

    @Override
    @Transactional
    public boolean removeItemFromBag(String user, String type, String item) {
        Bag bag = getBag(user);
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

    @Override
    @Transactional
    public String useItem(String user, String type, String item) {
        Bag bag = getBag(user);
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

    @Override
    @Transactional(readOnly = true)
    public long checkMoney(String user) {
        return getBag(user).getMoney();
    }

    @Override
    @Transactional
    public long setNewValance(String user, long amount, String operation) {
        Bag bag = getBag(user);
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

    @Override
    @Transactional
    public String createBag(String user) {
        bagRepository.save(Bag.builder().id(user)
                        .consumableItems(new HashMap<String, Integer>())
                        .evolutionItems(new HashMap<String, Integer>())
                .build());
        return "You have a new Bag!";
    }

    private Bag getBag(String user) {
        return bagRepository.findById(user).orElseThrow();
    }

}
