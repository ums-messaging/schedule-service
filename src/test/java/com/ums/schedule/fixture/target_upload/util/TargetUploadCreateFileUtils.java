package com.ums.schedule.fixture.target_upload.util;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.UUID;

public abstract class TargetUploadCreateFileUtils {

    public static Path createTargetList(int rowSize) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        createHeader(sheet);
        createRows(sheet, rowSize);

        Path temp = Files.createTempFile("target-", ".xlsx");
        OutputStream os = Files.newOutputStream(temp);
        workbook.write(os);

        return temp;
    }

    private static void createRows(XSSFSheet sheet, int rowSize) {
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

    private static void createHeader(XSSFSheet sheet) {
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("key");
        header.createCell(1).setCellValue("name");
        header.createCell(2).setCellValue("email");
        header.createCell(3).setCellValue("birthday");
        header.createCell(4).setCellValue("fruit");
        header.createCell(5).setCellValue("position");
    }
}
