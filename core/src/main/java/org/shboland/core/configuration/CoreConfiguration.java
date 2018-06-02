package org.shboland.core.configuration;

import org.shboland.persistence.configuration.PersistenceConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({PersistenceConfiguration.class})
@ComponentScan(basePackages = { "org.shboland.core.service" })
public class CoreConfiguration {
}
