package com.ydwf.bridge.base;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * 自定义出单对接异常类
 *
 * @author : huangshuanbao
 * @version : V1.0
 * @date : 2020/12/21 2:53 下午
 */
public class IssueOutException extends RuntimeException {

    private static final long serialVersionUID = 2069749923409209302L;

    public IssueOutException(String message) {
        super(message);
    }

    public IssueOutException(String message, Throwable cause) {
        super(message, cause);
    }

    public IssueOutException(Throwable cause) {
        super(cause);
    }

    public IssueOutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public String getLocalizedMessage() {
        return super.getLocalizedMessage();
    }

    @Override
    public synchronized Throwable getCause() {
        return super.getCause();
    }

    @Override
    public synchronized Throwable initCause(Throwable cause) {
        return super.initCause(cause);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }

    @Override
    public void printStackTrace(PrintStream s) {
        super.printStackTrace(s);
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return super.fillInStackTrace();
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }

    @Override
    public void setStackTrace(StackTraceElement[] stackTrace) {
        super.setStackTrace(stackTrace);
    }

}
