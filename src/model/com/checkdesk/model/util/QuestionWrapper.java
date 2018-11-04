/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.model.util;

import com.checkdesk.control.util.AttachmentUtilities;
import com.checkdesk.model.data.Attachment;
import com.checkdesk.model.data.Question;
import java.util.List;

/**
 *
 * @author MNicaretta
 */
public class QuestionWrapper
{
    private Question question;
    private List<Attachment> attachments;

    public QuestionWrapper(Question question)
    {
        this(question, AttachmentUtilities.getAttachments(question));
    }

    public QuestionWrapper(Question question, List<Attachment> attachments)
    {
        this.question = question;
        this.attachments = attachments;
    }

    public Question getQuestion()
    {
        return question;
    }

    public void setQuestion(Question question)
    {
        this.question = question;
    }

    public List<Attachment> getAttachments()
    {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments)
    {
        this.attachments = attachments;
    }
}
