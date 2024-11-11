package Projekt_Arbeit.Projekt_Anwendung.dtos;

import Projekt_Arbeit.Projekt_Anwendung.security.AccessToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginDtoOut
{
    private  final int id;
    private final String username;
    private final AccessToken credential;
}
