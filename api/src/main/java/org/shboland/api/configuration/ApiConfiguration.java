package org.shboland.api.configuration;

import org.shboland.core.configuration.CoreConfiguration;
import org.shboland.domain.configuration.DomainConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({CoreConfiguration.class, DomainConfiguration.class})
public class ApiConfiguration {
}
