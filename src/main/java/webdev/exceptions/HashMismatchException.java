package webdev.exceptions;

import java.lang.RuntimeException;

/**
    Exception is thrown when the hash sent by Paynow is not equal to the hash generated locally
 */
public class HashMismatchException extends RuntimeException
{
}