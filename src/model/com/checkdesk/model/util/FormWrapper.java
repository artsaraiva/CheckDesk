/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.util;

import com.checkdesk.control.util.FormUtilities;
import com.checkdesk.model.data.Form;
import com.checkdesk.model.data.Question;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MNicaretta
 */
public class FormWrapper
{
    private Form form;
    private List<QuestionWrapper> questions;

    public FormWrapper(Form form)
    {
        this(form, FormUtilities.getQuestions(form));
    }

    public FormWrapper(Form form, List<Question> questions)
    {
        this.form = form;
        this.questions = new ArrayList<>();
        
        for (Question question : questions)
        {
            this.questions.add(new QuestionWrapper(question));
        }
    }

    public Form getForm()
    {
        return form;
    }

    public void setForm(Form form)
    {
        this.form = form;
    }

    public List<QuestionWrapper> getQuestions()
    {
        return questions;
    }

    public void setQuestions(List<QuestionWrapper> questions)
    {
        this.questions = questions;
    }
}
