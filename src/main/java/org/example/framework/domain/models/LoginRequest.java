package org.example.framework.domain.models;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO for login request payloads.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest extends ModelBase {
    private String username;
    private String password;
}
