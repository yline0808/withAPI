package net.ddns.yline.withAPI.domain.contractmap;

import jakarta.persistence.*;
import lombok.Getter;
import net.ddns.yline.withAPI.domain.account.Account;
import net.ddns.yline.withAPI.domain.contract.Contract;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
public class ContractMap {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;
}
