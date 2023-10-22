package net.ddns.yline.withAPI.controller.contractFile;

import lombok.RequiredArgsConstructor;
import net.ddns.yline.withAPI.controller.SuccessResult;
import net.ddns.yline.withAPI.service.contractFile.ContractFileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/contractFile")
public class ContractFileController {
    private final ContractFileService contractFileService;

    @PostMapping("/uploadFile")
    public ResponseEntity<SuccessResult> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 없습니다.");
        }



        contractFileService.storeFile(file);

        return ResponseEntity.ok(new SuccessResult("Success upload file", "파일 업로드 성공"));
    }
}
