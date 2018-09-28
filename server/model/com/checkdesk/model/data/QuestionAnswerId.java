package com.checkdesk.model.data;
// Generated Aug 11, 2018 4:12:55 PM by Hibernate Tools 4.3.1

/**
 * QuestionAnswersId generated by hbm2java
 */
public class QuestionAnswerId
        implements java.io.Serializable
{
    private int questionId;
    private int answerId;

    public QuestionAnswerId()
    {
    }

    public QuestionAnswerId(int questionId, int answerId)
    {
        this.questionId = questionId;
        this.answerId = answerId;
    }

    public int getQuestionId()
    {
        return this.questionId;
    }

    public void setQuestionId(int questionId)
    {
        this.questionId = questionId;
    }

    public int getAnswerId()
    {
        return this.answerId;
    }

    public void setAnswerId(int answerId)
    {
        this.answerId = answerId;
    }

    @Override
    public boolean equals(Object other)
    {
        if ((this == other))
        {
            return true;
        }
        if ((other == null))
        {
            return false;
        }
        if (!(other instanceof QuestionAnswerId))
        {
            return false;
        }
        QuestionAnswerId castOther = (QuestionAnswerId) other;

        return (this.getQuestionId() == castOther.getQuestionId()) &&
               (this.getAnswerId() == castOther.getAnswerId());
    }

    @Override
    public int hashCode()
    {
        int result = 17;

        result = 37 * result + this.getQuestionId();
        result = 37 * result + this.getAnswerId();
        return result;
    }
}
