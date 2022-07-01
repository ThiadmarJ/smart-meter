package com.thiadmar.smartmeter.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Elec_Readings")
public class ElecReading {

    @Id
    private Long id;
    private Long meterId;
    private Long reading;
    private Date date;
    @ManyToOne
    @JoinColumn(name = "reading_id")
    private Reading readingMain;

    public ElecReading() {
    }

    public ElecReading(Long id, Long meterId, Long reading, Date date, Reading readingMain) {
        this.id = id;
        this.meterId = meterId;
        this.reading = reading;
        this.date = date;
        this.readingMain = readingMain;
    }

    public Long getId() {
        return id;
    }

    public Long getMeterId() {
        return meterId;
    }

    public Long getReading() {
        return reading;
    }

    public Date getDate() {
        return date;
    }
}
