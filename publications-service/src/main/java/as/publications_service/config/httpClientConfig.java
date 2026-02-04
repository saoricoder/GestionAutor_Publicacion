package as.publications_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Configuración del cliente HTTP para comunicación entre microservicios
 * RestTemplate, que actúa como el adaptador para realizar peticiones HTTP al servicio de autores.
 */
@Configuration
public class httpClientConfig {

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000); // 5 segundos
        factory.setReadTimeout(10000);   // 10 segundos
        
        return new RestTemplate(factory);
    }
}