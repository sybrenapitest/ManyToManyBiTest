package org.shboland.persistence.db.repo;

import org.shboland.persistence.db.hibernate.bean.Shop;
import org.shboland.persistence.criteria.ShopSearchCriteria;

import java.util.List;

public interface ShopRepositoryCustom {

    int findNumberOfShopBySearchCriteria(ShopSearchCriteria sc);

    List<Shop> findBySearchCriteria(ShopSearchCriteria sc);
}
