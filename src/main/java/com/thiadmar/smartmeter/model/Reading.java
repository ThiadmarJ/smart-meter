package com.thiadmar.smartmeter.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Readings")
public class Reading {

    @Id
    private Long accountId;

    @OneToMany(
            mappedBy = "readingMain",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<GasReading> gasReadings;

    @OneToMany(
            mappedBy = "readingMain",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ElecReading> elecReadings;

    public Reading() {
    }

    public Reading(Long accountId) {
        this.accountId = accountId;
    }

    public void setGasReadings(List<GasReading> gasReadings) {
        this.gasReadings = gasReadings;
    }

    public void setElecReadings(List<ElecReading> elecReadings) {
        this.elecReadings = elecReadings;
    }

    public Long getAccountId() {
        return accountId;
    }

    public List<GasReading> getGasReadings() {
        return gasReadings;
    }

    public List<ElecReading> getElecReadings() {
        return elecReadings;
    }
}
