package com.graduation.mainapp.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuItemDTO {

    private Long id;
    private String type;
    private String name;
    private Integer price;
    private String allergens;
    private Boolean isAvailable;
}
