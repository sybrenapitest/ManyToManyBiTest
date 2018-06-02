package org.shboland.persistence.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = { "org.shboland.persistence.db" })
@EntityScan(basePackages = { "org.shboland.persistence.db.hibernate.bean" })
@EnableJpaRepositories("org.shboland.persistence.db.repo")
public class PersistenceConfiguration {
}
