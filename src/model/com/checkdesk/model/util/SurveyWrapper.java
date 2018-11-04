/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.util;

import com.checkdesk.control.util.FormUtilities;
import com.checkdesk.model.data.Form;
import com.checkdesk.model.data.Survey;

/**
 *
 * @author MNicaretta
 */
public class SurveyWrapper
{
    private Survey survey;
    private FormWrapper formWrapper;

    public SurveyWrapper(Survey survey)
    {
        this(survey, FormUtilities.getForm(survey.getFormId()));
    }

    public SurveyWrapper(Survey survey, Form form)
    {
        this.survey = survey;
        this.formWrapper = new FormWrapper(form);
    }

    public Survey getSurvey()
    {
        return survey;
    }

    public void setSurvey(Survey survey)
    {
        this.survey = survey;
    }

    public FormWrapper getForm()
    {
        return formWrapper;
    }

    public void setForm(FormWrapper form)
    {
        this.formWrapper = form;
    }
}
