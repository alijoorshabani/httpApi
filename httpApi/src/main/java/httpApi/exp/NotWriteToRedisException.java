package httpApi.exp;

public class NotWriteToRedisException extends RuntimeException
{

        private String message;

        public NotWriteToRedisException()
        {
            this.message = "Record not write";
        }


        public NotWriteToRedisException(String message)
        {
            super(message);
        }


        public NotWriteToRedisException(String message, Throwable cause)
        {
            super(message, cause);
        }

}
