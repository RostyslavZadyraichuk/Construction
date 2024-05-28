package org.zadyraichuk.construction.service;

import java.io.File;
import java.net.URL;
import java.util.List;

public interface PictureService {

    boolean uploadUserPicture(String fileName, File userPicture);

    boolean uploadProjectPicture(String dirName, File projectPicture);

    boolean uploadProjectGallery(String dirName, List<File> pictures);

    boolean uploadProjectSchemes(String dirName, List<File> schemes);

    boolean uploadCompanyPicture(String fileName, File companyPicture);

    URL getUserPictureURL(String fileName);

    URL getProjectPictureURL(String dirName);

    List<URL> getProjectGallery(String dirName);

    List<URL> getProjectSchemes(String dirName);

    URL getCompanyPictureURL(String fileName);

    URL getDefaultUserPicture();

    URL getDefaultProjectPicture();

    URL getDefaultCompanyPicture();

    void removeUserPicture(String fileName);

    void removeCompanyPicture(String fileName);

    void removeProjectPictures(String dirName);

}
