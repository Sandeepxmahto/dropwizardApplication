package com.company.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SATResult {
    @JsonProperty
    @NotNull
    private String name;

    @NotNull
    @JsonProperty
    private String address;

    @NotNull
    @JsonProperty
    private String city;

    @NotNull
    @JsonProperty
    private String country;

    @NotNull
    @JsonProperty
    private String pincode;

    @NotNull
    @JsonProperty
    private int satScore;

    @JsonProperty
    private boolean passed;

    public SATResult(String name, String address, String city, String country, String pincode, int satScore){
        this.name = name;
        this.address = address;
        this.city = city;
        this.country = country;
        this.pincode = pincode;
        this.satScore = satScore;
        if(satScore > 30){
            this.passed = true;
        }
        else {
            this.passed = false;
        }
    }
}
