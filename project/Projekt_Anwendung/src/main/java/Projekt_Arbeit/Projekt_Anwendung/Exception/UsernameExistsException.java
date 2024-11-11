package Projekt_Arbeit.Projekt_Anwendung.Exception;

public class UsernameExistsException extends RuntimeException
{
    public UsernameExistsException(String message)
    {
        super(message);
    }
}
