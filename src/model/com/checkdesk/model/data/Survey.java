package com.checkdesk.model.data;

import java.sql.Timestamp;

/**
 * Surveys
 */
public class Survey
        extends Entity
{
    public static final int TYPE_PUBLIC    = 0;
    public static final int TYPE_PRIVATE   = 1;
    public static final int TYPE_ANONYMOUS = 2;
    public static final int TYPE_TOTEM     = 3;
    
    private int id;
    private int categoryId;
    private int formId;
    private int viewersId;
    private int participantsId;
    private int ownerId;
    private String title;
    private String info;
    private Timestamp createdDate;
    private int type;

    public Survey()
    {
    }

    public int getCategoryId()
    {
        return this.categoryId;
    }

    public void setCategoryId(int categoryId)
    {
        this.categoryId = categoryId;
    }

    public int getFormId()
    {
        return this.formId;
    }

    public void setFormId(int formId)
    {
        this.formId = formId;
    }

    public int getViewersId()
    {
        return this.viewersId;
    }

    public void setViewersId(int viewersId)
    {
        this.viewersId = viewersId;
    }

    public int getParticipantsId()
    {
        return this.participantsId;
    }

    public void setParticipantsId(int participantsId)
    {
        this.participantsId = participantsId;
    }

    public int getOwnerId()
    {
        return this.ownerId;
    }

    public void setOwnerId(int ownerId)
    {
        this.ownerId = ownerId;
    }

    public String getTitle()
    {
        return this.title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getInfo()
    {
        return this.info;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }

    public Timestamp getCreatedDate()
    {
        return this.createdDate;
    }

    public void setCreatedDate(Timestamp createdDate)
    {
        this.createdDate = createdDate;
    }

    public int getType()
    {
        return this.type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return title;
    }
}
