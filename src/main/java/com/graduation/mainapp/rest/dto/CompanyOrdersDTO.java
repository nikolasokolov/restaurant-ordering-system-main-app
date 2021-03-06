package com.graduation.mainapp.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyOrdersDTO {

    private String username;
    private String restaurantName;
    private String menuItemName;
    private Integer menuItemPrice;
    private String timePeriod;
    private String dateOfOrder;
    private String comments;
}
