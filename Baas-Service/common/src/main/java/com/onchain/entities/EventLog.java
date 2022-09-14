package com.onchain.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EventLog {
    @JsonProperty("GasConsumed")
    private Long gasConsumed;

    @JsonProperty("Notify")
    private Notify[] notify;

    @JsonProperty("TxHash")
    private String txHash;

    @JsonProperty("State")
    private Boolean state;
}

