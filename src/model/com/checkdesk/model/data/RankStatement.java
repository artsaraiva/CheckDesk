package com.checkdesk.model.data;
// Generated Aug 11, 2018 4:12:55 PM by Hibernate Tools 4.3.1

import java.math.BigDecimal;

/**
 * RankStatements generated by hbm2java
 */
public class RankStatement
        implements java.io.Serializable
{
    private RankStatementId id;
    private Answer answer;
    private User user;
    private BigDecimal score;

    public RankStatement()
    {
    }

    public RankStatement(RankStatementId id, Answer answer, User user, BigDecimal score)
    {
        this.id = id;
        this.answer = answer;
        this.user = user;
        this.score = score;
    }

    public RankStatementId getId()
    {
        return this.id;
    }

    public void setId(RankStatementId id)
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

    public User getUser()
    {
        return this.user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public BigDecimal getScore()
    {
        return this.score;
    }

    public void setScore(BigDecimal score)
    {
        this.score = score;
    }
}