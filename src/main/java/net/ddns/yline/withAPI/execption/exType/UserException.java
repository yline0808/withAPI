package net.ddns.yline.withAPI.execption.exType;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class UserException extends RuntimeException{

    @Getter
    private List<String> errorMessageList = new ArrayList<>();

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, List<String> errorResultList) {
        super(message);
        this.errorMessageList = errorResultList;
    }
}
