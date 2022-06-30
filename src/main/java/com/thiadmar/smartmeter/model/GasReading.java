package com.thiadmar.smartmeter.model;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Gas_Readings")
public class GasReading {

    @Id
    private Long id;
    private Long meterId;
    private Long reading;
    private Date date;
    @ManyToOne
    @JoinColumn(name = "reading_id")
    private Reading readingMain;

    public GasReading() {
    }

    public GasReading(Long id, Long meterId, Long reading, Date date, Reading readingMain) {
        this.id = id;
        this.meterId = meterId;
        this.reading = reading;
        this.date = date;
        this.readingMain = readingMain;
    }
}
