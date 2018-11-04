package com.checkdesk.model.data;

import java.io.File;

/**
 * Files
 */
public class Attachment
        extends Entity
{
    private int questionId;
    private String name;
    private String type;

    private File file;

    public Attachment()
    {
    }

    public int getQuestionId()
    {
        return this.questionId;
    }

    public void setQuestionId(int questionId)
    {
        this.questionId = questionId;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public File getAttachment()
    {
        return file;
    }

    public void setAttachment(File file)
    {
        this.file = file;
    }

    @Override
    public String toString()
    {
        return name + type;
    }
}
