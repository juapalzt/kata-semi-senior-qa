package org.example.framework.domain.models;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO for login response payloads.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse extends ModelBase {
    private String token;
    private String userId;
}
