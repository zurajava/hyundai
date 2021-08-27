package com.web.hyundai.controller.car.form;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.web.hyundai.model.Path;
import com.web.hyundai.model.car.Car;
import com.web.hyundai.model.car.Engine;
import com.web.hyundai.model.car.forms.CarInvoice;
import com.web.hyundai.model.car.forms.CarTestDrive;
import com.web.hyundai.model.car.modif.CarComplect;
import com.web.hyundai.repo.car.CarColorRepo;
import com.web.hyundai.repo.car.CarRepo;
import com.web.hyundai.repo.car.EngineRepo;
import com.web.hyundai.repo.car.form.CarInvoiceRepo;
import com.web.hyundai.repo.car.form.CarTestDriveRepo;
import com.web.hyundai.repo.car.modif.CarComplectRepo;
import com.web.hyundai.repo.car.modif.CarTireRepo;
import com.web.hyundai.repo.car.modif.ComplectInterierFeatureRepo;
import com.web.hyundai.service.ImageService;
import com.web.hyundai.service.email.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/forms")
@CrossOrigin("*")
public class CarFormController {

  private final Logger LOGGER = LoggerFactory.getLogger(CarFormController.class);

  @Value("${mailListTest}")
  private String[] TO;
  private final String SUBJECT = "Test Drive";
  private final String TEXT = "მომხმარებელი %s ჩაეწერა ავტომობილის ტესტდრაივზე ჰიუნდაი %s , კომპლექტაციით: %s. მეილი: %s , მობილური: %s";
  private final String TEXT_CLIENT = "თქვენ ჩაეწერეთ Hyundai %s -ს ტესტ დრაივზე, ჩვენი ოპერატორი მალევე დაგიკავშირდებათ. გისურვებთ წარმატებებს";

  @Autowired
  private CarRepo carRepo;
  @Autowired
  private CarTestDriveRepo carTestDriveRepo;
  @Autowired
  private CarInvoiceRepo carInvoiceRepo;
  @Autowired
  private EmailService emailService;
  @Autowired
  private CarComplectRepo carComplectRepo;
  @Autowired
  private CarTireRepo carTireRepo;
  @Autowired
  private CarColorRepo carColorRepo;
  @Autowired
  private ComplectInterierFeatureRepo complectInterierFeatureRepo;
  @Autowired
  private ImageService imageService;
  @Autowired
  private CarComplectRepo CarComplectRepo;


  @PostMapping("/invoice")
  public String invoiceRegister(@RequestParam Long tireid,
      @RequestParam Long colorid,
      @RequestParam Long complectid,
      @RequestParam Long carid,
      @RequestParam ArrayList<Long> featureList,
      @RequestBody @Valid CarInvoice carInvoice
  ) throws IOException, DocumentException, MessagingException {
    LOGGER.info("----------------Starting generate invoice-----------------------------");
    carInvoice.setCarid(carid);
    carInvoice.setColorid(colorid);
    carInvoice.setComplectid(complectid);
    carInvoice.setTireid(tireid);
    Document document = new Document();

    String path = "pdf/invoice-" + imageService.generateUUID() + ".pdf";
    String fullPath = Path.folderPath() + path;
    PdfWriter.getInstance(document, new FileOutputStream(fullPath));
    File pdfFile = new File(fullPath);
    document.open();
    Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);

    Optional<CarComplect> complect = carComplectRepo.findById(complectid);
    Optional<Car> car = carRepo.findById(carid);

    Paragraph paragraph1 = new Paragraph("Name: " + carInvoice.getName());
    Paragraph paragraph2 = new Paragraph("Email: " + carInvoice.getEmail());
    Paragraph paragraph3 = new Paragraph("Phone: " + carInvoice.getNumber());
    paragraph1.setSpacingBefore(50);
    paragraph3.setSpacingAfter(50);
    document.add(paragraph1);
    document.add(paragraph2);
    document.add(paragraph3);

    if (car.isPresent() && complect.isPresent()) {
      //CAR
      Car car1 = car.get();
      Image img = Image.getInstance(Path.folderPath() + car1.getSlider());
      img.scaleAbsolute(400, 225);
      img.setAlignment(Element.ALIGN_CENTER);
      PdfPTable table = new PdfPTable(2);
      table.setSpacingBefore(50);
      table.addCell("Model");
      table.addCell(car1.getModel());
      document.add(img);
      document.add(table);

      //COMPLECT
      CarComplect complect1 = complect.get();

      PdfPTable table3 = new PdfPTable(2);
      table3.addCell("Engine");
      table3.addCell(String.valueOf(complect1.getEngine().getTitle()));
      document.add(table3);

      carTireRepo.findById(tireid).ifPresent(carTire -> {
        try {
          PdfPTable table4 = new PdfPTable(2);
          table4.addCell("Tire");
          table4.addCell(carTire.getTitle());
          document.add(table4);
        } catch (DocumentException e) {
          e.printStackTrace();
        }
      });

      carColorRepo.findById(colorid).ifPresent(carColor -> {
        try {
          PdfPTable table5 = new PdfPTable(2);
          table5.addCell("Color");
          table5.addCell(carColor.getColorName());
          document.add(table5);
        } catch (DocumentException e) {
          e.printStackTrace();
        }
      });

      complectInterierFeatureRepo.findAllById(featureList).forEach(feature -> {

        try {
          PdfPTable table6 = new PdfPTable(2);
          table6.addCell("Interier Feature");
          table6.addCell(feature.getFeature());
          document.add(table6);
        } catch (DocumentException e) {
          e.printStackTrace();
        }


      });
      PdfPTable table2 = new PdfPTable(2);
      table2.addCell("Price");
      table2.addCell(String.valueOf(complect1.getEngine().getPrice()));
      document.add(table2);

    }

    document.close();

    emailService.sendFileMessage(carInvoice.getEmail(), "ინვოისი",
        "გამარჯობა, გიგზავნით თქვენსმიერ შერჩეულ კომპლექტაციას", pdfFile);
    carInvoiceRepo.save(carInvoice);
    return path;
  }


  @PostMapping(path = "/testdrive/{carid}", produces = "application/json;**charset=UTF-8**")
  public ResponseEntity<?> testDriveRegister(@PathVariable Long carid,
      @RequestBody @Valid CarTestDrive carTestDrive,
      BindingResult bindingResult) throws MessagingException {

    Optional<Car> car = carRepo.findById(carid);
    Optional<CarComplect> carComplect = carComplectRepo.findByEngineId(
        Long.valueOf(carTestDrive.getEngineId()));
    if (car.isPresent() && carComplect.isPresent()) {
      CarTestDrive testDrive = carTestDriveRepo.save(carTestDrive);
      String str = String.format(TEXT, carTestDrive.getFullName(), car.get().getModel(),
          carComplect.get().getEngine().getTitle(), carTestDrive.getEmail(),
          carTestDrive.getNumber());
      String str_client = String.format(TEXT_CLIENT, car.get().getModel());
      emailService.sendSimpleMessage(TO, SUBJECT, str);
      emailService.sendSimpleMessage(new String[]{carTestDrive.getEmail()}, SUBJECT, str_client);
      return ResponseEntity.ok(testDrive);
    }

    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body("ასეთი კომპლექტაცია/მოდელი ვერ მოიძებნა");
  }


}
