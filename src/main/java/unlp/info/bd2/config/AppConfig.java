package unlp.info.bd2.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("unlp.info.bd2")
@EnableJpaRepositories("unlp.info.bd2.repositories")
public class AppConfig {
}
