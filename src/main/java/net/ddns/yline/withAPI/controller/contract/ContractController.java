package net.ddns.yline.withAPI.controller.contract;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.ddns.yline.withAPI.domain.contract.Contract;
import net.ddns.yline.withAPI.service.contract.ContractService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contract")
public class ContractController {

    private final ContractService contractService;

    @PostMapping
    public CreateContractResponse saveContract(@RequestBody @Valid CreateContractRequest request) {
        Contract contract = new Contract();
        contract.setTitle(request.getTitle());
        contract.setContent(request.getContent());
        contract.setFileName(request.getFileName());

        Long id = contractService.save(contract);
        return new CreateContractResponse(id);
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
        private String title;
        private String content;
        private String fileName;
    }
}
