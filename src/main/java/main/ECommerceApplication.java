package main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import services.IMyService;
import services.MyService;

@SpringBootApplication()
@EntityScan(basePackages = {"entities"})
@EnableJpaRepositories(basePackages = {"repositories"})
@ComponentScan(basePackages = {"controllers","services","configurations"})
//@CacheConfig(cacheNames={"products"})
public class ECommerceApplication {
    public static void main(String[] args) {
       ConfigurableApplicationContext context=SpringApplication.run(ECommerceApplication.class, args);
       MyService myService= (MyService) context.getBean("blabla");
       MyService myService2= (MyService) context.getBean(IMyService.class);
       myService2.consume();
       // DataSource ds=context.getBean(DataSource.class);

    }

    @Bean(name="blabla")
    IMyService myService()
    {
        return new MyService();
    }
}
