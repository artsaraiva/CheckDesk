package com.checkdesk.model.data;
// Generated Oct 21, 2018 7:39:33 PM by Hibernate Tools 4.3.1

import java.io.File;


/**
 * Files generated by hbm2java
 */
public class Attachment
        implements java.io.Serializable
{

    private int id;
    private Question question;
    private String name;
    private String type;
    
    private File file;

    public Attachment()
    {
    }

    public Attachment(int id, Question questions, String name)
    {
        this.id = id;
        this.question = questions;
        this.name = name;
    }

    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public Question getQuestion()
    {
        return this.question;
    }

    public void setQuestion(Question question)
    {
        this.question = question;
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
