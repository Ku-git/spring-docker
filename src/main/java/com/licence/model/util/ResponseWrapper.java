package com.licence.model.util;

import lombok.Data;

import java.util.List;

@Data
public class ResponseWrapper {

    private int code;
    private String message;
    private Object data;

}
