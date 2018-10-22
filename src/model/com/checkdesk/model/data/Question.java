package com.checkdesk.model.data;
// Generated Aug 11, 2018 4:12:55 PM by Hibernate Tools 4.3.1

import java.util.HashSet;
import java.util.Set;

/**
 * Questions generated by hbm2java
 */
public class Question
        implements java.io.Serializable
{
    public static final int TYPE_CATEGORY      = 0;
    public static final int TYPE_SMALL_TEXT    = 1;
    public static final int TYPE_LARGE_TEXT    = 2;
    public static final int TYPE_SINGLE_CHOICE = 3;
    public static final int TYPE_MULTI_CHOICE  = 4;
    public static final int TYPE_DATE          = 5;
    public static final int TYPE_NUMBER        = 6;

    private int id;
    private Form form;
    private Option option;
    private String name;
    private int type;
    private String constraints;
    private Set questionAnswers = new HashSet(0);
    private Set attachments = new HashSet(0);

    public Question()
    {
    }

    public Question(Form form)
    {
        this.form = form;
    }

    public Question(int id, Form form, String name, int type, String constraints)
    {
        this.id = id;
        this.form = form;
        this.name = name;
        this.type = type;
        this.constraints = constraints;
    }

    public Question(int id, Form form, Option option, String name, int type, String constraints, Set questionAnswers, Set files)
    {
        this.id = id;
        this.form = form;
        this.option = option;
        this.name = name;
        this.type = type;
        this.constraints = constraints;
        this.questionAnswers = questionAnswers;
        this.attachments = files;
    }

    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public Form getForm()
    {
        return this.form;
    }

    public void setForm(Form form)
    {
        this.form = form;
    }

    public Option getOption()
    {
        return this.option;
    }

    public void setOption(Option option)
    {
        this.option = option;
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

    public Set getQuestionAnswers()
    {
        return this.questionAnswers;
    }

    public void setQuestionAnswers(Set questionAnswers)
    {
        this.questionAnswers = questionAnswers;
    }

    public Set getAttachments()
    {
        return attachments;
    }

    public void setAttachments(Set attachments)
    {
        this.attachments = attachments;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
