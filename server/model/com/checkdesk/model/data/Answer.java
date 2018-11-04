package com.checkdesk.model.data;

import java.sql.Timestamp;

/**
 * Answer
 */
public class Answer
        extends Entity
{
    private int surveyId;
    private int ownerId;
    private String feedback;
    private Timestamp occurredDate;

    public Answer()
    {
    }

    public int getSurveyId()
    {
        return this.surveyId;
    }

    public void setSurveyId(int surveyId)
    {
        this.surveyId = surveyId;
    }

    public int getOwnerId()
    {
        return this.ownerId;
    }

    public void setOwnerId(int ownerId)
    {
        this.ownerId = ownerId;
    }

    public String getFeedback()
    {
        return this.feedback;
    }

    public void setFeedback(String feedback)
    {
        this.feedback = feedback;
    }

    public Timestamp getOccurredDate()
    {
        return this.occurredDate;
    }

    public void setOccurredDate(Timestamp occurredDate)
    {
        this.occurredDate = occurredDate;
    }
}
