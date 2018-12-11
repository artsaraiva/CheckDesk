/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.parts;

import com.checkdesk.control.util.AnswerUtilities;
import com.checkdesk.model.data.Question;
import com.checkdesk.model.util.QuestionWrapper;
import com.checkdesk.views.util.Validation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 *
 * @author MNicaretta
 */
public class AnswerItem
        extends VBox
{
    private QuestionWrapper source;
    private Validation validation;
    private Node node;
    
    public AnswerItem(QuestionWrapper item, String initialValue)
    {
        setSource(item, initialValue);
    }
    
    private void setSource(QuestionWrapper item, String initialValue)
    {
        this.source = item;
        
        Question question = source.getQuestion();
        
        if (question.getType() != Question.TYPE_CATEGORY)
        {
            getChildren().add(new Label(question.getName()));
        }
        
        node = AnswerUtilities.nodeFor(question, initialValue);
        
        getChildren().add(node);
        validation = AnswerUtilities.validationFor(question, node);
    }

    public boolean isValid()
    {
        if (validation != null)
        {
            return validation.isValid();
        }
        
        return true;
    }
    
    public String getValue()
    {
        return AnswerUtilities.valueFor(source.getQuestion(), node);
    }
    
    public Question getQuestion()
    {
        return source.getQuestion();
    }
}
