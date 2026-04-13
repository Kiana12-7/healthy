package org.example.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrentUserDTO {
    private Long id;
    private String name;
    private Integer days;
    private Integer calories;
    private Integer courses;
}
