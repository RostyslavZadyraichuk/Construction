package org.zadyraichuk.construction.service.external;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.google.common.io.Files;
import org.springframework.stereotype.Service;
import org.zadyraichuk.construction.config.external.AmazonS3Connector;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AmazonS3Service {

    private final AmazonS3 s3Client;

    public AmazonS3Service(AmazonS3Connector connector) {
        this.s3Client = connector.getClient();
    }

    public boolean isBucketExists(String bucketName) {
        return s3Client.doesBucketExistV2(bucketName.toLowerCase());
    }

    public boolean createBucket(String bucketName) {
        if (isBucketExists(bucketName)) {
            return false;
        }
        s3Client.createBucket(bucketName.toLowerCase());
        return true;
    }

    public void removeBucket(String bucketName) {
        if (isBucketExists(bucketName)) {
            s3Client.deleteBucket(bucketName.toLowerCase());
        }
    }

    public boolean isFileExists(String bucketName, String fileName) {
        if (isBucketExists(bucketName)) {
            return s3Client.doesObjectExist(bucketName, fileName);
        }
        return false;
    }

    public boolean isFileExists(String bucketName, File file) {
        return isFileExists(bucketName, file.getName());
    }

    public boolean uploadFile(String bucketName, File file) {
        if (isFileExists(bucketName, file)) {
            return false;
        }

        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis)) {

            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(bis.available());
            meta.setContentType("image/" + Files.getFileExtension(file.getName()));

            s3Client.putObject(
                    new PutObjectRequest(bucketName.toLowerCase(), file.getName(), bis, meta)
            );
//                        .withCannedAcl(CannedAccessControlList.Private));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public File loadFile(String bucketName, String fileName) {
        if (isFileExists(bucketName, fileName)) {
            S3Object result = s3Client.getObject(
                    new GetObjectRequest(bucketName.toLowerCase(), fileName)
            );
//            String contentType = result.getObjectMetadata().getContentType();

//            int slashIndex = contentType.indexOf('/');
//            String extension = '.' + contentType.substring(slashIndex);
//            File file = new File(fileName + extension);
            File file = new File(fileName);
            byte[] resultData = null;
            try {
                resultData = result.getObjectContent().readAllBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (resultData == null)
                return null;

            try (ByteArrayInputStream bais = new ByteArrayInputStream(resultData);
                 FileOutputStream fos = new FileOutputStream(file);
                 BufferedOutputStream bos = new BufferedOutputStream(fos)) {
                byte[] buffer = new byte[4096];
                int nRead;
                while (bais.available() > 0) {
                    nRead = bais.read(buffer);
                    bos.write(buffer, 0 ,nRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return file;
        }
        return null;
    }

    public void removeFile(String bucketName, File file) {
        removeFile(bucketName, file.getName());
    }

    public void removeFile(String bucketName, String fileName) {
        if (isFileExists(bucketName, fileName)) {
            s3Client.deleteObject(bucketName, fileName);
        }
    }

    public List<String> getExistedFiles(String bucketName) {
        if (isBucketExists(bucketName)) {
            List<S3ObjectSummary> summaries = s3Client.listObjectsV2(bucketName.toLowerCase())
                    .getObjectSummaries();
            return summaries.stream().map(S3ObjectSummary::getKey).collect(Collectors.toList());
        }
        return List.of();
    }

//    public void removeAllFiles(String bucketName) {
//        if (isBucketExists(bucketName.toLowerCase())) {
//            List<String> files = getExistedFiles(bucketName.toLowerCase());
//            s3Client.deleteObjects(new DeleteObjectsRequest(bucketName.toLowerCase()));
//        }
//    }

}
