/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.util;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author MNicaretta
 */
public class Parameter
{
    public static final int COMPARATOR_EQUALS = 0;
    public static final int COMPARATOR_LOWER_CASE = 1;
    public static final int COMPARATOR_DATE = 2;

    private  DateFormat df = DateFormat.getDateInstance();
    
    private String key;
    private Field field;
    private Object value;
    private int comparator;
    
    public Parameter(String key, Field field, Object value, int comparator) throws Exception
    {
        this.key = key;
        this.field = field;
        this.value = value;
        this.comparator = comparator;
    }

    public String getKey()
    {
        return key;
    }

    public Field getField()
    {
        return field;
    }

    public Object getValue()
    {
        Object result = value;

        switch (comparator)
        {
            case COMPARATOR_LOWER_CASE:
                result = value.toString().toLowerCase();
                break;

            case COMPARATOR_DATE:
                if (value instanceof Date)
                {
                    result = df.format((Date) value);
                }
                break;
        }

        return result;
    }

    public String getCondition()
    {
        String result = null;

        switch (comparator)
        {
            case COMPARATOR_EQUALS:
                result = field.getName() + " = :" + key;
                break;

            case COMPARATOR_LOWER_CASE:
                result = "lower(" + field.getName() + ") like :" + key;
                break;

            case COMPARATOR_DATE:
                result = "to_char(" + field.getName() + ", '" + ((SimpleDateFormat) df).toPattern().toUpperCase() + "') = :" + key;
                break;
        }

        return result;
    }
}
