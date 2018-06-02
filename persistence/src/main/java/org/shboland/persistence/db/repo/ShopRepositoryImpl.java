package org.shboland.persistence.db.repo;

import org.shboland.persistence.db.hibernate.bean.Person;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Subquery;
import org.shboland.persistence.db.hibernate.bean.Shop;
import org.shboland.persistence.criteria.ShopSearchCriteria;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ShopRepositoryImpl extends AbstractHibernateRepository<Shop> implements ShopRepositoryCustom {

    private static final String ID_PROPERTY = "id";
    private static final String NUMBER_PROPERTY = "number";
    private static final String PERSON_SET_PROPERTY = "personSet";
    // @Property input

    @Override
    protected Class<Shop> getDomainClass() {
        return Shop.class;
    }

    @Override
    public int findNumberOfShopBySearchCriteria(ShopSearchCriteria sc) {
        CriteriaBuilder criteria = getDefaultCriteria();
        CriteriaQuery<Long> criteriaQuery = criteria.createQuery(Long.class);
        Root<Shop> root = criteriaQuery.from(getDomainClass());

        List<Predicate> predicates = createPredicates(criteriaQuery, sc, criteria, root);

        criteriaQuery.select(criteria.count(root)).distinct(true)
                .where(predicates.toArray(new Predicate[predicates.size()]));

        return getEntityManager()
                .createQuery(criteriaQuery)
                .getSingleResult()
                .intValue();
    }

    @Override
    public List<Shop> findBySearchCriteria(ShopSearchCriteria sc) {
        CriteriaBuilder criteria = getDefaultCriteria();
        CriteriaQuery<Shop> criteriaQuery = criteria.createQuery(getDomainClass());
        Root<Shop> root = criteriaQuery.from(getDomainClass());

        List<Predicate> predicates = createPredicates(criteriaQuery, sc, criteria, root);

        criteriaQuery.select(root).distinct(true)
                .where(predicates.toArray(new Predicate[predicates.size()]));

        return getEntityManager()
                .createQuery(criteriaQuery)
                .setFirstResult(sc.getStart())
                .setMaxResults(sc.getMaxResults())
                .getResultList();
    }

    private List<Predicate> createPredicates(CriteriaQuery<?> criteriaQuery, ShopSearchCriteria sc, CriteriaBuilder criteria, Root<Shop> root) {

        List<Predicate> predicates = new ArrayList<>();

        sc.getId().ifPresent(id -> predicates.add(criteria.equal(root.get(ID_PROPERTY), id)));
        
        sc.getNumber().ifPresent(number -> predicates.add(criteria.equal(root.get(NUMBER_PROPERTY), number)));
    
        sc.getPersonId().ifPresent(personId -> {
            Subquery<Person> subquery = criteriaQuery.subquery(Person.class);
            Root<Shop> subRoot = subquery.correlate(root);
            Join<Shop, Person> shopPersons = subRoot.join(PERSON_SET_PROPERTY);
            subquery.select(shopPersons).where(criteria.equal(shopPersons.get(ID_PROPERTY), personId));

            predicates.add(criteria.exists(subquery));
            });
    
        // @Predicate input

        return predicates;
    }
}
