package com.thiadmar.smartmeter.model.reading;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Gas_Readings")
public class GasReading {

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

    public GasReading() {
    }

    public GasReading(Long meterId, Long reading, Date date, Reading readingMain) {

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
