package net.ddns.yline.withAPI.domain.contractFile;

import jakarta.persistence.*;
import lombok.*;
import net.ddns.yline.withAPI.domain.base.BaseEntity;
import net.ddns.yline.withAPI.domain.contract.Contract;
import net.ddns.yline.withAPI.domain.file.UploadedFile;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ContractFile extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Embedded
    private UploadedFile uploadedFile;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;

    //=== 연관관계 메서드 ===
    public void setContract(Contract contract) {
        this.contract = contract;
        contract.getContractFileList().add(this);
    }
}
