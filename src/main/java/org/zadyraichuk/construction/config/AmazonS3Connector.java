package org.zadyraichuk.construction.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;

public class AmazonS3Connector {

    private static AmazonS3Connector instance;

    @Value("${aws.access.key}")
    private static String awsAccessKey;

    @Value("${aws.secret.key}")
    private static String awsSecretKey;

    private static final Regions REGION = Regions.EU_CENTRAL_1;

    private final AmazonS3 s3;

    private AmazonS3Connector() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
        s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(REGION)
                .build();
    }

    public static AmazonS3Connector getInstance() {
        if (instance == null) {
            instance = new AmazonS3Connector();
        }
        return instance;
    }

    public AmazonS3 getClient() {
        return s3;
    }
}
