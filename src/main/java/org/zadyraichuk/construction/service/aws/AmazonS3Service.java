package org.zadyraichuk.construction.service.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.google.common.io.Files;
import org.springframework.stereotype.Service;
import org.zadyraichuk.construction.config.AmazonS3Connector;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AmazonS3Service {

    private final AmazonS3 s3Client;

    public AmazonS3Service() {
        this.s3Client = AmazonS3Connector.getInstance().getClient();
    }

    public boolean createBucket(String bucketName) {
        if (s3Client.doesBucketExistV2(bucketName)) {
            return false;
        }
        s3Client.createBucket(bucketName);
        return true;
    }

    public void removeBucket(String bucketName) {
        if (s3Client.doesBucketExistV2(bucketName)) {
            s3Client.deleteBucket(bucketName);
        }
    }

    public boolean uploadFile(String bucketName, File file) {
        if (s3Client.doesBucketExistV2(bucketName)) {
            try (FileInputStream fis = new FileInputStream(file);
                 BufferedInputStream bis = new BufferedInputStream(fis)) {

                ObjectMetadata meta = new ObjectMetadata();
                meta.setContentLength(bis.available());
                meta.setContentType("image/" + Files.getFileExtension(file.getName()));

                s3Client.putObject(new PutObjectRequest(bucketName, file.getName(), bis, meta));
//                        .withCannedAcl(CannedAccessControlList.Private));
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public File loadFile(String bucketName, String fileName) {
        if (s3Client.doesBucketExistV2(bucketName)) {
            S3Object result = s3Client.getObject(new GetObjectRequest(bucketName, fileName));
            String contentType = result.getObjectMetadata().getContentType();

            int slashIndex = contentType.indexOf('/');
            String extension = '.' + contentType.substring(slashIndex);
            File file = new File(fileName + extension);

            try (BufferedInputStream bis = new BufferedInputStream(result.getObjectContent());
                 FileOutputStream fos = new FileOutputStream(file);
                 BufferedOutputStream bos = new BufferedOutputStream(fos)) {
                byte[] buffer = new byte[4096];
                int nRead;
                while (bis.available() > 0) {
                    nRead = bis.read(buffer);
                    bos.write(buffer, 0 ,nRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return file;
        }
        return null;
    }

    public List<String> getExistedFiles(String bucketName) {
        if (s3Client.doesBucketExistV2(bucketName)) {
            List<S3ObjectSummary> summaries = s3Client.listObjectsV2(bucketName).getObjectSummaries();
            return summaries.stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
        }
        return List.of();
    }
}
