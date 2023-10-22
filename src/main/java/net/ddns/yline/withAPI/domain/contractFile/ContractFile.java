package net.ddns.yline.withAPI.domain.contractFile;

import jakarta.persistence.*;
import lombok.*;
import net.ddns.yline.withAPI.domain.base.BaseEntity;
import net.ddns.yline.withAPI.domain.contract.Contract;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractFile extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String originalName;
    private String saveName;
    private String type;
    private Long size;
    private FileStatus fileStatus;
    @Setter
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;
}
