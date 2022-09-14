package com.onchain.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Notify {
    @JsonProperty("ContractAddress")
    private String contractAddress;

    @JsonProperty("States")
    private Object states;
}
