package org.example.RSC.DTO;

import lombok.Data;
import lombok.Getter;

@Data @Getter
public class AuthResponseDTO {
    private String accesToken;
    private String tokenType = "Bearer ";

    public AuthResponseDTO(String accesToken)
    {
        this.accesToken = accesToken;
    }
}
