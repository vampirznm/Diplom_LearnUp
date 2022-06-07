package ru.goremikin.keywords.pdf.payload;

import java.util.Objects;

/**
 * Класс описывает объект одной рекомендации, соответствующий одной строке файла конфигурации.
 */
public class Suggest {

    private final String keyWord;
    private final String title;
    private final String url;
    private final String keyWordLowerCase;

    public Suggest(String keyWord, String title, String url) {
        this.keyWord = keyWord;
        this.title = title;
        this.url = url;
        this.keyWordLowerCase = keyWord.toLowerCase();
    }

    public String getKeyWord() {
        return keyWord;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getKeyWordLowerCase() {
        return keyWordLowerCase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Suggest suggest = (Suggest) o;
        return Objects.equals(keyWord, suggest.keyWord) && Objects.equals(title, suggest.title) && Objects.equals(url, suggest.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keyWord, title, url);
    }

    @Override
    public String toString() {
        return String.format("Suggest: keyWord = %s, title = %s, url = %s", keyWord, title, url);
    }
}