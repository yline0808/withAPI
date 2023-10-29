package net.ddns.yline.withAPI.controller.contract;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.ddns.yline.withAPI.controller.SuccessResult;
import net.ddns.yline.withAPI.domain.account.Account;
import net.ddns.yline.withAPI.domain.contract.Contract;
import net.ddns.yline.withAPI.domain.contract.ContractStatus;
import net.ddns.yline.withAPI.domain.contractFile.ContractFile;
import net.ddns.yline.withAPI.service.contractFile.ContractFileService;
import net.ddns.yline.withAPI.service.contractMap.ContractMapService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/contract")
public class ContractController {

    private final ContractMapService contractMapService;

    @GetMapping
    public ResponseEntity<SearchContractResponse> getContractList(
            @RequestBody @Valid SearchContractRequest request
    ) {

        return ResponseEntity.ok(new SearchContractResponse());
    }

    @PostMapping
    public ResponseEntity<CreateContractResponse> saveContract(
            @RequestBody @Valid CreateContractRequest request,
            Principal principal) {
        //===계약서 생성===
        var contract = Contract.builder()
                .title(request.title)
                .content(request.content)
                .contractStatus(ContractStatus.INVALID)
                .build();

        //계약서 메핑 인원
        List<String> receivers = request.getReceivers();
        receivers.add(principal.getName());

        //인원별 계약서 저장
        Long savedContractId = contractMapService.saveAccountMapping(contract, receivers);

        return ResponseEntity.ok(new CreateContractResponse(savedContractId));
    }

    @Data
    static class CreateContractResponse{
        private Long id;

        public CreateContractResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    static class CreateContractRequest{
        @NotBlank(message = "제목은 필수값 입니다.")
        private String title;
        @NotBlank(message = "내용은 필수값 입니다.")
        private String content;
        private List<String> receivers = new ArrayList<>();
    }

    @Data
    static class SearchContractRequest{
        private Long searchId;
        private String searchTitle;
        private String searchContent;
    }

    @Data
    static class SearchContractResponse{
        private List<ContractDto> contractList = new ArrayList<>();
        private Long page;
        private Long count;
        private Long total;
    }

    @Getter
    static class ContractDto{
        private Long id;
        private String title;
        private String content;
        private String status;
        private LocalDateTime createdAt;
        private List<ContractFileDto> contractFileList;
        private List<AccountDto> accountList;

        public ContractDto(Contract contract) {
            this.id = contract.getId();
            this.title = contract.getTitle();
            this.content = contract.getContent();
            this.status = contract.getContractStatus().toString();
            this.createdAt = contract.getCreatedDate();
            this.contractFileList = contract.getContractFileList().stream().map(ContractFileDto::new)
                    .collect(Collectors.toList());
            this.accountList = contract.getContractMapList().stream()
                    .map(contractMap -> new AccountDto(contractMap.getAccount()))
                    .collect(Collectors.toList());
        }
    }

    @Getter
    static class ContractFileDto{
        private Long id;
        private String originalName;
        private Long size;
        private String type;

        public ContractFileDto(ContractFile contractFile) {
            this.id = contractFile.getId();
            this.originalName = contractFile.getUploadedFile().getOriginalName();
            this.size = contractFile.getUploadedFile().getSize();
            this.type = contractFile.getUploadedFile().getType();
        }
    }

    @Getter
    static class AccountDto{
        private Long id;
        private String name;
        private String email;
        private String phone;

        public AccountDto(Account account) {
            this.id = account.getId();
            this.name = account.getName();
            this.email = account.getEmail();
            this.phone = account.getPhone();
        }
    }
}
