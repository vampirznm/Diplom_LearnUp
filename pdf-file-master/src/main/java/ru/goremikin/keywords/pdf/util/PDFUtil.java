package ru.goremikin.keywords.pdf.util;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.annot.PdfLinkAnnotation;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.goremikin.keywords.pdf.payload.LinksSuggester;
import ru.goremikin.keywords.pdf.payload.Suggest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для работы с pdf-документом
 */

public class PDFUtil {

    private final File fileIn;
    private final File fileOut;
    private final LinksSuggester linksSuggester;
    private final List<Suggest> suggestsInDocument;
    private static final Logger LOGGER = LoggerFactory.getLogger(PDFUtil.class);

    public PDFUtil(File fileIn, LinksSuggester linksSuggester, String nameDirPdfToEdit) throws FileNotFoundException {
        this.fileIn = fileIn;
        if (!fileIn.exists()) {
            LOGGER.error("входной файл не найден - {}", fileIn.getPath());
            throw new FileNotFoundException("File input not found");
        }
        this.fileOut = new File(nameDirPdfToEdit + DirNameConstants.SUB_DIR_ALL_PDFS_RESULT_NAME + fileIn.getName());

        LOGGER.info(
                "FIND PDF: из директории получен pdf-документ, который нужно обработать - {}",
                fileIn.getPath()
        );
        this.linksSuggester = linksSuggester;
        this.suggestsInDocument = new ArrayList<>();
    }

    public File getFileIn() {
        return fileIn;
    }

    public File getFileOut() {
        return fileOut;
    }

    public LinksSuggester getLinksSuggester() {
        return linksSuggester;
    }

    public List<Suggest> getSuggestsInDocument() {
        return suggestsInDocument;
    }

    public void edit() {
        // создать объект редактируемого pdf-документа
        try (PdfDocument doc = new PdfDocument(new PdfReader(fileIn), new PdfWriter(fileOut))) {
            LOGGER.info(
                    "CREATE NEW PDF: создан pdf-документ для редактирования - {}",
                    fileOut.getPath()
            );
            int countPageInDocument = getPageCount(doc);
            LOGGER.info(
                    "START EDITING: начало редактирования pdf-документа, кол-во стр - {}",
                    countPageInDocument
            );
            int countPage = 1;
            while (countPage <= countPageInDocument) {
                PdfPage page = doc.getPage(countPage);
                var text = PdfTextExtractor.getTextFromPage(page);
                List<Suggest> suggestsInPage = linksSuggester.suggest(text);
                equalsRecommendationDocumentInPage(suggestsInDocument, suggestsInPage);
                if (!suggestsInPage.isEmpty()) {
                    var newPage = doc.addNewPage(countPage+1);
                    createInsertBlogToPagePdf(newPage, suggestsInPage);
                    countPageInDocument++;
                    countPage++;
                }
                countPage++;
            }
            LOGGER.info(
                    "END EDITING: конец редактирования pdf-документа, кол-во стр - {}",
                    countPageInDocument
            );
        } catch (IOException e) {
            LOGGER.error("не удалось создать pdf-файл - {}", fileIn.getPath());
            throw new RuntimeException("Failed to create pdf-file");
        }
    }

    /**
     * Получить кол-во страниц в pdf-документе
     */
    public int getPageCount(PdfDocument doc) {
        return doc.getNumberOfPages();
    }

    /**
     * Сравнение рекомендаций во всем документе с рекомендациями на странице
     */
    private void equalsRecommendationDocumentInPage(List<Suggest> suggestsInDoc, List<Suggest> suggestsInPage) {
        for (int i = 0; i < suggestsInPage.size(); i++) {
            Suggest suggest = suggestsInPage.get(i);
            if (suggestsInDoc.contains(suggest)) {
                suggestsInPage.remove(i);
                i--;
            } else {
                suggestsInDoc.add(suggest);
            }
        }
    }

    /**
     * Создает блок, который нужно вставить на страницу
     */
    private void createInsertBlogToPagePdf(PdfPage newPage, List<Suggest> suggests) {
        var rect = new Rectangle(newPage.getPageSize()).moveRight(10).moveDown(10);
        Canvas canvas = new Canvas(newPage, rect);
        Paragraph paragraph = new Paragraph("Suggestions:\n");
        paragraph.setFontSize(25);
        for (Suggest suggest : suggests) {
            Link link = addNewLinkToPagePdf(rect, suggest);
            paragraph.add(link.setUnderline());
            paragraph.add("\n");
        }
        canvas.add(paragraph);
    }

    /**
     * Добавляет в блок нужную ссылку на рекомендацию
     */
    private Link addNewLinkToPagePdf(Rectangle rect, Suggest suggest) {
        PdfLinkAnnotation annotation = new PdfLinkAnnotation(rect);
        PdfAction action = PdfAction.createURI(suggest.getUrl());
        annotation.setAction(action);
        return new Link(suggest.getTitle(), annotation);
    }

}