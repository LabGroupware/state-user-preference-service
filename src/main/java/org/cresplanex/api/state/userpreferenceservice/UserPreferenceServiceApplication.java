package org.cresplanex.api.state.userpreferenceserviceservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class UserPreferenceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(org.cresplanex.api.state.userpreferenceserviceservice.UserPreferenceServiceApplication.class, args);
    }

}
