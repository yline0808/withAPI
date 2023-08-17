package net.ddns.yline.withAPI.execption.exhandler.advice;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ErrorResultList {
    private String code;
    private Map<Integer, String> errMap;
}
