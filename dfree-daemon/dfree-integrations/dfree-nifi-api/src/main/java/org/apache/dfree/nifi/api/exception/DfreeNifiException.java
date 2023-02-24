package org.apache.dfree.nifi.api.exception;

public class DfreeNifiException extends RuntimeException {
    public DfreeNifiException(String error) {
        super(error);
    }

    public static void throwException(String error) {
        throw new DfreeNifiException(error);
    }
}
