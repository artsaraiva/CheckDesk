/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.util;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 *
 * @author MNicaretta
 */
public class Parameter
        implements Serializable
{
    public static final int COMPARATOR_EQUALS     = 0;
    public static final int COMPARATOR_LOWER_CASE = 1;
    public static final int COMPARATOR_DATE       = 2;
    public static final int COMPARATOR_UNLIKE     = 3;
    public static final int COMPARATOR_DATE_FROM  = 4;
    public static final int COMPARATOR_DATE_UNTIL = 5;
    public static final int COMPARATOR_MAX_VALUE  = 6;
    public static final int COMPARATOR_MIN_VALUE  = 7;

    private String field;
    private Object value;
    private int comparator;

    public Parameter(Field field, Object value, int comparator) throws Exception
    {
        this.field = field.getName();
        this.value = value;
        this.comparator = comparator;
    }

    public String getField()
    {
        return field;
    }

    public Object getValue()
    {
        return value;
    }

    public int getComparator()
    {
        return comparator;
    }
}
