package com.pilotcoupondispatchservice.dao;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileIOService {

    public String uploadImageIntoStorage(MultipartFile file, String folderName, String fileName);

    public Resource findImageFromStorageAsResource(String fileName);

    public InputStreamResource findImageFromStorageAsInputStreamResource(String fileName);

    public byte[] findImageFromStorageAsBytes(String fileName);

}
