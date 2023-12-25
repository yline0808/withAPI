package net.ddns.yline.withAPI.controller.contractFile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.ddns.yline.withAPI.controller.SuccessResult;
import net.ddns.yline.withAPI.service.contractFile.ContractFileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/contractFile")
public class ContractFileController {
    private final ContractFileService contractFileService;

    @PostMapping
    public ResponseEntity<SuccessResult> uploadFile(
            @RequestParam Long contractId,
            @RequestParam(required = false) List<MultipartFile> files) {
        if (files.isEmpty() || contractId == null) {
            throw new IllegalArgumentException("파일이 없습니다.");
        }
        contractFileService.storeFile(contractId, files);
        return ResponseEntity.ok(new SuccessResult("Success upload file", "파일 업로드 성공"));
    }
}
