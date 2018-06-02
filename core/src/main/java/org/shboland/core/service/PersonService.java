package org.shboland.core.service;

import org.shboland.persistence.db.repo.ShopRepository;
import org.shboland.persistence.db.hibernate.bean.Shop;
import java.util.List;
import org.shboland.persistence.criteria.PersonSearchCriteria;
import java.util.Optional;
import org.shboland.persistence.db.hibernate.bean.Person;
import org.shboland.persistence.db.repo.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class PersonService {

    private final PersonRepository personRepository;
    private final ShopRepository shopRepository;
    // @FieldInput

    @Autowired
    public PersonService(ShopRepository shopRepository, PersonRepository personRepository) {
        this.personRepository = personRepository;
        this.shopRepository = shopRepository;
        // @ConstructorInput
    }
    
    // @Input

    public boolean removeShop(long personId, long shopId) {
        Optional<Person> personOptional = personRepository.findById(personId);
        if (personOptional.isPresent()) {
            Person person = personOptional.get();

            Optional<Shop> shopOptional = person.getShopSet().stream()
                    .filter(shop -> shop.getId() == shopId).findFirst();

            if (shopOptional.isPresent()) {
                Shop shop = shopOptional.get();
            
            shop.getPersonSet().remove(person);
                shopRepository.save(shop);
                return true;
            }
        }

        return false;
    }

    public boolean updatePersonWithShop(long personId, long shopId) {
        Optional<Person> personOptional = personRepository.findById(personId);
        if (personOptional.isPresent()) {
            Person person = personOptional.get();

            Optional<Shop> shopOptional = shopRepository.findById(shopId);
            if (shopOptional.isPresent()) {
                Shop shop = shopOptional.get();

                shop.getPersonSet().add(person);
                shopRepository.save(shop);
                return true;
            }
        }

        return false;
    }

    public List<Person> fetchPersonsForShop(long shopId) {
        PersonSearchCriteria personSearchCriteria =  PersonSearchCriteria.builder()
                .shopId(Optional.of(shopId))
                .build();

        return personRepository.findBySearchCriteria(personSearchCriteria);
    }
  
    public int findNumberOfPerson(PersonSearchCriteria sc) {
        return personRepository.findNumberOfPersonBySearchCriteria(sc);
    }
    

    public List<Person> findBySearchCriteria(PersonSearchCriteria sc) {
        return personRepository.findBySearchCriteria(sc);
    }

    public Person save(Person person) {
        return personRepository.save(person);
    }

    public Optional<Person> fetchPerson(long personId) {
        return personRepository.findById(personId);
    }

    public boolean deletePerson(long personId) {
        Optional<Person> person = personRepository.findById(personId);

        if (person.isPresent()) {
            personRepository.delete(person.get());
            return true;
        } else {
            return false;
        }
    }
    
}