package org.example.framework.domain.models;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactRequest extends ModelBase {
    private String name;
    private String email;
    private String phone;
}
