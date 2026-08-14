package com.lcwd.electronicStore.service.impl;

import com.lcwd.electronicStore.exception.BadApiRequestException;
import com.lcwd.electronicStore.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceimpl implements FileService {

    private Logger logger= LoggerFactory.getLogger(FileServiceimpl.class);

    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {

        String originalFilename = file.getOriginalFilename();
        logger.info("filename {}:",originalFilename);
        String fileName= UUID.randomUUID().toString();
        String extention=originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileNameWithextention=fileName+extention;
        String fullPathWithfileName=path+fileNameWithextention;

        logger.info("fullpathwithfilename : {} ",fullPathWithfileName);
        if(extention.equalsIgnoreCase(".png")|| extention.equalsIgnoreCase(".jpg")|| extention.equalsIgnoreCase(".jpeg")){

            File folder=new File(path);

            if(!folder.exists()){
                folder.mkdirs();
            }

            Files.copy(file.getInputStream(), Paths.get(fullPathWithfileName));
            return fileNameWithextention;

        }
        else{
            throw new BadApiRequestException("filename with this" + extention+"is not allowed!!");
        }


    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {

        String fullpath=path+File.separator+name;
        InputStream inputStream=new FileInputStream(fullpath);
        return inputStream;



    }
}
