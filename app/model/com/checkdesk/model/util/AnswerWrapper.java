/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.util;

import com.checkdesk.control.util.AnswerUtilities;
import com.checkdesk.model.data.Answer;
import com.checkdesk.model.data.Question;
import com.checkdesk.model.data.QuestionAnswer;
import java.util.List;

/**
 *
 * @author MNicaretta
 */
public class AnswerWrapper
{
    private Answer answer;
    private List<QuestionAnswer> questionAnswers;

    public AnswerWrapper(Answer answer)
    {
        this(answer, AnswerUtilities.getQuestionAnswers(answer));
    }

    public AnswerWrapper(Answer answer, List<QuestionAnswer> questionAnswers)
    {
        this.answer = answer;
        this.questionAnswers = questionAnswers;
    }

    public Answer getAnswer()
    {
        return answer;
    }

    public void setAnswer(Answer answer)
    {
        this.answer = answer;
    }

    public List<QuestionAnswer> getQuestionAnswers()
    {
        return questionAnswers;
    }

    public void setQuestionAnswers(List<QuestionAnswer> questionAnswers)
    {
        this.questionAnswers = questionAnswers;
    }
    
    public String getValue(int questionId)
    {
        String result = null;
        
        for (QuestionAnswer qa : getQuestionAnswers())
        {
            if (qa.getQuestionId() == questionId)
            {
                result = qa.getValue();
            }
        }
        
        return result;
    }
}
