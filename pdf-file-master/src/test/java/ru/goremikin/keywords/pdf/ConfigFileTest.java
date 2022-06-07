package ru.goremikin.keywords.pdf;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ru.goremikin.keywords.pdf.exception.WrongLinksFormatException;
import ru.goremikin.keywords.pdf.payload.LinksSuggester;
import ru.goremikin.keywords.pdf.util.DirNameConstants;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Тест конфигурационного файла
 */
public class ConfigFileTest {

    @Test
    @DisplayName("конфигурационный файл пустой")
    public void configFile_whenEmpty_thenWrongLinksFormatException() {
        Assertions.assertThrows(
                WrongLinksFormatException.class, () -> new LinksSuggester(
                        new File(DirNameConstants.TESTING_DIR_NAME + "/config1")
                ));
    }

    @Test
    @DisplayName("конфигурационный файл содержит некорректные данные")
    public void configFile_whenContainsIncorrectData_thenWrongLinksFormatException() {
        Assertions.assertThrows(
                WrongLinksFormatException.class, () -> new LinksSuggester(
                        new File(DirNameConstants.TESTING_DIR_NAME + "/config2")
                ));
    }

    @Test
    @DisplayName("конфигурационный файл отсутствует")
    public void configFile_whenAbsent_thenFileNotFoundException() {
        Assertions.assertThrows(
                FileNotFoundException.class, () -> new LinksSuggester(
                        new File(DirNameConstants.TESTING_DIR_NAME + "/config3")
                ));
    }

}