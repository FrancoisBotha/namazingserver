package io.francoisbotha.namazingserver.config;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableDynamoDBRepositories
        (basePackages = "io.francoisbotha.namazingserver.domain.dao")
public class DynamoDBConfig {

    //Comment out for PRODUCTION
    @Value("${amazon.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;

    @Value("${aws.s3.profile}")
    private String awsProfileName;

    @Value("${aws.s3.region}")
    private String region;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {

        //DEVELOPMENT
        AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration(amazonDynamoDBEndpoint, region))
                .build();

        //PRODUCTION
//        AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.standard()
//                .withCredentials(new ProfileCredentialsProvider(awsProfileName))
//                .build();

        return dynamoDB;

    }

}

