package io.francoisbotha.namazingserver.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.*;


@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class VendorDto {

    private String id;
    private Integer num;

    @NotBlank
    @Size(min = 6, max = 6)
    private String vendorCde;

    @NotBlank
    @Size(min = 3, max = 50)
    private String vendorName;

    private String vendorLogoUrl;

}
