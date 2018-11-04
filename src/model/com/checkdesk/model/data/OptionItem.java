package com.checkdesk.model.data;

/**
 * OptionItem
 */
public class OptionItem
        extends Entity
{
    private int optionId;
    private String name;
    private String value;

    public OptionItem()
    {
    }

    public int getOptionId()
    {
        return this.optionId;
    }

    public void setOptionId(int optionId)
    {
        this.optionId = optionId;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getValue()
    {
        return this.value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
}
