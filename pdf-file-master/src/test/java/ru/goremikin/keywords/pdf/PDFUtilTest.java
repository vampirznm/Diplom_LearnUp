package ru.goremikin.keywords.pdf;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ru.goremikin.keywords.pdf.payload.LinksSuggester;
import ru.goremikin.keywords.pdf.util.DirNameConstants;
import ru.goremikin.keywords.pdf.util.PDFUtil;

import java.io.File;
import java.io.IOException;

/**
 * Тест редактирования pdf-файлов
 */
public class PDFUtilTest {

    private LinksSuggester ls;

    @BeforeEach
    public void init() throws IOException {
        ls = new LinksSuggester(new File(DirNameConstants.TESTING_DIR_NAME + "/config"));
    }

    @Test
    @DisplayName("test1.pdf: 1 стр, содержит 3 ключевых слова на стр -> 2 стр")
    public void edit_when1PageHave3KeyWords_then2Page() throws IOException {
        File fileIn = new File(
                DirNameConstants.TESTING_DIR_NAME + DirNameConstants.SUB_DIR_ALL_PDFS_NAME + "test1.pdf"
        );
        PDFUtil pdf = new PDFUtil(fileIn, ls, DirNameConstants.TESTING_DIR_NAME);
        pdf.edit();
        File fileOut = new File(
                DirNameConstants.TESTING_DIR_NAME + DirNameConstants.SUB_DIR_ALL_PDFS_RESULT_NAME + "test1.pdf"
        );
        PdfDocument doc2 = new PdfDocument(new PdfReader(fileOut));
        Assertions.assertEquals(2, pdf.getPageCount(doc2));
    }

    @Test
    @DisplayName("test2.pdf: 3 стр, содержит по 3 ключевых слова на каждой стр (т.е. повторения ключевых слов) -> 4 стр")
    public void edit_when3PageHaveDuplicatesKeyWords_then4Page() throws IOException {
        File fileIn = new File(
                DirNameConstants.TESTING_DIR_NAME + DirNameConstants.SUB_DIR_ALL_PDFS_NAME + "test2.pdf"
        );
        PDFUtil pdf = new PDFUtil(fileIn, ls, DirNameConstants.TESTING_DIR_NAME);
        pdf.edit();
        File fileOut = new File(
                DirNameConstants.TESTING_DIR_NAME + DirNameConstants.SUB_DIR_ALL_PDFS_RESULT_NAME + "test2.pdf"
        );
        PdfDocument doc2 = new PdfDocument(new PdfReader(fileOut));
        Assertions.assertEquals(4, pdf.getPageCount(doc2));
    }

    @Test
    @DisplayName("test3.pdf: 3 стр, содержит по 1 ключевому слову на каждой стр -> 6 стр")
    public void edit_when3PageHaveNoDuplicatesAnd1KeyWordsInPage_then6Page() throws IOException {
        File fileIn = new File(
                DirNameConstants.TESTING_DIR_NAME + DirNameConstants.SUB_DIR_ALL_PDFS_NAME + "test3.pdf"
        );
        File fileOut = new File(
                DirNameConstants.TESTING_DIR_NAME + DirNameConstants.SUB_DIR_ALL_PDFS_RESULT_NAME + "test3.pdf"
        );
        PDFUtil pdf = new PDFUtil(fileIn, ls, DirNameConstants.TESTING_DIR_NAME);
        pdf.edit();
        PdfDocument doc2 = new PdfDocument(new PdfReader(fileOut));
        Assertions.assertEquals(6, pdf.getPageCount(doc2));
    }

    @Test
    @DisplayName("test4.pdf: 1 стр, не содержит ключевые слова -> 1 стр")
    public void edit_when1PageNotKeyWords_then1Page() throws IOException {
        File fileIn = new File(
                DirNameConstants.TESTING_DIR_NAME + DirNameConstants.SUB_DIR_ALL_PDFS_NAME + "test4.pdf"
        );
        File fileOut = new File(
                DirNameConstants.TESTING_DIR_NAME + DirNameConstants.SUB_DIR_ALL_PDFS_RESULT_NAME + "test4.pdf"
        );
        PDFUtil pdf = new PDFUtil(fileIn, ls, DirNameConstants.TESTING_DIR_NAME);
        pdf.edit();
        PdfDocument doc2 = new PdfDocument(new PdfReader(fileOut));
        Assertions.assertEquals(1, pdf.getPageCount(doc2));
    }

    @Test
    @DisplayName("test5.pdf: 6 стр, 1 - кл.слово, 2 - нет, 3 - кл.слово, 4 - нет, 5 - кл.слово, 6 - повтор кл.слова -> 9 стр")
    public void edit_when6PageHaveNotKeyWordsAndDuplicates_then9Page() throws IOException {
        File fileIn = new File(
                DirNameConstants.TESTING_DIR_NAME + DirNameConstants.SUB_DIR_ALL_PDFS_NAME + "test5.pdf"
        );
        File fileOut = new File(
                DirNameConstants.TESTING_DIR_NAME + DirNameConstants.SUB_DIR_ALL_PDFS_RESULT_NAME + "test5.pdf"
        );
        PDFUtil pdf = new PDFUtil(fileIn, ls, DirNameConstants.TESTING_DIR_NAME);
        pdf.edit();
        PdfDocument doc2 = new PdfDocument(new PdfReader(fileOut));
        Assertions.assertEquals(9, pdf.getPageCount(doc2));
    }

}