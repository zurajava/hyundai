package com.web.hyundai.service;


import com.web.hyundai.model.FileUpload;
import com.web.hyundai.model.Path;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Objects;
import java.util.UUID;

@Service
public class ImageService {


    public String generateUUID() {
        return UUID.randomUUID().toString().split("-")[4];
    }


    public String uploadNewDir(MultipartFile file, String folder) throws IOException {
        File directory = new File(Path.folderPath() + folder.replaceAll(" ",""));
        System.out.println(directory + "  ++++++++");
        boolean dir = true;
        FileUpload img = null;
        if (!directory.exists()) dir = directory.mkdirs();
        if (dir) return folder + uploadImage(file, folder).getFile().getName();
        return "null";
    }


    public FileUpload uploadImage(MultipartFile file, String folder) throws IOException {
        String randomId = generateUUID();
        String[] splitedName = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
        String hashedName = splitedName[0] + "-" + randomId + "." + splitedName[1];
        String path = Path.folderPath() + folder + Objects.requireNonNull(hashedName.replace(" ",""));

        File serverFile = new File(path);
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
        stream.write(file.getBytes());
        stream.close();

        FileUpload fileUpload = new FileUpload();
        fileUpload.setFile(serverFile);
        fileUpload.setSplitedName(splitedName[1]);
        fileUpload.setRandomId(randomId);

        return fileUpload;
    }


    public String removeDoubleSlash(String path) {
        return path.replaceAll("\\\\\\\\", "\\\\");
    }



    public String thumbnail(String path, String name, String randomId, String ext, String folder) throws IOException {

        name = name.replace(" ","".replace(" ",""));
        String newPath = folder + "thumbnail-" + name.split("\\.")[0] + "-" + randomId + "." + ext;


        BufferedImage img = new BufferedImage(674, 485, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphic = img.createGraphics();

        graphic.drawImage(ImageIO.read(new File(path))
                .getScaledInstance(674, 485, Image.SCALE_SMOOTH), 0, 0, null);
        ImageIO.write(img, "jpg", new File(Path.folderPath() + newPath));

        graphic.dispose();

        return newPath;
    }



    // https://stackoverflow.com/questions/1069095/how-do-you-create-a-thumbnail-image-out-of-a-jpeg-in-java
    public String thumbnailFromImage(MultipartFile file, String folder) throws IOException {

        String[] nameArray = file.getOriginalFilename().split("\\.");


        String newPath = folder + "thumbnail-" + nameArray[0].replace(" ","") + "-parse-" + generateUUID() + "." + nameArray[1];


        BufferedImage img = new BufferedImage(536, 363, BufferedImage.TYPE_INT_RGB);
        img.createGraphics().drawImage(ImageIO.read(file.getInputStream()).getScaledInstance(260, 260, Image.SCALE_SMOOTH), 0, 0, null);
        ImageIO.write(img, "jpg", new File(Path.folderPath() + newPath));


        return newPath;
    }


    public void uploadFile(String folder, MultipartFile file) throws IOException {
        File serverFile = new File(Path.folderPath() + folder + file.getOriginalFilename().replace(" ",""));
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
        stream.write(file.getBytes());
        stream.close();

    }


    public void deleteFile(String imageName) {
        File oldFile = new File(Path.folderPath() + imageName);
        if (oldFile.exists()) oldFile.delete();
    }


}
