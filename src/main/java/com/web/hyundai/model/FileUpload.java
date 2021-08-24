package com.web.hyundai.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileUpload {
    File file;
    String randomId;
    String splitedName;

}
