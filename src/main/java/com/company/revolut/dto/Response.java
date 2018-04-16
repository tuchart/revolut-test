package com.company.revolut.dto;

import lombok.Data;

@Data
public class Response {

    private String requestId;
    private Object body;
    private int statusCode;
    private String errorMessage;
}
