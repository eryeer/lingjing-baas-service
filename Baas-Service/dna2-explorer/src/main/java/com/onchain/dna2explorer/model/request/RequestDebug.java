package com.onchain.dna2explorer.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDebug {
    String method;
    Integer id;
    String jsonrpc;
    String[] params;
}
