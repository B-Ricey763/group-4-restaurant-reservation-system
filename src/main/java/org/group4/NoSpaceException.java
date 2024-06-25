package org.group4;

class NoSpaceException extends RuntimeException {
    public NoSpaceException() {
        super("Restaurant has no space.");
    }
}