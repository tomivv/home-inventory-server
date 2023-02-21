package com.github.tomivv.homeinventoryserver.config;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

@Configuration
@EnableDynamoDBRepositories
public class AppConfig {
    
    @Bean
    public AmazonDynamoDB amazonDynamoDB(AWSCredentials awsCredentials, @Value("${aws.dynamoDBUrl}") String DynamoDBURL) {
        AmazonDynamoDBClientBuilder builder = AmazonDynamoDBClientBuilder.standard()
                                                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(DynamoDBURL, DynamoDBURL))
                                                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials));
        AmazonDynamoDB client = builder.build();
        return client;
    }

    @Bean
    public AWSCredentials awsCredentials(@Value("${aws.accessKey}") String accesskey, @Value("${aws.secretKey}") String secretkey) {
        return new BasicAWSCredentials(accesskey, secretkey);
    }
}
