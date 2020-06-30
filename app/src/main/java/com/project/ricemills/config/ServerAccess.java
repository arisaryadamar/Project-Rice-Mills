package com.project.ricemills.config;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerAccess {
//    deklarasi untuk link link api yang dibutuhkan di aplikasi
//    public static final String BASE_URL = "http://ricemills.mif-project.com/old/RiceMills/";
    public static final String BASE_URL = "http://192.168.1.9/TA/RiceMilis/";
    public static final String ROOT_API = BASE_URL+"Api/";
    public static final String LOGIN = ROOT_API+"login/";
    public static final String BERAS = ROOT_API+"hasilgiling/";
    public static final String STOK = ROOT_API+"stok/";
    public static final String PEMASOKAN = ROOT_API+"pmsk/";
    public static final String PENGGILINGAN = ROOT_API+"penggilingan/";
    public static final String PENJUALAN = ROOT_API+"penjualan/";
    public static String numberConvert(String val){
        double v = Double.parseDouble(val);
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String format = formatter.format(v);
        return "Rp "+format;
    }
    public static String dateFormat(String date){
//        String date ="29/07/13";
        SimpleDateFormat input = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
        String str = null;
        try {
            Date oneWayTripDate = input.parse(date);                 // parse input
            str = output.format(oneWayTripDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

}
