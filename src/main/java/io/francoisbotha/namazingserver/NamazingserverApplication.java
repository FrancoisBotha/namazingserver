package io.francoisbotha.namazingserver;

import io.francoisbotha.namazingserver.config.DynamoDBConfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;

@Slf4j
@SpringBootApplication
@Configuration
@Import(DynamoDBConfig.class)
public class NamazingserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(NamazingserverApplication.class, args);
    }

}
