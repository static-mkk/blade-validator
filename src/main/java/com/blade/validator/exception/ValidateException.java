
package com.blade.validator.exception;

public class ValidateException extends RuntimeException {

    private static final long serialVersionUID = -5770807215499382620L;

    private String errMsg;

    public ValidateException(String errMsg) {
        super(errMsg);
        this.errMsg = errMsg;
    }

    public String getErrMsg() {
        return errMsg;
    }
}
