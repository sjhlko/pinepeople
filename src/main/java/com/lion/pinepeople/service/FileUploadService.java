package com.lion.pinepeople.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadService {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private String defaultUrl = "https://pinepeople-t3-bucket.s3.ap-northeast-2.amazonaws.com";

    public String uploadFile(MultipartFile file, String dir) throws IOException {
        try{
            dir = "/" + dir;
            String bucketDir = bucketName + dir;
            String dirUrl = defaultUrl + dir + "/";
            String fileName = generateFileName(file);

            amazonS3Client.putObject(bucketDir, fileName, file.getInputStream(), getObjectMetadata(file));
            return dirUrl + fileName;
        } catch (SdkClientException e){
            throw new IOException("Error uploading file to S3", e);
        }
    }

    private ObjectMetadata getObjectMetadata(MultipartFile file){
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        return objectMetadata;
    }

    private String generateFileName(MultipartFile file){
        return UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
    }
}
