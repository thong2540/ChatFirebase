package com.example.chatfirebase.util;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//        "yyyy.MM.dd G 'at' HH:mm:ss z" ---- 2001.07.04 AD at 12:08:56 PDT
//        "hh 'o''clock' a, zzzz" ----------- 12 o'clock PM, Pacific Daylight Time
//        "EEE, d MMM yyyy HH:mm:ss Z"------- Wed, 4 Jul 2001 12:08:56 -0700
//        "yyyy-MM-dd'T'HH:mm:ss.SSSZ"------- 2001-07-04T12:08:56.235-0700
//        "yyMMddHHmmssZ"-------------------- 010704120856-0700
//        "K:mm a, z" ----------------------- 0:08 PM, PDT
//        "h:mm a" -------------------------- 12:08 PM
//        "EEE, MMM d, ''yy" ---------------- Wed, Jul 4, '01

public class PatternString {

    public static boolean  isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean checkIdCard(String id) {
        if (id == null) return false;
        int sum = 0;

        id = id.trim();
        if (id.length() != 13) return false;

        for (int i = 0; i < 12; i++) {
            sum += Integer.valueOf(id.substring(i, i + 1)) * (13 - i);
        }

        if (11 - (sum % 11) >= 10) {
            return (11 - (sum % 11)) - 10 == Integer.valueOf(id.substring(12, 13));
        } else {
            return 11 - (sum % 11) == Integer.valueOf(id.substring(12, 13));
        }
    }

    public static String setWeightFormat(String strPrice) {

        DecimalFormat decimalFormat = new DecimalFormat("#,##0.000");
        String result = decimalFormat.format(Double.parseDouble(strPrice.replace(",", "")));
        Log.d("result", "" + result);

        return result;
    }

    public static String setWeightFormat(Double strPrice) {

        DecimalFormat decimalFormat = new DecimalFormat("#,##0.000");
        String result = decimalFormat.format(strPrice);
        Log.d("result", "" + result);

        return result;
    }

    public static String setPriceFormat(Double strPrice) {

        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        String result = decimalFormat.format(strPrice);
        Log.d("result", "" + result);

        return result;
    }

    public static String setPriceFormat(String strPrice) {

        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        String result = decimalFormat.format(Double.parseDouble(strPrice.replace(",", "")));
        Log.d("result", "" + result);

        return result;
    }

    public static boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

    public static String getStringDateFromYMD(int year, int month, int dayofmonth){
        String sDay = String.valueOf(dayofmonth);
        String sMonth = String.valueOf(month+1);
        String sYear = String.valueOf(year);
        if(sDay.length()<2){
            sDay = "0"+sDay;
        }
        if(sMonth.length()<2){
            sMonth = "0"+sMonth;
        }
        return sDay+"/"+sMonth+"/"+sYear;
    }

    public static String getDateFormat(Date date, String dateFormat){
        return new SimpleDateFormat(dateFormat).format(date);
    }

    public static String getDateFromString(String input, String inputDateFormat, String outputFormat) throws ParseException {
        return getDateFormat(new SimpleDateFormat(inputDateFormat).parse(input),outputFormat);
    }
    
    public static String checkNullJavaString(String input){
        if(input == null){
            return "";
        }else if(input.equals("null") || input.equals("Null")){
            return "";
        }else{
            return input;
        }
    }

    public static String checkDateTimeFormat(String input){
        if(!input.isEmpty()) {
            String DateTime[] = input.split("\\.");
            if (DateTime.length == 2) { //check ว่ามีเศษของเวลาไหม
                try {
                    return getDateFromString(input, "yyyy-MM-dd'T'HH:mm:ss.SS", "dd/MM/yyyy HH:mm:ss");
                } catch (ParseException e) {
                    e.printStackTrace();
                    return "";
                }
            } else {
                try {
                    return getDateFromString(input, "yyyy-MM-dd'T'HH:mm:ss", "dd/MM/yyyy");
                } catch (ParseException e) {
                    e.printStackTrace();
                    return "";
                }
            }
        }else{
            return "";
        }
    }

    public static String getStringFormatFromTimeString(int hour,int minutes){
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),hour,minutes);
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(calendar.getTime());
    }

}
