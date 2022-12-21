package com.onchain.entities;

import lombok.Data;


@Data
public class SjResponse<T> {
    Integer code;
    String msg;
    String status;
    T data;
}
