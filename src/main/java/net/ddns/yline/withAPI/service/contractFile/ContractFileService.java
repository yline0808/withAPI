package net.ddns.yline.withAPI.service.contractFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ddns.yline.withAPI.domain.contractFile.ContractFile;
import net.ddns.yline.withAPI.domain.contractFile.FileStatus;
import net.ddns.yline.withAPI.repository.contractFile.ContractFileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ContractFileService {
    @Value("${file.upload-dir}")
    private String UPLOAD_DIR;

    private final ContractFileRepository contractFileRepository;

    @Transactional
    public void storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            Path copyLocation = Paths.get(UPLOAD_DIR + File.separator + fileName);
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/downloadFile/")
                    .path(fileName)
                    .toUriString();

            var contractFile = ContractFile.builder()
                    .originalName(file.getOriginalFilename())
                    .saveName(fileName)
                    .type(file.getContentType())
                    .size(file.getSize())
                    .fileStatus(FileStatus.SAVED)
                    .build();

            contractFileRepository.save(contractFile);
        } catch (IOException ex) {
            throw new IllegalArgumentException("파일 업로드에 실패했습니다.", ex);
        }
    }
}
