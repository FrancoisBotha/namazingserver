package io.francoisbotha.namazingserver.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.*;


@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class SpecialDto {

    private String id;
    private Integer specialNo;

    @NotBlank
    @Size(min = 3, max = 50)
    private String specialName;

    private String specialLogoUrl;

    private String specialDesc;
}
