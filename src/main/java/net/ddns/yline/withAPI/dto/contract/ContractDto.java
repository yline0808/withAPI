package net.ddns.yline.withAPI.dto.contract;

import com.querydsl.core.annotations.QueryProjection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.ddns.yline.withAPI.domain.contract.ContractStatus;

@Data
public class ContractDto {
    private Long contractId;
    private String title;
    @Enumerated(EnumType.STRING)
    private ContractStatus contractStatus;


    @QueryProjection
    public ContractDto(Long contractId, String title, ContractStatus contractStatus) {
        this.contractId = contractId;
        this.title = title;
        this.contractStatus = contractStatus;
    }
}
