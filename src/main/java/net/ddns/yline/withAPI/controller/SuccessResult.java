package net.ddns.yline.withAPI.controller;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessResult {
    private String code;
    private String message;
}
