package com.gram.eureka.eureka_gram_user.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 1 Byte (B)
     * 1 Kilobyte (KB) = 1,024 Bytes
     * 1 Megabyte (MB) = 1,024 KB
     * 1 Gigabyte (GB) = 1,024 MB
     * 1 Terabyte (TB) = 1,024 GB
     * 1 Petabyte (PB) = 1,024 TB
     * 1 Exabyte (EB) = 1,024 PB
     */

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof FileSizeLimitExceededException) {
            return ResponseEntity
                    .status(HttpStatus.PAYLOAD_TOO_LARGE)
                    .body("개별 파일 크기는 5MB 이하만 업로드할 수 있습니다.");
        } else if (cause instanceof SizeLimitExceededException) {
            return ResponseEntity
                    .status(HttpStatus.PAYLOAD_TOO_LARGE)
                    .body("요청 전체 크기가 너무 큽니다. (예: 파일 3개 합쳐서 15MB 초과)");
        } else {
            return ResponseEntity
                    .status(HttpStatus.PAYLOAD_TOO_LARGE)
                    .body("파일 업로드 크기 제한을 초과했습니다.");
        }
    }
}
