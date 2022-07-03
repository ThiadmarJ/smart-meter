package com.thiadmar.smartmeter.model.reading;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Elec_Readings")
public class ElecReading {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long meterId;
    private Long reading;
    private Date date;
    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "reading_id")
    private Reading readingMain;

    public ElecReading() {
    }

    public ElecReading( Long meterId, Long reading, Date date, Reading readingMain) {
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

    public void setReadingMain(Reading readingMain) {
        this.readingMain = readingMain;
    }
}
