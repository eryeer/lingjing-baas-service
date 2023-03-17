package com.onchain.dna2explorer.model.response;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.formula.functions.T;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseRpc<T> {
    String jsonrpc;
    Integer     id;
    T       result;
    String error;
}
