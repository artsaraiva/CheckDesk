/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.data;

/**
 *
 * @author MNicaretta
 */
public class QuestionAnswer
        extends Entity
{
    private int answerId;
    private int questionId;
    private String value;

    public QuestionAnswer()
    {
    }

    public int getAnswerId()
    {
        return answerId;
    }

    public void setAnswerId(int answerId)
    {
        this.answerId = answerId;
    }

    public int getQuestionId()
    {
        return questionId;
    }

    public void setQuestionId(int questionId)
    {
        this.questionId = questionId;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    @Override
    public boolean equals(Object source)
    {
        if (source instanceof QuestionAnswer)
        {
            return ((QuestionAnswer) source).getAnswerId() == this.answerId && ((QuestionAnswer) source).getQuestionId() == this.questionId;
        }

        return false;
    }
}
