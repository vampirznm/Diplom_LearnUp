package ru.goremikin.keywords.pdf.exception;

/**
 * Исключение - файл с рекомендациями содержит некорректные данные
 */
public class WrongLinksFormatException extends RuntimeException {

    public WrongLinksFormatException(String message) {
        super(message);
    }

}