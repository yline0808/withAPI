package net.ddns.yline.withAPI.repository.contract;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.ddns.yline.withAPI.domain.contract.ContractStatus;

@Data
@AllArgsConstructor
public class ContractDto {
    private Long contractId;
    private String title;
    @Enumerated(EnumType.STRING)
    private ContractStatus contractStatus;
}
