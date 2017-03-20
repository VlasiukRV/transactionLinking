package vr.com.apps.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import vr.com.apps.transactionLinking.EMatchingOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static Date parseDate(String stringDate, String stringDateFormat){
        SimpleDateFormat format = new SimpleDateFormat(stringDateFormat);
        Date date = new Date(0);
        try {
            date = format.parse(stringDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static EMatchingOptions matchId(String currentValue, String regex){
        Pattern p = Pattern.compile("P-?\\d+");
        Matcher m = p.matcher(currentValue);
        if (m.find()){
            String currentId = m.group();
            EMatchingOptions matchingRes = matchString(regex, "(" + currentId + ")");
            if (matchingRes != EMatchingOptions.NOMATCES){
                if (currentId.length() == regex.length()){
                    return EMatchingOptions.FULL;
                }else {
                    return EMatchingOptions.PARTLY;
                }
            }
        }
        return EMatchingOptions.NOMATCES;
    }

    public static EMatchingOptions matchString(String currentValue, String regex){
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(currentValue);
        if (m.find()){
            return EMatchingOptions.FULL;
        }
        return EMatchingOptions.NOMATCES;
    }

    public static String getJSONOfObject(Object o){
        ObjectMapper mapper = new ObjectMapper();
        String JSONOfObject = null;
        try {
            JSONOfObject = mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return JSONOfObject;
    }

}
