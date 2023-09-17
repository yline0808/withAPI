package net.ddns.yline.withAPI.domain.contract;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.ddns.yline.withAPI.domain.base.BaseEntity;
import net.ddns.yline.withAPI.domain.contractmap.ContractMap;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Contract extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;
    @OneToMany(mappedBy = "contract")
    private List<ContractMap> contractMapList = new ArrayList<>();
    @Setter
    private String title;
    @Setter
    private String content;
    @Setter
    @Enumerated(EnumType.STRING)
    private ContractStatus contractStatus;
    private String imgSeq;
    @Setter
    private String fileName;
    private String fileSeq;
}
