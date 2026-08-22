package com.navaneeth.multitenantgstplatform.common.exception;

public class UserInactiveException extends RuntimeException {

    public UserInactiveException(String message) {
        super(message);
    }
}