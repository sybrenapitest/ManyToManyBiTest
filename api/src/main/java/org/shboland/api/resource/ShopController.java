package org.shboland.api.resource;

import org.shboland.api.convert.PersonConverter;
import org.shboland.core.service.PersonService;
import org.shboland.domain.entities.JsonPerson;
import org.shboland.persistence.db.hibernate.bean.Person;
import org.shboland.persistence.db.hibernate.bean.Shop;
import java.util.stream.Collectors;
import javax.ws.rs.BeanParam;
import java.util.List;
import java.util.ArrayList;
import org.shboland.persistence.criteria.ShopSearchCriteria;
import org.shboland.api.convert.ConvertException;
import org.shboland.domain.entities.JsonSearchResult;
import org.shboland.domain.entities.JsonShopSearchCriteria;
import lombok.extern.slf4j.Slf4j;
import org.shboland.api.convert.ShopSearchCriteriaConverter;
import java.net.URISyntaxException;
import org.springframework.http.HttpStatus;
import org.shboland.domain.entities.JsonShop;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.shboland.core.service.ShopService;
import org.shboland.api.convert.ShopConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ShopController implements IShopController {

    private final ShopService shopService;
    private final ShopConverter shopConverter;
    private final ShopSearchCriteriaConverter shopSearchCriteriaConverter;
    private final PersonService personService;
    private final PersonConverter personConverter;
    // @FieldInput

    @Autowired
    public ShopController(PersonConverter personConverter, PersonService personService, ShopSearchCriteriaConverter shopSearchCriteriaConverter, ShopService shopService, ShopConverter shopConverter) {
        this.shopService = shopService;
        this.shopConverter = shopConverter;
        this.shopSearchCriteriaConverter = shopSearchCriteriaConverter;
        this.personService = personService;
        this.personConverter = personConverter;
        // @ConstructorInput
    }
    
    // @Input

    @Override
    public ResponseEntity deletePersonWithShop(@PathVariable long shopId, @PathVariable long personId) {

        return shopService.removePerson(shopId, personId) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity putPersonWithShop(@PathVariable long shopId, @PathVariable long personId) {

        return shopService.updateShopWithPerson(shopId, personId) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity getPersons(@PathVariable long shopId) {
        List<Person> personList = personService.fetchPersonsForShop(shopId);

        JsonSearchResult<JsonPerson> result = JsonSearchResult.<JsonPerson>builder()
                .results(personList.stream().map(personConverter::toJson).collect(Collectors.toList()))
                .numberOfResults(personList.size())
                .grandTotalNumberOfResults(personList.size())
                .build();
        
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<JsonShop> getShop(@PathVariable long shopId) {
        Optional<Shop> shopOptional = shopService.fetchShop(shopId);

        return shopOptional.isPresent() ?
                ResponseEntity.ok(shopConverter.toJson(shopOptional.get())) :
                ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<JsonSearchResult> list(@BeanParam JsonShopSearchCriteria searchCriteria) {

        ShopSearchCriteria sc;
        try {
            sc = shopSearchCriteriaConverter.createSearchCriteria(searchCriteria);
        } catch (ConvertException e) {
            log.warn("Conversion failed!", e);
            return ResponseEntity.badRequest().build();
        }

        List<Shop> shopList = new ArrayList<>();
        int numberOfShop = shopService.findNumberOfShop(sc);
        if (numberOfShop > 0) {
            shopList = shopService.findBySearchCriteria(sc);
        }

        JsonSearchResult<JsonShop> result = JsonSearchResult.<JsonShop>builder()
                .results(shopList.stream().map(shopConverter::toJson).collect(Collectors.toList()))
                .numberOfResults(shopList.size())
                .grandTotalNumberOfResults(numberOfShop)
                .build();

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity postShop(@RequestBody JsonShop jsonShop) throws URISyntaxException {
            
        Shop newShop = shopService.save(shopConverter.fromJson(jsonShop));

        return ResponseEntity.status(HttpStatus.CREATED).body(shopConverter.toJson(newShop));
    }

    @Override
    public ResponseEntity<JsonShop> putShop(@PathVariable long shopId, @RequestBody JsonShop jsonShop) {

        Optional<Shop> shopOptional = shopService.fetchShop(shopId);

        Shop savedShop;
        if (!shopOptional.isPresent()) {
            savedShop = shopService.save(shopConverter.fromJson(jsonShop));
        } else {
            savedShop = shopService.save(shopConverter.fromJson(jsonShop, shopId));
        }

        return ResponseEntity.ok(shopConverter.toJson(savedShop));
    }

    @Override
    public ResponseEntity deleteShop(@PathVariable long shopId) {

        return shopService.deleteShop(shopId) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }
    
}