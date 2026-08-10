package com.pilotcoupondispatchservice.dao;

import com.pilotcoupondispatchservice.exceptions.FileStorageException;
import com.pilotcoupondispatchservice.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.util.StringUtils;

/**
 * Created by IntelliJ IDEA.
 * User: Md. Shamim
 * Date: ২৫/১১/২২
 * Time: ১০:৫০ PM
 * Email: mdshamim723@gmail.com
 */

@Slf4j
@Service
public class FileIOServiceImpl implements FileIOService {

    @Value("${file.image-upload-path:static/uploads}")
    public String IMAGE_UPLOAD_PATH;

    private final ResourceLoader resourceLoader;

    public FileIOServiceImpl(@Qualifier("webApplicationContext") ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public String uploadImageIntoStorage(MultipartFile file, String folderName, String fileName) {
        String path = String.format("%s/%s", IMAGE_UPLOAD_PATH, folderName);
        return fileUploadIntoStorage(file, path, fileName);
    }

    @Override
    public Resource findImageFromStorageAsResource(String fileName) {
        return loadFileFromStorageAsResource(fileName);
    }

    @Override
    public InputStreamResource findImageFromStorageAsInputStreamResource(String fileName) {

        Resource resource = loadFileFromStorageAsResource(fileName);

        try {
            return new InputStreamResource(resource.getInputStream());
        } catch (IOException e) {
            throw new FileStorageException("Can not convert into input stream.");
        }

    }

    @Override
    public byte[] findImageFromStorageAsBytes(String fileName) {
        return loadFileFromStorageAsByte(fileName);
    }

    /* ============ Core ============== */

    private String fileUploadIntoStorage(MultipartFile file, String filePath, String fileName) {

        File directory = new File(filePath);

        if (directory.exists()) {
            log.info("Directory already exists.");
        } else {

            boolean mkdirs = directory.mkdirs();

            if (mkdirs) {
                log.info("Directory created.");
            } else {
                log.error("Directory can't be created.");
            }

        }

        if (file.isEmpty()) {
            throw new FileStorageException("File is missing.");
        }

//        if(file.getOriginalFilename().contains("..")) {
//            throw new FileStorageException("Sorry! Filename contains invalid path sequence " + file.getOriginalFilename());
//        }

        String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String savedFileName = String.format("%s.%s", fileName, fileExtension);

        try {
            Files.copy(
                file.getInputStream(),
                Paths.get(directory.getPath(), savedFileName),
                StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new FileStorageException("Failed to upload file.");
        }

        return String.format("/%s/%s", filePath, savedFileName);
    }

    // Filename along with full path
    private Resource loadFileFromStorageAsResource(String fileName) {

        Resource resource = resourceLoader.getResource("file:" + fileName);

        if (!resource.exists()) {
            throw new ResourceNotFoundException("File", "name", fileName);
        }

        return resource;
    }

    // Filename along with full path
    private byte[] loadFileFromStorageAsByte(String fileName) {

        File file = new File(fileName);

        if (!file.exists()) {
            throw new ResourceNotFoundException("File", "name", fileName);
        }

        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new ResourceNotFoundException("File", "name", fileName);
        }

    }

}
