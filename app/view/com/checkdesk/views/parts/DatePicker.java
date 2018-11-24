/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.parts;

import java.time.ZoneId;
import java.util.Date;

/**
 *
 * @author MNicaretta
 */
public class DatePicker
        extends javafx.scene.control.DatePicker
{
    public void setDate(Date date)
    {
        setValue(date != null ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null);
    }

    public Date getDate()
    {
        return getValue() != null ? Date.from(getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;
    }
}
