package integration;
         
import java.util.HashSet;
import java.util.Collections;
import org.shboland.persistence.db.repo.ShopRepository;
import org.shboland.persistence.db.hibernate.bean.Shop;
import org.shboland.domain.entities.JsonShop;
import org.shboland.persistence.db.repo.PersonRepository;
import org.shboland.persistence.db.hibernate.bean.Person;
import org.shboland.domain.entities.JsonPerson;

public class IntegrationTestFactory {

    // @Input

   public static Shop givenAShopWithPerson(ShopRepository shopRepository, PersonRepository personRepository) {
        Person person = personRepository.save(Person.builder()
            .shopSet(new HashSet<>())
            .build());

        Shop shop = shopRepository.save(Shop.builder()
                .personSet(Collections.singleton(person))
                // @FieldInput
                .build());
        
        person.getShopSet().add(shop);
        return shop;
    }

   public static Person givenAPersonWithShop(PersonRepository personRepository, ShopRepository shopRepository) {
        Person person = personRepository.save(Person.builder()
            .shopSet(new HashSet<>())
            .build());

        Shop shop = shopRepository.save(Shop.builder()
                .personSet(Collections.singleton(person))
                // @FieldInput
                .build());

        person.getShopSet().add(shop);
        return person;
    }

    public static Shop givenAShop(ShopRepository shopRepository) {
        return givenAShop(Shop.builder()
                 .number(3147483647L)
                // @FieldInputShopBean
                .build(),
                shopRepository);
    }
    
    public static Shop givenAShop(Shop shop, ShopRepository shopRepository) {
        return shopRepository.save(shop);
    }
    
    public static JsonShop givenAJsonShop() {
        return JsonShop.builder()
                 .number(4447483647L)
                // @FieldInputJsonShop
                .build();
    }
        

    public static Person givenAPerson(PersonRepository personRepository) {
        return givenAPerson(Person.builder()
                 .age(3)
                // @FieldInputPersonBean
                .build(),
                personRepository);
    }
    
    public static Person givenAPerson(Person person, PersonRepository personRepository) {
        return personRepository.save(person);
    }
    
    public static JsonPerson givenAJsonPerson() {
        return JsonPerson.builder()
                 .age(4)
                // @FieldInputJsonPerson
                .build();
    }
        
}
