/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.util;

import java.lang.reflect.Field;

/**
 *
 * @author MNicaretta
 */
public class Parameter
{
    public static final int COMPARATOR_EQUALS = 0;
    public static final int COMPARATOR_LOWER_CASE = 1;
    
    private final String key;
    private final Field field;
    private final Object value;
    private final int comparator;

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
        return value;
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
        }
        
        return result;
    }
}
