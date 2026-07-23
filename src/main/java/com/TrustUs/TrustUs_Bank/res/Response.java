package com.TrustUs.TrustUs_Bank.res;

import com.TrustUs.TrustUs_Bank.auth_users.dtos.LoginResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {

    private int statusCode;
    private String message;
    private T data;
    private Map<String, Serializable> meta; //Metadata for pagination
}


