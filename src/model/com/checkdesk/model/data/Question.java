package com.checkdesk.model.data;

/**
 * Questions
 */
public class Question
        extends Entity
{
    public static final int TYPE_CATEGORY      = 0;
    public static final int TYPE_SMALL_TEXT    = 1;
    public static final int TYPE_LARGE_TEXT    = 2;
    public static final int TYPE_SINGLE_CHOICE = 3;
    public static final int TYPE_MULTI_CHOICE  = 4;
    public static final int TYPE_DATE          = 5;
    public static final int TYPE_NUMBER        = 6;

    private int formId;
    private int optionId;
    private String name;
    private int type;
    private String constraints;

    public Question()
    {
    }

    public int getFormId()
    {
        return this.formId;
    }

    public void setFormId(int formId)
    {
        this.formId = formId;
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

    public int getType()
    {
        return this.type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getConstraints()
    {
        return this.constraints;
    }

    public void setConstraints(String constraints)
    {
        this.constraints = constraints;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
