package org.shboland.persistence.db.repo;

import org.shboland.persistence.db.hibernate.bean.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopRepository extends JpaRepository<Shop, Long>, ShopRepositoryCustom {
}