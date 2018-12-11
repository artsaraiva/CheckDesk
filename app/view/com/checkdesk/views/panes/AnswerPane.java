/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.views.panes;

import com.checkdesk.control.ResourceLocator;
import com.checkdesk.control.util.AnswerUtilities;
import com.checkdesk.control.util.FormUtilities;
import com.checkdesk.control.util.SurveyUtilities;
import com.checkdesk.model.data.Answer;
import com.checkdesk.model.data.QuestionAnswer;
import com.checkdesk.model.data.Survey;
import com.checkdesk.model.util.AnswerWrapper;
import com.checkdesk.model.util.QuestionWrapper;
import com.checkdesk.views.details.util.DetailsCaption;
import com.checkdesk.views.parts.AnswerItem;
import com.checkdesk.views.parts.Prompts;
import com.checkdesk.views.util.Callback;
import com.checkdesk.views.util.EditorButton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 *
 * @author MNicaretta
 */
public class AnswerPane
        extends DefaultPane
{
    private Callback<AnswerWrapper> callback;
    private Map<QuestionWrapper, List<QuestionWrapper>> questions;
    
    public AnswerPane(Callback<AnswerWrapper> callback)
    {
        this.callback = callback;
        
        initComponents();
        
        setSource(callback.getSource());
    }
    
    private void setSource(AnswerWrapper wrapper)
    {
        Survey survey = null;
        
        if (wrapper != null)
        {
            survey = SurveyUtilities.getValue(wrapper.getAnswer().getSurveyId());
            
            if (survey != null)
            {
                questions = FormUtilities.questionListToMap(FormUtilities.getQuestions(survey.getFormId()));
                pane.setTop(new DetailsCaption(survey.getTitle()));
            }
        }
        
        if (survey == null || questions == null || questions.isEmpty())
        {
            close();
        }
    }
    
    @Override
    protected void resize()
    {
        pane.setPrefSize(getWidth(), getHeight());
        
        vbox.setPrefWidth(getWidth() - 20);
    }

    @Override
    public void refreshContent()
    {
        if (questions != null && !questions.isEmpty())
        {
            loadQuestions(null, 0, questions);
        }
    }
    
    private void loadQuestions(QuestionWrapper parent, int indentation, Map<QuestionWrapper, List<QuestionWrapper>> questions)
    {
        List<QuestionWrapper> list = questions.get(parent);
        
        if (list != null)
        {
            list.forEach((wrapper) ->
            {
                AnswerItem item = new AnswerItem(wrapper, callback.getSource().getValue(wrapper.getQuestion().getId()));
                item.setPadding(new Insets(0, 0, 0, 50 * indentation));

                vbox.getChildren().add(item);
                loadQuestions(wrapper, indentation + 1, questions);
            });
        }
    }
    
    private boolean validate()
    {
        boolean result = true;
        
        for (Node node : vbox.getChildren())
        {
            if (node instanceof AnswerItem)
            {
                result &= ((AnswerItem) node).isValid();
            }
        }
        
        return result;
    }
    
    private void finish()
    {
        if (validate())
        {
            callback.getSource().getAnswer().setFeedback(Prompts.input("Feedback da pesquisa", "Deixe seu feedback da pesquisa"));
            callback.getSource().getAnswer().setState(Answer.STATE_FINISHED);
            
            save();
            close();
        }
        
        else
        {
            Prompts.info("Existem campos com erros!");
        }
    }
    
    private void save()
    {
        List<QuestionAnswer> questionAnswers = new ArrayList<>();
        
        vbox.getChildren().stream().filter((node) -> (node instanceof AnswerItem)).forEachOrdered((node) ->
        {
            QuestionAnswer questionAnswer = new QuestionAnswer();
            questionAnswer.setQuestionId(((AnswerItem) node).getQuestion().getId());
            questionAnswer.setValue(((AnswerItem) node).getValue());
            
            questionAnswers.add(questionAnswer);
        });
        
        callback.getSource().getQuestionAnswers().clear();
        callback.getSource().setQuestionAnswers(questionAnswers);
        
        if (AnswerUtilities.saveAnswer(callback.getSource()))
        {
            Prompts.info("Resposta salva com sucesso");
        }
    }
    
    private void close()
    {
        callback.handle(null);
    }
    
    private void initComponents()
    {
        getStylesheets().add(ResourceLocator.getInstance().getStyleResource("details.css"));
        
        HBox.setHgrow(vbox, Priority.ALWAYS);
        
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(5));
        
        buttonsPane.setSpacing(5);
        buttonsPane.getChildren().addAll(finishButton, saveButton, closeButton);
        
        pane.setCenter(new ScrollPane(vbox));
        pane.setBottom(new Pane(buttonsPane));
        
        getChildren().add(pane);
    }
    
    private BorderPane pane = new BorderPane();
    private VBox vbox = new VBox();
    private HBox buttonsPane = new HBox();
    
    private EditorButton finishButton = new EditorButton(new ButtonType("Finalizar", ButtonBar.ButtonData.OK_DONE));
    private EditorButton saveButton = new EditorButton(new ButtonType("Salvar", ButtonBar.ButtonData.APPLY));
    private EditorButton closeButton = new EditorButton(new ButtonType("Fechar", ButtonBar.ButtonData.CANCEL_CLOSE));
    {
        finishButton.setOnMouseClicked((MouseEvent t) ->
        {
            finish();
        });
        
        saveButton.setOnMouseClicked((MouseEvent t) ->
        {
            save();
        });
        
        closeButton.setOnMouseClicked((MouseEvent t) ->
        {
            close();
        });
    }
}
