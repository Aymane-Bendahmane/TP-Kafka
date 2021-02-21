package org.sid.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
@Data
@AllArgsConstructor @NoArgsConstructor @ToString
public class PageEvent {
    private String name;
    private String alias;
    private LocalDate date;
    private Integer nbr ;
}
