package com.camping_rental.server.domain.file.api;

import com.camping_rental.server.annotation.RequiredLogin;
import com.camping_rental.server.domain.exception.dto.AccessDeniedPermissionException;
import com.camping_rental.server.domain.file.service.FileUploadService;
import com.camping_rental.server.domain.message.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v2/file")
@RequiredArgsConstructor
public class FileApiV2 {
    private final FileUploadService fileUploadService;


    /**
     * <b>API URL : /api/v2/file/image/s3</b>
     *
     * @param files
     * @return
     */
    @PostMapping(value = "/image/s3")
    @RequiredLogin
    public ResponseEntity<?> postMethodName(@RequestParam("files") List<MultipartFile> files) {
        Message message = new Message();

        try {
            message.setStatus(HttpStatus.OK);
            message.setMessage("success");
            message.setData(fileUploadService.uploadImagesToS3(files));
        } catch (IllegalArgumentException e) {
            message.setStatus(HttpStatus.BAD_REQUEST);
            message.setMessage(e.getMessage());
            message.setMemo("업로드에 실패 하였습니다.");
        }

        return new ResponseEntity<>(message, message.getStatus());
    }
}
