package com.web.hyundai.googlesheet;


import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.web.hyundai.model.usedcars.UsedCar;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import static com.web.hyundai.googlesheet.GoogleAuthorizeUtil.APPLICATION_NAME;

@Service
public class SpreadSheetService {


    final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

    Sheets service = new Sheets.Builder(HTTP_TRANSPORT, GoogleAuthorizeUtil.JSON_FACTORY,
            GoogleAuthorizeUtil.getCredentials(HTTP_TRANSPORT))
            .setApplicationName(APPLICATION_NAME)
            .build();

    public SpreadSheetService() throws GeneralSecurityException, IOException {
    }


    // google oauth2 ginda tavisi kredentialebit da shemdeg terminalshi gamotanil linkze unda mivcet verifikacia
    public boolean writeToSheet(String spreadsheetId,List<List<Object>> values) throws IOException {

        String range = "Sheet1";

        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();

        ValueRange body = new ValueRange()
                .setValues(values);
        UpdateValuesResponse result =
                service.spreadsheets().values().update(spreadsheetId, range, body)
                        .setValueInputOption("RAW")
                        .execute();
        System.out.printf("%d cells updated.", result.getUpdatedCells());
        return true;


    }

    public String createSheet(String name) throws IOException {

        Spreadsheet spreadsheet = new Spreadsheet()
                .setProperties(new SpreadsheetProperties()
                        .setTitle(name));
        spreadsheet = service.spreadsheets().create(spreadsheet)
                .setFields("spreadsheetId")
                .execute();
        return spreadsheet.getSpreadsheetId();
    }




}
