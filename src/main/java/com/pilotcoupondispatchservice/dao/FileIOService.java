package com.pilotcoupondispatchservice.dao;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by IntelliJ IDEA.
 * User: Md. Shamim
 * Date: ২৫/১১/২২
 * Time: ১০:৫০ PM
 * Email: mdshamim723@gmail.com
 */
public interface FileIOService {

    public String uploadImageIntoStorage(MultipartFile file, String folderName, String fileName);

    public Resource findImageFromStorageAsResource(String fileName);

    public InputStreamResource findImageFromStorageAsInputStreamResource(String fileName);

    public byte[] findImageFromStorageAsBytes(String fileName);

}
