package io.francoisbotha.namazingserver.domain.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class VendorDto {

    @NotNull
    @NotEmpty
    @Size(min = 6, max = 6)
    private String vendorCde;

    @NotNull
    @NotEmpty
    @Size(min = 3, max = 50)
    private String vendorName;

}
