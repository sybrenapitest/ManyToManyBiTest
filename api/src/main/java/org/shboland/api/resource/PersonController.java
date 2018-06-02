package org.shboland.api.resource;

import org.shboland.api.convert.ShopConverter;
import org.shboland.core.service.ShopService;
import org.shboland.domain.entities.JsonShop;
import org.shboland.persistence.db.hibernate.bean.Shop;
import org.shboland.persistence.db.hibernate.bean.Person;
import java.util.stream.Collectors;
import javax.ws.rs.BeanParam;
import java.util.List;
import java.util.ArrayList;
import org.shboland.persistence.criteria.PersonSearchCriteria;
import org.shboland.api.convert.ConvertException;
import org.shboland.domain.entities.JsonSearchResult;
import org.shboland.domain.entities.JsonPersonSearchCriteria;
import lombok.extern.slf4j.Slf4j;
import org.shboland.api.convert.PersonSearchCriteriaConverter;
import java.net.URISyntaxException;
import org.springframework.http.HttpStatus;
import org.shboland.domain.entities.JsonPerson;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.shboland.core.service.PersonService;
import org.shboland.api.convert.PersonConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PersonController implements IPersonController {

    private final PersonService personService;
    private final PersonConverter personConverter;
    private final PersonSearchCriteriaConverter personSearchCriteriaConverter;
    private final ShopService shopService;
    private final ShopConverter shopConverter;
    // @FieldInput

    @Autowired
    public PersonController(ShopConverter shopConverter, ShopService shopService, PersonSearchCriteriaConverter personSearchCriteriaConverter, PersonService personService, PersonConverter personConverter) {
        this.personService = personService;
        this.personConverter = personConverter;
        this.personSearchCriteriaConverter = personSearchCriteriaConverter;
        this.shopService = shopService;
        this.shopConverter = shopConverter;
        // @ConstructorInput
    }
    
    // @Input

    @Override
    public ResponseEntity deleteShopWithPerson(@PathVariable long personId, @PathVariable long shopId) {

        return personService.removeShop(personId, shopId) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity putShopWithPerson(@PathVariable long personId, @PathVariable long shopId) {

        return personService.updatePersonWithShop(personId, shopId) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity getShops(@PathVariable long personId) {
        List<Shop> shopList = shopService.fetchShopsForPerson(personId);

        JsonSearchResult<JsonShop> result = JsonSearchResult.<JsonShop>builder()
                .results(shopList.stream().map(shopConverter::toJson).collect(Collectors.toList()))
                .numberOfResults(shopList.size())
                .grandTotalNumberOfResults(shopList.size())
                .build();
        
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<JsonPerson> getPerson(@PathVariable long personId) {
        Optional<Person> personOptional = personService.fetchPerson(personId);

        return personOptional.isPresent() ?
                ResponseEntity.ok(personConverter.toJson(personOptional.get())) :
                ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<JsonSearchResult> list(@BeanParam JsonPersonSearchCriteria searchCriteria) {

        PersonSearchCriteria sc;
        try {
            sc = personSearchCriteriaConverter.createSearchCriteria(searchCriteria);
        } catch (ConvertException e) {
            log.warn("Conversion failed!", e);
            return ResponseEntity.badRequest().build();
        }

        List<Person> personList = new ArrayList<>();
        int numberOfPerson = personService.findNumberOfPerson(sc);
        if (numberOfPerson > 0) {
            personList = personService.findBySearchCriteria(sc);
        }

        JsonSearchResult<JsonPerson> result = JsonSearchResult.<JsonPerson>builder()
                .results(personList.stream().map(personConverter::toJson).collect(Collectors.toList()))
                .numberOfResults(personList.size())
                .grandTotalNumberOfResults(numberOfPerson)
                .build();

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity postPerson(@RequestBody JsonPerson jsonPerson) throws URISyntaxException {
            
        Person newPerson = personService.save(personConverter.fromJson(jsonPerson));

        return ResponseEntity.status(HttpStatus.CREATED).body(personConverter.toJson(newPerson));
    }

    @Override
    public ResponseEntity<JsonPerson> putPerson(@PathVariable long personId, @RequestBody JsonPerson jsonPerson) {

        Optional<Person> personOptional = personService.fetchPerson(personId);

        Person savedPerson;
        if (!personOptional.isPresent()) {
            savedPerson = personService.save(personConverter.fromJson(jsonPerson));
        } else {
            savedPerson = personService.save(personConverter.fromJson(jsonPerson, personId));
        }

        return ResponseEntity.ok(personConverter.toJson(savedPerson));
    }

    @Override
    public ResponseEntity deletePerson(@PathVariable long personId) {

        return personService.deletePerson(personId) ?
                ResponseEntity.ok().build() :
                ResponseEntity.notFound().build();
    }
    
}