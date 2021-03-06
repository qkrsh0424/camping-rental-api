package com.camping_rental.server.domain.file.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Getter
@Builder
@ToString
public class FileS3GetDto {
    private UUID id;
    private String fileOriginName;
    private String fileName;
    private String fileStorageUri;
    private String fileFullUri;
    private String serviceUrl;
    private String filePath;
    private String fileExtension;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime madeAt;
    private Long size;
}
