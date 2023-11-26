package net.ddns.yline.withAPI.dto.contract;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContractSearchCondition {
    private String title;
    private String content;
    private String email;
    private LocalDateTime createGoeDate;
    private LocalDateTime createLoeDate;
}
