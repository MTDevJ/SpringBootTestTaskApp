package com.test.springBootApp;

import java.util.HashMap;
import java.util.Map;

public class ErrorMessageClass {

    private static final Map<String,String> errorMessages = new HashMap<String,String>(){{
        put("0",null);
        put("1","Элемент с данным наименованием уже имеется в базе!");
        put("2","Элемент с данным id отсутствует в базе!");
        put("3","Недопустимый формат файла, либо отсутствует имя файла!");
        put("4","Ошибка удаления файла!");
    }};

    public static String getErrorMessage(String id) {
        return errorMessages.get(id);
    }
}
