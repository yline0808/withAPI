package net.ddns.yline.withAPI.domain.contractMap;

import jakarta.persistence.*;
import lombok.*;
import net.ddns.yline.withAPI.domain.account.Account;
import net.ddns.yline.withAPI.domain.contract.Contract;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ContractMap {

    @Id
    @GeneratedValue
    private Long id;

    @Setter
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;

    @Setter
    @Enumerated(STRING)
    private Opinion opinion;

    //=== 연관관계 메서드 ===
    public void setAccount(Account account) {
        this.account = account;
        account.getContractMapList().add(this);
    }

    public void setContract(Contract contract) {
        this.contract = contract;
        contract.getContractMapList().add(this);
    }
}
