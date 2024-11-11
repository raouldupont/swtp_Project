package Projekt_Arbeit.Projekt_Anwendung.security;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class AccessToken {
    private String accessToken;

public AccessToken(String uuid)
{
    this.accessToken = uuid;
}
public AccessToken(){}

    @Override
    public boolean equals(Object x)
    {
        if (this == x) return true;
        if (x == null || getClass() != x.getClass()) return false;
        AccessToken accessToken = (AccessToken) x;
        return Objects.equals(this.accessToken, accessToken.accessToken);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(accessToken);
    }
}
