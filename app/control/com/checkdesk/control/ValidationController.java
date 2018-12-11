/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control;

import com.checkdesk.views.parts.DatePicker;
import com.checkdesk.views.util.Validation;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.control.TextField;

/**
 *
 * @author MNicaretta
 */
public class ValidationController
{
    public static Validation addValidation(final TextField field)
    {
        return new Validation(field)
        {
            @Override
            public String validate()
            {
                String error = "";
                
                if (field.getText() == null || field.getText().isEmpty())
                {
                    error = "Esse campo deve ser preenchido";
                }
                
                return error;
            }
        };
    }
    
    public static Validation addValidation(final DatePicker field)
    {
        return addValidation(field, null, null);
    }
    
    public static Validation addValidation(final DatePicker field, Date min, Date max)
    {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        
        return new Validation(field)
        {
            @Override
            public String validate()
            {
                String error = "";
                
                if (field.getDate() == null)
                {
                    error = "Esse campo deve ser preenchido";
                }
                
                else
                {
                    if (min != null && field.getDate().before(min))
                    {
                        error = "A data mínima é " + df.format(min);
                    }
                    
                    else if (max != null && field.getDate().after(max))
                    {
                        error = "A data máxima é " + df.format(max);
                    }
                }
                
                return error;
            }
        };
    }
}
