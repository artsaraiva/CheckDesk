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
    private Integer optionId;
    private String name;
    private int type;
    private String constraints;
    private Integer parentId;
    private int position;

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

    public Integer getOptionId()
    {
        return this.optionId;
    }

    public void setOptionId(Integer optionId)
    {
        this.optionId = optionId == null || optionId == 0 ? null : optionId;
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

    public Integer getParentId()
    {
        return parentId;
    }

    public void setParentId(Integer parentId)
    {
        this.parentId = parentId == null || parentId == 0 ? null : parentId;
    }

    public int getPosition()
    {
        return position;
    }

    public void setPosition(int position)
    {
        this.position = position;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
