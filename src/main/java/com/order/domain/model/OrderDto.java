package com.order.domain.model;


import com.order.domain.entity.Status;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class OrderDto {
    private Long id;
    @NotBlank
    @Pattern(regexp = "^\\d{8}$", message = "Zip code must be in the format 65064589")
    private String code;
    @Valid
    private List<ItemDto> items;
    private Status status;
    private BigDecimal valueTotal;
}