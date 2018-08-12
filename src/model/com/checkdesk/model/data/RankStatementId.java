package com.checkdesk.model.data;
// Generated Aug 11, 2018 4:12:55 PM by Hibernate Tools 4.3.1

/**
 * RankStatementsId generated by hbm2java
 */
public class RankStatementId
        implements java.io.Serializable
{
    private int answerId;
    private int userId;

    public RankStatementId()
    {
    }

    public RankStatementId(int answerId, int userId)
    {
        this.answerId = answerId;
        this.userId = userId;
    }

    public int getAnswerId()
    {
        return this.answerId;
    }

    public void setAnswerId(int answerId)
    {
        this.answerId = answerId;
    }

    public int getUserId()
    {
        return this.userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
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
        if (!(other instanceof RankStatementId))
        {
            return false;
        }
        RankStatementId castOther = (RankStatementId) other;

        return (this.getAnswerId() == castOther.getAnswerId()) &&
               (this.getUserId() == castOther.getUserId());
    }

    @Override
    public int hashCode()
    {
        int result = 17;

        result = 37 * result + this.getAnswerId();
        result = 37 * result + this.getUserId();
        return result;
    }
}