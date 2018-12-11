/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.util;

import com.checkdesk.control.util.FormUtilities;
import com.checkdesk.model.data.Form;
import com.checkdesk.model.data.Question;
import java.util.List;
import java.util.Map;

/**
 *
 * @author MNicaretta
 */
public class FormWrapper
{
    private Form form;
    private Map<QuestionWrapper, List<QuestionWrapper>> questions;
    private int type;

    public FormWrapper(Form form)
    {
        this(form, FormUtilities.getQuestions(form));
    }

    public FormWrapper(Form form, List<Question> questions)
    {
        this.form = form;
        this.questions = FormUtilities.questionListToMap(questions);
    }
    
    public Form getForm()
    {
        return form;
    }

    public void setForm(Form form)
    {
        this.form = form;
    }

    public Map<QuestionWrapper, List<QuestionWrapper>> getQuestions()
    {
        return questions;
    }

    public void setQuestions(Map<QuestionWrapper, List<QuestionWrapper>> questions)
    {
        this.questions = questions;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }
}
