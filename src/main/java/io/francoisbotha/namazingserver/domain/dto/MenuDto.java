package io.francoisbotha.namazingserver.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.*;


@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class MenuDto {

    private String id;
    private Integer menuNo;

    @NotBlank
    @Size(min = 3, max = 50)
    private String menuName;

    private String menuImgUrl;

    private String menuDesc;

    private Double menuAmt;

}
