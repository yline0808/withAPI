package net.ddns.yline.withAPI.service.contractFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ddns.yline.withAPI.domain.contract.Contract;
import net.ddns.yline.withAPI.domain.contractFile.ContractFile;
import net.ddns.yline.withAPI.domain.file.FileStatus;
import net.ddns.yline.withAPI.domain.file.UploadedFile;
import net.ddns.yline.withAPI.repository.contractFile.ContractFileRepository;
import net.ddns.yline.withAPI.service.contract.ContractService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ContractFileService {
    @Value("${file.upload-dir}")
    private String UPLOAD_DIR;

    private final ContractFileRepository contractFileRepository;
    private final ContractService contractService;

    @Transactional
    public void storeFile(Long contractId, List<MultipartFile> files) {
        //해당하는 계약서 불러오기
        Contract findContract = contractService.findById(contractId);
        List<ContractFile> contractFileList = new ArrayList<>();

        for(MultipartFile file: files) {
            try {
                String originalName = StringUtils.cleanPath(file.getOriginalFilename());
                String uniqueName = generateUniqueFileName(originalName);

                Path copyLocation = Paths.get(UPLOAD_DIR + File.separator + uniqueName);
                Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

                var uploadedFile = UploadedFile.builder()
                        .originalName(file.getOriginalFilename())
                        .uniqueName(uniqueName)
                        .type(file.getContentType())
                        .size(file.getSize())
                        .path(copyLocation.toString())
                        .fileStatus(FileStatus.SAVED)
                        .build();

                var contractFile = ContractFile.builder()
                        .uploadedFile(uploadedFile)
                        .build();

                //연관관계 설정
                contractFile.setContract(findContract);

                contractFileList.add(contractFile);
            } catch (IOException ex) {
                throw new IllegalArgumentException("파일 업로드에 실패했습니다.", ex);
            }
        }

        contractFileRepository.saveAll(contractFileList);
    }

    //고유한 파일 이름 만들기
    private String generateUniqueFileName(String originalName) {
        return UUID.randomUUID().toString() + "_" + originalName;
    }
}
