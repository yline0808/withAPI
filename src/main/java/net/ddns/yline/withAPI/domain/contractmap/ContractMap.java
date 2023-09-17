package net.ddns.yline.withAPI.domain.contractmap;

import jakarta.persistence.*;
import lombok.*;
import net.ddns.yline.withAPI.domain.account.Account;
import net.ddns.yline.withAPI.domain.contract.Contract;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.*;

@Entity
@Getter
//@NoArgsConstructor
public class ContractMap {

    @Id
    @GeneratedValue
    private Long id;

    @Setter
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Setter
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "contract_id")
    private Contract contract;

    @Setter
    @Enumerated(STRING)
    private Opinion opinion;
}
