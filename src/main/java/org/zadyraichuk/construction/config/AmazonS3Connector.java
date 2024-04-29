package org.zadyraichuk.construction.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonS3Connector {

    private static final Regions REGION = Regions.EU_CENTRAL_1;

    private final AmazonS3 s3;

    public AmazonS3Connector(@Value("${aws.access.key}") String awsAccessKey,
                             @Value("${aws.secret.key}") String awsSecretKey) {

        BasicAWSCredentials credentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
        s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(REGION)
                .build();

    }

    public AmazonS3 getClient() {
        return s3;
    }
}
