package com.lion.pinepeople.controller;

import com.lion.pinepeople.domain.response.Response;
import com.lion.pinepeople.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "/pinepeople/upload")
@RequiredArgsConstructor
@Slf4j
public class FileUploadController {
    private final FileUploadService fileUploadService;

//    @PostMapping
//    public Response<Void> uploadFile(@RequestPart(value = "file") MultipartFile file) throws IOException{
//        log.info("fileUPloadstart");
//        String url = fileUploadService.uploadFile(file);
//        log.info("fileUPload");
//        return Response.success(url);
//    }
}
