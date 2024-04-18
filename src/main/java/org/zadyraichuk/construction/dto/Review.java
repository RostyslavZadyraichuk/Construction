package org.zadyraichuk.construction.dto;

import java.time.LocalDate;

public class Review {

    private String description;
    private LocalDate date;
    private double starRate;
    private ProjectConnector project;

    public Review(String description,
                  LocalDate date,
                  double starRate,
                  ProjectConnector project) {
        this.description = description;
        this.date = date;
        this.starRate = starRate;
        this.project = project;
    }
}
