package com.web.hyundai.service.news;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.web.hyundai.googlesheet.SpreadSheetService;
import com.web.hyundai.model.FileUpload;
import com.web.hyundai.model.Path;
import com.web.hyundai.model.news.News;
import com.web.hyundai.model.news.Subscribe;
import com.web.hyundai.repo.car.CarRepo;
import com.web.hyundai.repo.news.NewsRepo;
import com.web.hyundai.service.ImageService;
import com.web.hyundai.service.car.CarBuildService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class NewsService {

    private static String[] columns = {"FirstName", "LastName", "Email", "Date"};

    @Autowired
    private NewsRepo newsRepo;

    @Autowired
    private ImageService imageService;
    @Autowired
    private SpreadSheetService spreadSheetService;
    @Autowired
    private CarRepo carRepo;
    @Autowired
    private CarBuildService carBuildService;

    private final String JSON_FILTER_NAME = "newsFilter";
    private final Set<String> EXCLUDED_FIELDS = Stream.of("titleGEO", "textGEO", "displayTextGEO","car").collect(Collectors.toSet());


    public String deleteNews(Long id) {
        Optional<News> news = newsRepo.findById(id);
        if (news.isPresent()) {
            File image = new File(Path.folderPath() + news.get().getPhoto());
            File thumbnail = new File(Path.folderPath() + news.get().getThumbnail());

            if (image.exists()) image.delete();
            if (thumbnail.exists()) thumbnail.delete();

            newsRepo.deleteById(id);
            return "წარმატებით წაიშალა";
        }
        return "ასეთი ნიუსი არ მოიძებნა";
    }


    public ResponseEntity<String> newsUpdate(Long id, News updatedNews, MultipartFile file, Long carid) throws JsonProcessingException {


        AtomicInteger counter = new AtomicInteger(0);

        Optional<News> dbNews = newsRepo.findById(id);


        dbNews.ifPresent(oldNews -> {
            counter.incrementAndGet();

            oldNews.setTitle(updatedNews.getTitle());
            oldNews.setTitleGEO(updatedNews.getTitleGEO());
            oldNews.setDisplayText(updatedNews.getDisplayText());
            oldNews.setDisplayTextGEO(updatedNews.getDisplayTextGEO());
            oldNews.setText(updatedNews.getText());
            oldNews.setTextGEO(updatedNews.getTextGEO());
            oldNews.setProm(updatedNews.isProm());
            //oldNews.setHashtag(updatedNews.getHashtag());
            oldNews.setShare(updatedNews.getShare());
            oldNews.setSort(updatedNews.getSort());
            if (carid != null) {

            carRepo.findById(carid).ifPresent(oldNews::setCar);
            }


            String slug = "0";

            Optional<News> checkNews = newsRepo.findBySlug(updatedNews.getSlugUrl());
            if (checkNews.isPresent() && !checkNews.get().getSlugUrl().equals(oldNews.getSlugUrl())) {
                counter.decrementAndGet();
            }
            oldNews.setSlugUrl(updatedNews.getSlugUrl());

            System.out.println(file + " faili");
            if (file != null) {

                try {
                    HashMap<String, String> nameMap = updateImage(file, oldNews.getPhoto(), oldNews.getThumbnail());
                    oldNews.setPhoto(nameMap.get("image"));
                    oldNews.setThumbnail((nameMap.get("thumbnail")));
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

        });

        if (counter.get() == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ნიუსი ვერ მოიძებნა ან ასეთი slugUrl უკვე არსებობს");
        }


        return ResponseEntity.ok(carBuildService.jsonFilter(JSON_FILTER_NAME,newsRepo.save(dbNews.get())));
    }


    private HashMap<String, String> updateImage(MultipartFile file, String oldImage, String oldThumbnail) throws IOException {

        System.out.println(Path.folderPath() + Path.newsPath() + oldImage);


        File oldFile = new File(Path.folderPath() + oldImage);
        File oldFileThumb = new File(Path.folderPath() + oldThumbnail);

        System.out.println(oldFile.getAbsolutePath());
        System.out.println(oldFileThumb.getAbsolutePath());

        if (oldFile.exists()) oldFile.delete();
        if (oldFileThumb.exists()) oldFileThumb.delete();


        return uploadNewsImage(file);


    }

    public HashMap<String, String> uploadNewsImage(MultipartFile file) throws IOException {


        FileUpload serverFile = imageService.uploadImage(file, Path.newsPath());


        HashMap<String, String> pathsMap = new HashMap<>();

        pathsMap.put("image", Path.newsPath() + serverFile.getFile().getName());
        pathsMap.put("thumbnail", imageService.thumbnail(serverFile.getFile().getAbsolutePath(),
                Objects.requireNonNull(file.getOriginalFilename()), serverFile.getRandomId(), serverFile.getSplitedName(),
                Path.newsPath()));


        return pathsMap;
    }


    public void translateNews(List<News> news) {

        news.forEach(news1 -> {
            news1.setTitle(news1.getTitleGEO());
            news1.setDisplayText(news1.getDisplayTextGEO());
            news1.setText(news1.getTextGEO());
        });

    }

    public void translateNews(News news) {

        news.setTitle(news.getTitleGEO());
        news.setDisplayText(news.getDisplayTextGEO());
        news.setText(news.getTextGEO());


    }


    private String readableDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }


    private List<List<Object>> objectDivisionTo2dArray(List<Subscribe> values) {

        List<List<Object>> result = new ArrayList<>();

        result.add(Arrays.asList("სახელი", "გვარი", "მეილი", "თარიღი"));

        values.forEach(o -> {
            List<Object> oneValueList = new ArrayList<>();
            oneValueList.add(o.getFirstName());
            oneValueList.add(o.getLastName());
            oneValueList.add(o.getEmail());
            oneValueList.add(readableDate(o.getDate()));
            result.add(oneValueList);
        });


        return result;
    }


    public boolean createSpreadSheet(List<Subscribe> subscribers, Date date) throws IOException {


        List<List<Object>> subscriberLists = objectDivisionTo2dArray(subscribers);

        System.out.println(subscriberLists);
        System.out.println(date);

        String id = spreadSheetService.createSheet("იუზერები თარიღამდე " + readableDate(date));

        return spreadSheetService.writeToSheet(id, subscriberLists);


    }


    public String createXLS(List<Subscribe> subscribers, Date date) throws IOException {


        // Create a Workbook
        Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

        /* CreationHelper helps us create instances of various things like DataFormat,
           Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way */
        CreationHelper createHelper = workbook.getCreationHelper();

        // Create a Sheet
        Sheet sheet = workbook.createSheet("Users");

        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Create cells
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        // Create Cell Style for formatting Date
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

        // Create Other rows and cells with employees data
        int rowNum = 1;
        for (Subscribe subscribe : subscribers) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(subscribe.getFirstName());
            row.createCell(1).setCellValue(subscribe.getLastName());

            Cell dateOfBirthCell = row.createCell(3);
            dateOfBirthCell.setCellValue(subscribe.getDate());
            dateOfBirthCell.setCellStyle(dateCellStyle);

            row.createCell(2) .setCellValue(subscribe.getEmail());
        }

        // Resize all columns to fit the content size
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        File file = new File(Path.folderPath() + "excel\\", "users till " + date.getTime() + ".xlsx");
        FileOutputStream fileOut = new FileOutputStream(file);
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();


        return Path.folderPath() + "excel\\" + "test.xlsx";
    }


    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public String makeSlug(String input) {
        if (!input.matches("^[a-zA-Z0-9. -_?]*$")) {
            return "0";
        }
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }


}
