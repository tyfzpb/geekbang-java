package org.geektimes.oauth.exception;

public class AuthException extends RuntimeException{

    private int errorCode;

    private String errorMsg;

    public AuthException(String errorMsg){
        this(500,errorMsg);
    }

    public  AuthException(int errorCode,String errorMsg){
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public AuthException(String errorMsg, Throwable cause){
        super(errorMsg, cause);
    }


    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
