package com.onchain.dna2explorer.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDebugTraceTransaction {
    String type;
    String from;
    String to;
    String value;
    String gas;
    String gasUsed;
    String input;
    String output;
    String error;
    String revertReason;
    ResponseDebugTraceTransaction[] calls;
}
