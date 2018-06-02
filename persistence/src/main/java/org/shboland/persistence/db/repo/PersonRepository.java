package org.shboland.persistence.db.repo;

import org.shboland.persistence.db.hibernate.bean.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long>, PersonRepositoryCustom {
}