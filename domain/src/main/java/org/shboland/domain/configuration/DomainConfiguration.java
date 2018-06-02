package org.shboland.domain.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "org.shboland.domain.entities" })
public class DomainConfiguration {
}
