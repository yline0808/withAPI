package net.ddns.yline.withAPI.domain.contract;

import jakarta.persistence.*;
import lombok.*;
import net.ddns.yline.withAPI.domain.base.BaseEntity;
import net.ddns.yline.withAPI.domain.contractFile.ContractFile;
import net.ddns.yline.withAPI.domain.contractMap.ContractMap;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Contract extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;
    @Builder.Default
    @OneToMany(mappedBy = "contract")
    private List<ContractMap> contractMapList = new ArrayList<>();
    private String title;
    private String content;
    @Enumerated(EnumType.STRING)
    private ContractStatus contractStatus;
    @Builder.Default
    @OneToMany(mappedBy = "contract")
    private List<ContractFile> contractFileList = new ArrayList<>();
}
