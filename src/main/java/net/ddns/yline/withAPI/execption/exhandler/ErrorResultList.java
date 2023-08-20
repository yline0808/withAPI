package net.ddns.yline.withAPI.execption.exhandler;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrorResultList {
    private String code;
    private List<String> messageList;
}
