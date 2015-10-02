package main.java.scripter.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by holly on 30/09/2015.
 */
public class StringUtils
{
    public static String getTodaysDate()
    {
        String today = "";
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        today = format.format(cal.getTime());
        return today;
    }

    public static String removeLeadingWhiteSpace(String value)
    {
        StringBuilder builder = new StringBuilder();
        if(isValidString(value))
        {
            boolean hasFoundCharacter = false;
            for (int i = 0; i < value.length(); i++)
            {
                if(!Character.isWhitespace(value.charAt(i)))
                {
                    hasFoundCharacter = true;
                    builder.append(value.charAt(i));
                }
                else if(hasFoundCharacter)
                {
                    builder.append(value.charAt(i));
                }
            }
        }
        return builder.toString().trim();
    }

    public static boolean isValidString(String value)
    {
        if(value != null)
        {
            String s = value.trim();
            return s.length() > 0;
        }
        return false;
    }

    public boolean isStringNumeric(String value)
    {
        if(isValidString(value))
        {
            String s = value.trim();
            for (int i = 0; i < s.length(); i++)
            {
                if(!Character.isDigit(s.charAt(i)))
                {
                    return false;
                }
            }
        }
        return true;
    }
}

