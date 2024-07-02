package httpApi.exp;

public class NotReadFromRedisException extends RuntimeException
{

    private String message;

    public NotReadFromRedisException()
    {
        this.message = "Record not found";
    }


    public NotReadFromRedisException(String message)
    {
        super(message);
    }


    public NotReadFromRedisException(String message, Throwable cause)
    {
        super(message, cause);
    }

}

