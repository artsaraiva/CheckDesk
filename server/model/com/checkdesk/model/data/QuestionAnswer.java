package com.checkdesk.model.data;
// Generated Aug 11, 2018 4:12:55 PM by Hibernate Tools 4.3.1

/**
 * QuestionAnswers generated by hbm2java
 */
public class QuestionAnswer
        implements java.io.Serializable
{
    private QuestionAnswerId id;
    private Answer answer;
    private Question question;
    private String value;

    public QuestionAnswer()
    {
    }

    public QuestionAnswer(QuestionAnswerId id, Answer answer, Question question, String value)
    {
        this.id = id;
        this.answer = answer;
        this.question = question;
        this.value = value;
    }

    public QuestionAnswerId getId()
    {
        return this.id;
    }

    public void setId(QuestionAnswerId id)
    {
        this.id = id;
    }

    public Answer getAnswer()
    {
        return this.answer;
    }

    public void setAnswer(Answer answer)
    {
        this.answer = answer;
    }

    public Question getQuestion()
    {
        return this.question;
    }

    public void setQuestion(Question question)
    {
        this.question = question;
    }

    public String getValue()
    {
        return this.value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
}
