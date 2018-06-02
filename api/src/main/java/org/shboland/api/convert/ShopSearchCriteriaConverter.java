package org.shboland.api.convert;

import org.shboland.domain.entities.JsonShopSearchCriteria;
import org.shboland.persistence.criteria.ShopSearchCriteria;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("shopSearchCriteriaConverter")
public class ShopSearchCriteriaConverter {

    public ShopSearchCriteria createSearchCriteria(JsonShopSearchCriteria jsonShopSearchCriteria) {
        ShopSearchCriteria.ShopSearchCriteriaBuilder searchCriteriaBuilder = ShopSearchCriteria.builder();

        searchCriteriaBuilder.start(jsonShopSearchCriteria.getStart());
        if (jsonShopSearchCriteria.getMaxResults() > 0) {
            searchCriteriaBuilder.maxResults(jsonShopSearchCriteria.getMaxResults());
        } else {
            throw new ConvertException("Maximum number of results should be a positive number.");
        }

        Long id = jsonShopSearchCriteria.getId();
        searchCriteriaBuilder.id(Optional.ofNullable(id));
        
        Long number = jsonShopSearchCriteria.getNumber();
        searchCriteriaBuilder.number(Optional.ofNullable(number));
    
        // @Input

        return searchCriteriaBuilder.build();
    }
    
    // @Function input
}