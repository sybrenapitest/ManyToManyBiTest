package org.shboland.api.convert;

import org.shboland.persistence.db.hibernate.bean.Shop;
import org.shboland.domain.entities.JsonShop;
import org.shboland.api.resource.ShopController;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Service
public class ShopConverter {
    
    public JsonShop toJson(Shop shop) {
        JsonShop jsonShop = JsonShop.builder()
                .number(shop.getNumber())
                // @InputJsonField
                .build();
        
        jsonShop.add(linkTo(ShopController.class).slash(shop.getId()).withSelfRel());
        // @InputLink
        
        jsonShop.add(linkTo(ShopController.class).slash(shop.getId()).slash("/persons").withRel("persons"));

        return jsonShop;
    }
    
    public Shop fromJson(JsonShop jsonShop) {
        return shopBuilder(jsonShop).build();
    }

    public Shop fromJson(JsonShop jsonShop, long shopId) {
        return shopBuilder(jsonShop)
                .id(shopId)
                .build();
    }

    private Shop.ShopBuilder shopBuilder(JsonShop jsonShop) {

        return Shop.builder()
                .number(jsonShop.getNumber())
                // @InputBeanField
        ;
    }
}