package org.shboland.api.resource;

import javax.ws.rs.BeanParam;
import org.shboland.domain.entities.JsonSearchResult;
import org.shboland.domain.entities.JsonShopSearchCriteria;
import java.net.URISyntaxException;
import org.shboland.domain.entities.JsonShop;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/shops")
public interface IShopController {

    // @Input

    @RequestMapping(value = "/{shopId}/persons/{personId}", method = RequestMethod.DELETE)
    ResponseEntity deletePersonWithShop(@PathVariable("shopId") long shopId, @PathVariable("personId") long personId);

    @RequestMapping(value = "/{shopId}/persons/{personId}", method = RequestMethod.PUT)
    ResponseEntity putPersonWithShop(@PathVariable("shopId") long shopId, @PathVariable("personId") long personId);

    @RequestMapping(path = "/{shopId}/persons", method = RequestMethod.GET)
    ResponseEntity getPersons(@PathVariable long shopId);

    @RequestMapping(path = "/{shopId}", method = RequestMethod.GET)
    ResponseEntity<JsonShop> getShop(@PathVariable long shopId);
    
    @RequestMapping(path = "", method = RequestMethod.GET)
    ResponseEntity<JsonSearchResult> list(@BeanParam JsonShopSearchCriteria searchCriteria);
    
    @RequestMapping(value = "", method = RequestMethod.POST)
    ResponseEntity postShop(@RequestBody JsonShop shop) throws URISyntaxException;
    
    @RequestMapping(value = "/{shopId}", method = RequestMethod.PUT)
    ResponseEntity putShop(@PathVariable("shopId") long shopId, @RequestBody JsonShop jsonShop);
    
    @RequestMapping(value = "/{shopId}", method = RequestMethod.DELETE)
    ResponseEntity deleteShop(@PathVariable("shopId") long shopId);
    
}