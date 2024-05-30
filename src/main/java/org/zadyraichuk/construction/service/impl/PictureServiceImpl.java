package org.zadyraichuk.construction.service.impl;

import org.springframework.stereotype.Service;
import org.zadyraichuk.construction.service.PictureService;
import org.zadyraichuk.construction.service.external.AmazonS3Service;

import java.io.*;
import java.net.URL;
import java.util.List;

@Service
public class PictureServiceImpl implements PictureService {

    private static final String USERS_BUCKET = "buildico-aws-user";
    private static final String COMPANY_BUCKET = "buildico-aws-company";
    private static final String PROJECT_BUCKET = "buildico-aws-project";

    private static final File DEFAULT_LOGO = new File("src/main/webapp/resources/img/default_logo.jpg");
    private static final File DEFAULT_PROJECT = new File("src/main/webapp/resources/img/default_project.jpg");

    //TODO add different extensions support
    private static final String DEFAULT_EXTENSION = "jpg";
    private static final String DEFAULT_NAME = "default";

    private boolean isDefaultUserUploaded = false;
    private boolean isDefaultProjectUploaded = false;
    private boolean isDefaultCompanyUploaded = false;

    private AmazonS3Service ams;

    public PictureServiceImpl(AmazonS3Service ams) {
        this.ams = ams;
    }

    @Override
    public boolean uploadUserPicture(String fileName, File userPicture) {
        return ams.uploadFile(USERS_BUCKET, fileName, userPicture);
    }

    @Override
    public boolean uploadProjectPicture(String dirName, File projectPicture) {
        return ams.uploadFile(PROJECT_BUCKET, dirName, "main", projectPicture);
    }

    @Override
    public boolean uploadProjectGallery(String dirName, List<File> pictures) {
        String directory = dirName + "/gallery";
        boolean success = true;

        for (int i = 0; i < pictures.size(); i++) {
            success = success && ams.uploadFile(PROJECT_BUCKET, directory, "picture" + i, pictures.get(i));
        }

        return success;
    }

    @Override
    public boolean uploadProjectSchemes(String dirName, List<File> schemes) {
        String directory = dirName + "/schemes";
        boolean success = true;

        for (int i = 0; i < schemes.size(); i++) {
            success = success && ams.uploadFile(PROJECT_BUCKET, directory, "scheme" + i, schemes.get(i));
        }

        return success;
    }

    @Override
    public boolean uploadCompanyPicture(String fileName, File companyPicture) {
        return ams.uploadFile(COMPANY_BUCKET, fileName, companyPicture);
    }

    @Override
    public URL getUserPictureURL(String fileName) {
        return ams.getFileURL(USERS_BUCKET, fileName, DEFAULT_EXTENSION);
    }

    @Override
    public URL getProjectPictureURL(String dirName) {
        return ams.getFileURL(PROJECT_BUCKET, dirName, "main", DEFAULT_EXTENSION);
    }

    @Override
    public List<URL> getProjectGallery(String dirName) {
        String directory = dirName + "gallery/";
        return ams.getFileURLs(PROJECT_BUCKET, directory);
    }

    @Override
    public List<URL> getProjectSchemes(String dirName) {
        String directory = dirName + "schemes/";
        return ams.getFileURLs(PROJECT_BUCKET, directory);
    }

    @Override
    public URL getCompanyPictureURL(String fileName) {
        return ams.getFileURL(COMPANY_BUCKET, fileName, DEFAULT_EXTENSION);
    }

    @Override
    public void removeUserPicture(String fileName) {
        ams.removeFile(USERS_BUCKET, fileName, DEFAULT_EXTENSION);
    }

    @Override
    public void removeCompanyPicture(String fileName) {
        ams.removeFile(COMPANY_BUCKET, fileName, DEFAULT_EXTENSION);
    }

    @Override
    public void removeProjectPictures(String dirName) {
        ams.removeDirectory(PROJECT_BUCKET, dirName + "gallery/");
        ams.removeDirectory(PROJECT_BUCKET, dirName + "schemes/");
        ams.removeDirectory(PROJECT_BUCKET, dirName);
    }

    public URL getDefaultUserPicture() {
        if (!isDefaultUserUploaded && !ams.isFileExists(USERS_BUCKET, DEFAULT_NAME, DEFAULT_EXTENSION)) {
            ams.uploadFile(USERS_BUCKET, DEFAULT_NAME, DEFAULT_LOGO);
            isDefaultUserUploaded = true;
        }
        return ams.getFileURL(USERS_BUCKET, DEFAULT_NAME, DEFAULT_EXTENSION);
    }

    public URL getDefaultProjectPicture() {
        if (!isDefaultProjectUploaded && !ams.isFileExists(PROJECT_BUCKET, DEFAULT_NAME, DEFAULT_EXTENSION)) {
            ams.uploadFile(PROJECT_BUCKET, DEFAULT_NAME, DEFAULT_PROJECT);
            isDefaultProjectUploaded = true;
        }
        return ams.getFileURL(PROJECT_BUCKET, DEFAULT_NAME, DEFAULT_EXTENSION);
    }

    public URL getDefaultCompanyPicture() {
        if (!isDefaultCompanyUploaded && !ams.isFileExists(COMPANY_BUCKET, DEFAULT_NAME, DEFAULT_EXTENSION)) {
            ams.uploadFile(COMPANY_BUCKET, DEFAULT_NAME, DEFAULT_LOGO);
            isDefaultCompanyUploaded = true;
        }
        return ams.getFileURL(COMPANY_BUCKET, DEFAULT_NAME, DEFAULT_EXTENSION);
    }
}
