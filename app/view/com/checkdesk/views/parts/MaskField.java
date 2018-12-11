/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.parts;

import com.checkdesk.control.util.UserUtilities;
import com.checkdesk.views.util.Validation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

/**
 *
 * @author MNicaretta
 */
public class MaskField
        extends TextField
{
    public static final int MASK_NONE = -1;
    public static final int MASK_PHONE = 0;

    private int mode;
    private Validation validation;

    public MaskField()
    {
        this(MASK_NONE);
    }

    public MaskField(int m)
    {
        this.mode = m;
        this.validation = new MaskValidation();

        lengthProperty().addListener((ObservableValue<? extends Number> ov, Number oldLenght, Number newLenght) ->
        {
            String result = "";
            
            switch (mode)
            {
                case MASK_PHONE:
                {
                    result = UserUtilities.maskPhone(getText());
                }
                break;
            }

            setText(result);
            
            positionCaret(newLenght.intValue());
        });
    }

    public int getLimit()
    {
        int result = 200;

        switch (mode)
        {
            case MASK_PHONE:
                result = 15; // (__) _____-____
                break;
        }

        return result;
    }

    public String getValue()
    {
        String result = getText();

        switch (mode)
        {
            case MASK_PHONE:
            {
                result = result.replaceAll("[^0-9]", "");
            }
            break;
        }

        return result;
    }
    
    public Validation getValidation()
    {
        return validation;
    }
    
    private class MaskValidation
            extends Validation
    {
        public MaskValidation()
        {
            super(MaskField.this);
        }
        
        @Override
        public String validate()
        {
            String error = "";

            if (getText() == null || getText().isEmpty())
            {
                error = "Esse campo deve ser preenchido";
            }

            switch (mode)
            {
                case MASK_PHONE:
                    if (error.isEmpty() && getValue().length() < 10)
                    {
                        error = "Esse campo está incompleto";
                    }
                    break;
            }

            return error;
        }
    }
}
