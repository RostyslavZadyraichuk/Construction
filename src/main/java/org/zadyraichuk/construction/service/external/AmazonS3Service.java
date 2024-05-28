package org.zadyraichuk.construction.service.external;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.google.common.io.Files;
import org.springframework.stereotype.Service;
import org.zadyraichuk.construction.config.external.AmazonS3Connector;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AmazonS3Service {

    private static final StringBuilder SB = new StringBuilder();

    private final AmazonS3 s3Client;

    public AmazonS3Service(AmazonS3Connector connector) {
        this.s3Client = connector.getClient();
    }

    public boolean isBucketExists(String bucketName) {
        return s3Client.doesBucketExistV2(bucketName);
    }

    public boolean createBucket(String bucketName) {
        if (isBucketExists(bucketName)) {
            return false;
        }
        s3Client.createBucket(bucketName);
        return true;
    }

    public void removeBucket(String bucketName) {
        if (isBucketExists(bucketName)) {
            s3Client.deleteBucket(bucketName);
        }
    }

    public boolean isFileExists(String bucketName, String fileName, String extension) {
        return isFileExists(bucketName, null, fileName, extension);
    }

    public boolean isFileExists(String bucketName, String directory, String fileName, String extension) {
        if (isBucketExists(bucketName)) {
            return s3Client.doesObjectExist(bucketName, toFilePath(directory, fileName, extension));
        }
        return false;
    }

    public boolean uploadFile(String bucketName, String fileName, File file) {
        return uploadFile(bucketName, null, fileName, file);
    }

    public boolean uploadFile(String bucketName, String directory, String fileName, File file) {
        if (!isBucketExists(bucketName)) {
            createBucket(bucketName);
        }

        String extension = Files.getFileExtension(file.getName());
        if (isFileExists(bucketName, directory, fileName, extension)) {
            return false;
        }

        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis)) {

            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(bis.available());
            meta.setContentType("image/" + extension);

            s3Client.putObject(new PutObjectRequest(bucketName,
                            toFilePath(directory, fileName, extension), bis, meta));

        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public URL getFileURL(String bucketName, String fileName, String extension) {
        return getFileURL(bucketName, null, fileName, extension);
    }

    public URL getFileURL(String bucketName, String directory, String fileName, String extension) {
        if (isFileExists(bucketName, fileName, extension)) {
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName,
                    toFilePath(directory, fileName, extension));
            return s3Client.generatePresignedUrl(request);
        }
        return null;
    }

    public List<URL> getFileURLs(String bucketName) {
        return getFileURLs(bucketName, "");
    }

    public List<URL> getFileURLs(String bucketName, String directory) {
        if (isBucketExists(bucketName)) {
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, "");
            List<S3ObjectSummary> summaries = s3Client.listObjectsV2(bucketName)
                    .getObjectSummaries();
            return summaries.stream().map(e -> {
                String fileName = e.getKey();
                request.setKey(directory + fileName);
                return s3Client.generatePresignedUrl(request);
            }).collect(Collectors.toList());
        }
        return List.of();
    }

    //TODO make recursively
    public void removeDirectory(String bucketName, String directory) {
        clearDirectory(bucketName, directory);
        removeFile(bucketName, null, directory, null);
    }

    public void removeFile(String bucketName, String fileName, String extension) {
        removeFile(bucketName, null, fileName, extension);
    }

    public void removeFile(String bucketName, String directory, String fileName, String extension) {
        if (isFileExists(bucketName, directory, fileName, extension)) {
            s3Client.deleteObject(bucketName, toFilePath(directory, fileName, extension));
        }
    }

    public void clearDirectory(String bucketName, String directory) {
        if (isBucketExists(bucketName)) {
            ListObjectsRequest request = new ListObjectsRequest().withBucketName(bucketName)
                    .withPrefix(directory);
            ObjectListing objectListing = s3Client.listObjects(request);

            for (S3ObjectSummary summary : objectListing.getObjectSummaries()) {
                s3Client.deleteObject(bucketName, summary.getKey());
            }
        }
    }

    private String toFilePath(String directory, String fileName, String extension) {
        SB.setLength(0);
        if (directory != null)
            SB.append(directory);
        SB.append(fileName);
        if (extension != null)
            SB.append('.').append(extension);
        return SB.toString();
    }

}
