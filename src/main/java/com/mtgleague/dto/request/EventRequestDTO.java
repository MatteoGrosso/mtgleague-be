package com.mtgleague.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventRequestDTO {
    private Long eventId;
    private String name;
    private Date date;
    private int cap;
    private String description;

}
