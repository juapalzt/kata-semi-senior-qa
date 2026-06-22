package org.example.framework.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactResponse extends ModelBase {
    @JsonProperty("_id")
    private String id;
    private String name;
    private String email;
    private String phone;
}
