package ru.goremikin.keywords.pdf;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ru.goremikin.keywords.pdf.payload.LinksSuggester;
import ru.goremikin.keywords.pdf.payload.Suggest;
import ru.goremikin.keywords.pdf.util.DirNameConstants;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Тест метода suggest() класса LinksSuggester
 */
public class SuggestMethodTest {

    private LinksSuggester ls;

    private Suggest suggest1;
    private Suggest suggest2;
    private Suggest suggest3;

    @BeforeEach
    public void init() throws IOException {
        ls = new LinksSuggester(new File(DirNameConstants.TESTING_DIR_NAME + "/config"));

        // Suggest: keyWord = класс, title = Good article about classes, url = http://example.org/class
        suggest1 = ls.getSuggestsInConfigFile().get(0);

        // Suggest: keyWord = java, title = The Best Java course, url = http://example.org/java
        suggest2 = ls.getSuggestsInConfigFile().get(1);

        //Suggest: keyWord = объект, title = OOP principles in practice, url = http://example.org/objects
        suggest3 = ls.getSuggestsInConfigFile().get(2);
    }

    @Test
    @DisplayName("метод корректно заполняет список всех подошедших рекомендаций: игнорирует регистор")
    public void suggest_whenDifferentRegistrarInText_thenCorrectlyFillsListRecommendation() {
        List<Suggest> expected = new ArrayList<>(List.of(
                suggest1, suggest2, suggest3
        ));
        Assertions.assertArrayEquals(expected.toArray(), ls.suggest("JAVA КЛАССЫ объект").toArray());
    }

    @Test
    @DisplayName("метод корректно заполняет список всех подошедших рекомендаций: без дубликатов")
    public void suggest_whenDuplicatesInText_thenCorrectlyFillsListRecommendation() {
        List<Suggest> expected = new ArrayList<>(List.of(
                suggest1, suggest2
        ));
        Assertions.assertArrayEquals(expected.toArray(),ls.suggest("JAVA java JAva jAVa класс").toArray());
    }

    @Test
    @DisplayName("метод корректно заполняет список всех подошедших рекомендаций: в тексте нет рекомендаций")
    public void suggest_whenNoRecommendationsInText_thenCorrectlyFillsListRecommendation() {
        Assertions.assertArrayEquals(new ArrayList<Suggest>().toArray(),ls.suggest("A B C D").toArray());
    }

}