package org.shboland.persistence.db.repo;

import org.shboland.persistence.db.hibernate.bean.Person;
import org.shboland.persistence.criteria.PersonSearchCriteria;

import java.util.List;

public interface PersonRepositoryCustom {

    int findNumberOfPersonBySearchCriteria(PersonSearchCriteria sc);

    List<Person> findBySearchCriteria(PersonSearchCriteria sc);
}
