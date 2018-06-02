package org.shboland.core.service;

import org.shboland.persistence.db.repo.PersonRepository;
import org.shboland.persistence.db.hibernate.bean.Person;
import java.util.List;
import org.shboland.persistence.criteria.ShopSearchCriteria;
import java.util.Optional;
import org.shboland.persistence.db.hibernate.bean.Shop;
import org.shboland.persistence.db.repo.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ShopService {

    private final ShopRepository shopRepository;
    private final PersonRepository personRepository;
    // @FieldInput

    @Autowired
    public ShopService(PersonRepository personRepository, ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
        this.personRepository = personRepository;
        // @ConstructorInput
    }
    
    // @Input

    public boolean removePerson(long shopId, long personId) {
        Optional<Shop> shopOptional = shopRepository.findById(shopId);
        if (shopOptional.isPresent()) {
            Shop shop = shopOptional.get();
        
            Optional<Person> personOptional = personRepository.findById(personId);
            if (personOptional.isPresent()) {
                Person person = personOptional.get();

                shop.getPersonSet().remove(person);
                shopRepository.save(shop);
                return true;
            }
        }

        return false;
    }

    public boolean updateShopWithPerson(long shopId, long personId) {
        Optional<Shop> shopOptional = shopRepository.findById(shopId);
        if (shopOptional.isPresent()) {
            Shop shop = shopOptional.get();

            Optional<Person> personOptional = personRepository.findById(personId);
            if (personOptional.isPresent()) {
                Person person = personOptional.get();

                shop.getPersonSet().add(person);
                shopRepository.save(shop);
                return true;
            }
        }

        return false;
    }

    public List<Shop> fetchShopsForPerson(long personId) {
        ShopSearchCriteria shopSearchCriteria =  ShopSearchCriteria.builder()
                .personId(Optional.of(personId))
                .build();

        return shopRepository.findBySearchCriteria(shopSearchCriteria);
    }
  
    public int findNumberOfShop(ShopSearchCriteria sc) {
        return shopRepository.findNumberOfShopBySearchCriteria(sc);
    }
    

    public List<Shop> findBySearchCriteria(ShopSearchCriteria sc) {
        return shopRepository.findBySearchCriteria(sc);
    }

    public Shop save(Shop shop) {
        return shopRepository.save(shop);
    }

    public Optional<Shop> fetchShop(long shopId) {
        return shopRepository.findById(shopId);
    }

    public boolean deleteShop(long shopId) {
        Optional<Shop> shop = shopRepository.findById(shopId);

        if (shop.isPresent()) {
            shopRepository.delete(shop.get());
            return true;
        } else {
            return false;
        }
    }
    
}