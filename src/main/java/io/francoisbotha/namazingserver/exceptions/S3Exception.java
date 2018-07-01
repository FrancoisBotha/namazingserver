package io.francoisbotha.namazingserver.exceptions;

/*******************
 * Courtesy of:
 * https://github.com/mtedone/devopsbuddy/blob/develop/src/main/java/com/devopsbuddy/exceptions/S3Exception.java
 */

/**
 * Created by tedonema on 14/05/2016.
 */
public class S3Exception extends RuntimeException {

    public S3Exception(Throwable e) {
        super(e);
    }

    public S3Exception(String s) {
        super(s);
    }
}