package config;

import org.springframework.context.annotation.*;

@Configuration
@Import({RestTemplateConfig.class})
@EnableAspectJAutoProxy
@ComponentScan({"service", "testData", "org.pet"})
public class BackendMicroservicesApiConfig {

}
