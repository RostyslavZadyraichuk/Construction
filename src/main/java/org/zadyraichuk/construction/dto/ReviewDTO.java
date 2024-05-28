package org.zadyraichuk.construction.dto;

import lombok.Getter;
import org.zadyraichuk.construction.dto.simple.ProjectSimpleDTO;

import java.time.LocalDate;

@Getter
public class ReviewDTO {

    private String description;
    private LocalDate date;
    private double starRate;
    private ProjectSimpleDTO project;

    public ReviewDTO(String description,
                     LocalDate date,
                     double starRate,
                     ProjectSimpleDTO project) {
        this.description = description;
        this.date = date;
        this.starRate = starRate;
        this.project = project;
    }
}
