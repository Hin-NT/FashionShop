package com.example.FashionShop.utils;

import java.lang.reflect.Field;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class Util {

    public static String createIdForEntity(String id) {
        String noAccentString = Util.removeDiacritics(id);

        return noAccentString.replace(" ", "-").toLowerCase();
    }

    public static String removeDiacritics(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).
                replaceAll("").
                replaceAll("[^\\p{ASCII}]", "");
    }

    public static void trimFields(Object object) {
        if (object == null) return;

        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getType() == String.class) {
                field.setAccessible(true);
                try {
                    String value = (String) field.get(object);
                    if (value != null) {
                        field.set(object, value.trim());
                    }
                } catch (IllegalAccessException e) {
                    System.err.println("Error accessing field: " + field.getName());
                    e.printStackTrace();
                }
            }
        }
    }

}
