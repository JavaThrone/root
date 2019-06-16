package it.discovery.order;

import it.discovery.balancer.RetryConfiguration;
import it.discovery.balancer.api.LoadBalancer;
import it.discovery.balancer.client.RetryableRestTemplate;
import it.discovery.order.client.BookClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableJpaRepositories("it.discovery.order.repository")
@EnableScheduling
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

    @Bean
    public BookClient bookClient(RestTemplate restTemplate,
                                 LoadBalancer loadBalancer) {
        return new BookClient(restTemplate, loadBalancer);
    }

    @Bean
    @Primary
    @Qualifier("retry")
    public RestTemplate restTemplate(RetryConfiguration retryConfiguration) {
        return new RetryableRestTemplate(retryConfiguration);
    }

    @Bean
    @Qualifier("no-retry")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
