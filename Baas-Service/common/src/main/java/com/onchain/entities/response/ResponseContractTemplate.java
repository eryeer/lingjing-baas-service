package com.onchain.entities.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseContractTemplate {
    private String templateType;
    private String contractFileUrl;
    private String abiContent;
}
