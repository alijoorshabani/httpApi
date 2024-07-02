package httpApi.exp;

public class InvalidDataException extends RuntimeException
{

    private String message;

    public InvalidDataException()
    {
        this.message = "Invalid Data";
    }


    public InvalidDataException(String message)
    {
        super(message);
    }


    public InvalidDataException(String message, Throwable cause)
    {
        super(message, cause);
    }

}
