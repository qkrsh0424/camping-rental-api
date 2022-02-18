package com.camping_rental.server.domain.file.api;

import com.camping_rental.server.domain.exception.dto.AccessDeniedPermissionException;
import com.camping_rental.server.domain.message.dto.Message;
import com.camping_rental.server.domain.file.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@RestController
@RequestMapping("/api/v1/file")
public class FileApiController {
    private final FileUploadService fileUploadService;

    @Value("${app.admin.password}")
    private String ADMIN_PASSWORD;

    @Autowired
    public FileApiController(
            FileUploadService fileUploadService
    ) {
        this.fileUploadService = fileUploadService;
    }

    /**
     * <b>API URL : /api/v1/file/image/s3</b>
     *
     * @param files
     * @return
     */
    @PostMapping(value = "/image/s3")
    public ResponseEntity<?> postMethodName(@RequestParam("files") MultipartFile[] files, @RequestParam("password") String password) {
        Message message = new Message();

        if(!password.equals(ADMIN_PASSWORD)){
            throw new AccessDeniedPermissionException("접근 권한이 없습니다.");
        }

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
