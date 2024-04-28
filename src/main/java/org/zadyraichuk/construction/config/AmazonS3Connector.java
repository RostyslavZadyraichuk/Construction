package org.zadyraichuk.construction.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AmazonS3Connector {

    private AmazonS3Connector() {}

    @Value("${aws.access.key}")
    private static String awsAccessKey;

    @Value("${aws.secret.key}")
    private static String awsSecretKey;

    private static final Regions REGION = Regions.EU_CENTRAL_1;

    @Bean
    public static AmazonS3 createPersonalClient() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(REGION)
                .build();
    }
}
