package ru.goremikin.keywords.pdf.payload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.goremikin.keywords.pdf.exception.WrongLinksFormatException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс предоставляющий рекомендации для документа
 */

public class LinksSuggester {

    private final File file; //file c рекомендациями
    private final static Logger LOGGER = LoggerFactory.getLogger(LinksSuggester.class);
    private final List<Suggest> suggestsInConfigFile;

    public LinksSuggester(File file) throws IOException, WrongLinksFormatException {
        this.file = file;
        if (!file.exists()) {
            LOGGER.error("конфигурационный файл не найден - {}", file.getAbsolutePath());
            throw new FileNotFoundException("File not found");
        }
        if (isFileRecommendationValid()) {
            LOGGER.info(
                    "CREATE RECOMMENDATIONS: созданы рекомендации на основании конфигурационного файла - {}",
                    file.getPath()
            );
        }
        suggestsInConfigFile = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(file.getPath()));
            for (String s : lines) {
                String[] line = s.split("\t");
                Suggest suggest = new Suggest(line[0], line[1], line[2]);
                suggestsInConfigFile.add(suggest);
            }
        } catch (IOException e) {
            LOGGER.error("ошибка ввода-вывода при чтении из конфигурационного файла - {}", file.getAbsolutePath());
            throw new RuntimeException("I/O error occurs reading from the file");
        }
    }

    public File getFile() {
        return file;
    }

    public List<Suggest> getSuggestsInConfigFile() {
        return suggestsInConfigFile;
    }

    /**
     * Анализирует переданный текст и возвращает список из всех подошедших рекомендаций
     * Проверка на то, что рекомендация уже использовалась для этого документа должна быть в main
     */
    public List<Suggest> suggest(String text) {
        List<Suggest> suggestsInText = new ArrayList<>();
        String textLowerCase = text.toLowerCase();
        for (Suggest suggest : suggestsInConfigFile) {
            int index = textLowerCase.indexOf(suggest.getKeyWordLowerCase());
            if (index >= 0) {
                suggestsInText.add(suggest);
            }
        }
        if (suggestsInText.isEmpty()) {
            LOGGER.info("NOT GET RECOMMENDATIONS: в тексте не нашлось подходящих рекомендаций");
        } else {
            LOGGER.info("GET RECOMMENDATIONS: из текста получены все подходящие рекомендации");
        }
        return suggestsInText;
    }

    /**
     * Проверка, что файл с рекомендациями содержит корректные данные:
     * каждая строка файла состоит именно из трёх частей
     * если нет кидать своё исключение WrongLinksFormatException
     */
    private boolean isFileRecommendationValid() throws IOException, WrongLinksFormatException {
        List<String> lines = Files.readAllLines(Paths.get(file.getPath()));
        if (lines.isEmpty()) {
            LOGGER.error("конфигурационный файл пустой - {}", file.getPath());
            throw new WrongLinksFormatException("Config file is empty");
        }
        for (String s : lines) {
            String[] recommendations = s.split("\t");
            if (recommendations.length != 3) {
                LOGGER.error("конфигурационный файл содержит некорректные данные - {}", file.getPath());
                throw new WrongLinksFormatException("Config file contains incorrect data");
            }
        }
        return true;
    }

}