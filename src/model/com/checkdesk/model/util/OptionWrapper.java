/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.util;

import com.checkdesk.control.util.FormUtilities;
import com.checkdesk.model.data.Option;
import com.checkdesk.model.data.OptionItem;
import java.util.List;

/**
 *
 * @author MNicaretta
 */
public class OptionWrapper
{
    private Option option;
    private List<OptionItem> items;

    public OptionWrapper(Option option)
    {
        this(option, FormUtilities.getOptionItems(option));
    }

    public OptionWrapper(Option option, List<OptionItem> items)
    {
        this.option = option;
        this.items = items;
    }

    public Option getOption()
    {
        return option;
    }

    public void setOption(Option option)
    {
        this.option = option;
    }

    public List<OptionItem> getItems()
    {
        return items;
    }

    public void setItems(List<OptionItem> items)
    {
        this.items = items;
    }
}
