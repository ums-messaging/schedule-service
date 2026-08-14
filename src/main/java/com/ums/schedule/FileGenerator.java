package com.ums.schedule;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class FileGenerator {
    public static void main(String[] args) throws IOException {
        createTargetList(100_000);
    }
    public static void createTargetList(int rowSize) throws IOException {
        SXSSFWorkbook workbook = new SXSSFWorkbook(100);
        Sheet sheet = workbook.createSheet();

        createHeader(sheet);
        createRows(sheet, rowSize);

        FileOutputStream os =
                new FileOutputStream("test-targets-20260810.xlsx");
        workbook.write(os);

        workbook.dispose();
    }

    private static void createRows(Sheet sheet, int rowSize) {
        for (int i = 1; i <= rowSize; i++) {
            Row row = sheet.createRow(i);
            createRow(row, i);
        }
    }

    private static void createRow(Row row, int i) {
        createRow(row, 0, UUID.randomUUID().toString());
        createRow(row, 1, RandomStringUtils.randomAlphabetic(6)); // name
        createRow(row, 2, randomEmail(i)); // email
        createRow(row, 3, randomBirthday(i)); // birthday
        createRow(row, 4, randomFruit(i)); // fruit
        createRow(row, 5, randomPosition(i)); // position
    }

    public static void createRow(Row row, int column, String value) {
        row.createCell(column).setCellValue(value);
    }

    private static String randomEmail(int i) {
        String[] emails = {"naver.com", "hanmail.com", "gmail.com", "yahoo.com","hotmail.com"};
        String randomId = RandomStringUtils.randomAlphabetic(8);
        int random = (int) (Math.random() * emails.length);
        return "%s@%s".formatted(randomId, emails[random]);
    }

    private static String randomBirthday(int i) {
        LocalDate startDt = LocalDate.of(1994, 3, 14);

        return startDt.plusDays(i).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    private static String randomFruit(int i) {
        String[] fruits = {"apple", "banana", "watermelon", "strawberry", "blueberry", "mango", "cherry"};
        int random = (int) (Math.random() * fruits.length);
        return fruits[random];
    }

    private static String randomPosition(int i) {
        String[] positions = {"owner", "leader", "staff", "senior", "junior", "guest", "none"};
        int random = (int) (Math.random() * positions.length);

        return positions[random];
    }

    private static void createHeader(Sheet sheet) {
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("key");
        header.createCell(1).setCellValue("name");
        header.createCell(2).setCellValue("email");
        header.createCell(3).setCellValue("birthday");
        header.createCell(4).setCellValue("fruit");
        header.createCell(5).setCellValue("position");
    }
}
