package br.com.efb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BackEndConfiguration {

    @Bean(name = "applicationName")
    public String applicationName(){
        return "Back-End";
    }
}
