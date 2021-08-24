package com.web.hyundai.model;

public class Path {

    public static final String home = "/home/";
    private static final String CAR_PATH = "/car/";
    public static final String CAR_GALLERY = "/car/modedPhoto/";
    public static final String CAR_PATH_FILES = "/car/files/";
    public static final String CAR_PATH_PHOTOFEATURE = "/car/photofeature/";
    public static final String CAR_PATH_COMFORT = "/car/comfort/";
    public static final String CAR_PATH_SERVICE = "/carservicelogos/";

    private static final String CAR_PATH_PHOTO360 = "/car/photo360/";
    private static final String CAR_PATH_PHOTO360INT = "/car/photo360int/";
    private static final String CAR_PATH_COMPLECT_INTERIER = "/car/complect-interier/";


    public static String getPhoto360(String model,String color){
        return CAR_PATH_PHOTO360 + model +"/" + color + "/";
    }
    public static String getPhoto360Int(String model){
        return CAR_PATH_PHOTO360INT + model +"/";
    }
    public static String getTire(String title){
        return "/car/tires/" + title + "/";
    }
    public static String getCarPath(String model){
        return "/car/" + model + "/";
    }
    public static String getComplectIntPath(){
        return CAR_PATH_COMPLECT_INTERIER;
    }
    public static String folderPath(){
        return System.getProperty("user.dir") + getDir();
    }

    public static String getDir() {
        return "/dodoimage/";
    }

    public static String newsPath(){
        return  "/news/";
    }

    public static String usedCarPath(){
        return  "/usedcars/";

    }

    public static String legalInfoPath(){
        return  "/legalinfo/";

    }

    public static String aboutPath(){
        return  "/about/";

    }
}
