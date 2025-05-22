package org.example.Security;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private String accesToken;
    private String tokenType = "Bearer ";

    public AuthResponseDTO(String accesToken)
    {
        this.accesToken = accesToken;
    }
}
