package br.com.efb;


import com.efb.api.ContaApi;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@ConfigurationPropertiesScan
public class BackEndApplicaton extends SpringBootServletInitializer {

    Logger log = LoggerFactory.getLogger(BackEndApplicaton.class);
    public static void main(String[] args) {
        SpringApplication.run(BackEndApplicaton.class,args);
    }

    @Bean
    public CommandLineRunner startComand(){
        return args -> {
            log.info("Start Back-End");
        };
    }
    @Value("${application.Name}")
    private String applicationName;

}
