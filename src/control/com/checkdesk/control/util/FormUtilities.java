/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control.util;

import com.checkdesk.control.ApplicationController;
import com.checkdesk.model.data.Form;
import com.checkdesk.model.data.Option;
import com.checkdesk.model.data.Question;
import com.checkdesk.model.db.service.EntityService;
import com.checkdesk.model.util.FormWrapper;
import com.checkdesk.model.util.Parameter;
import com.checkdesk.model.util.QuestionWrapper;
import com.checkdesk.views.editors.FormEditor;
import com.checkdesk.views.util.EditorCallback;
import com.checkdesk.views.parts.Prompts;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.event.Event;
import javafx.stage.FileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author MNicaretta
 */
public class FormUtilities
{

    public static final Item TYPE_CATEGORY = new Item("Categoria", Question.TYPE_CATEGORY);
    public static final Item TYPE_SMALL_TEXT = new Item("Texto", Question.TYPE_SMALL_TEXT);
    public static final Item TYPE_LARGE_TEXT = new Item("Texto Grande", Question.TYPE_LARGE_TEXT);
    public static final Item TYPE_SINGLE_CHOICE = new Item("Escolha Simples", Question.TYPE_SINGLE_CHOICE);
    public static final Item TYPE_MULTI_CHOICE = new Item("Escolha Multipla", Question.TYPE_MULTI_CHOICE);
    public static final Item TYPE_DATE = new Item("Data", Question.TYPE_DATE);
    public static final Item TYPE_NUMBER = new Item("Número", Question.TYPE_NUMBER);
    
    public static final FileChooser.ExtensionFilter XML_FILTER = new FileChooser.ExtensionFilter("eXtensible Markup Language file", "*.xml");

    public static void addForm()
    {
        Form form = new Form();

        new FormEditor(new EditorCallback<FormWrapper>(new FormWrapper(form))
        {
            @Override
            public void handle(Event t)
            {
                try
                {
                    saveForm(getSource());
                }

                catch (Exception e)
                {
                    ApplicationController.logException(e);
                }
            }
        }).showAndWait();
    }

    public static void editForm(Form form)
    {
        new FormEditor(new EditorCallback<FormWrapper>(new FormWrapper(form))
        {
            @Override
            public void handle(Event t)
            {
                try
                {
                    saveForm(getSource());
                }

                catch (Exception e)
                {
                    ApplicationController.logException(e);
                }
            }
        }).showAndWait();
    }

    public static void deleteForm(int formId)
    {
        deleteForm(getForm(formId));
    }

    public static void deleteForm(Form form)
    {
        if (Prompts.confirm("Exlusão de Formulário", "Deseja realmente excluir o formulário?"))
        {
            try
            {
                for (Question question : getQuestions(form))
                {
                    EntityService.getInstance().delete(question);
                }

                EntityService.getInstance().delete(form);
            }

            catch (Exception e)
            {
                ApplicationController.logException(e);
            }
        }
    }

    public static void saveForm(FormWrapper formWrapper) throws Exception
    {
        if (formWrapper.getForm().getId() == 0)
        {
            formWrapper.setForm((Form) EntityService.getInstance().save(formWrapper.getForm()));

            for (QuestionWrapper questionWrapper : formWrapper.getQuestions())
            {
                questionWrapper.getQuestion().setFormId(formWrapper.getForm().getId());

                questionWrapper.setQuestion((Question) EntityService.getInstance().save(questionWrapper.getQuestion()));
                AttachmentUtilities.saveAttachments(questionWrapper);
            }
        }

        else
        {
            List<Question> oldQuestions = EntityService.getInstance().getValues(Question.class,
                    new Parameter(Question.class.getDeclaredField("formId"),
                            formWrapper.getForm().getId(),
                            Parameter.COMPARATOR_EQUALS));
            for (Question deletable : oldQuestions)
            {
                boolean delete = true;

                for (QuestionWrapper questionWrapper : formWrapper.getQuestions())
                {
                    if (deletable.equals(questionWrapper.getQuestion()))
                    {
                        delete = false;
                        break;
                    }
                }

                if (delete)
                {
                    AttachmentUtilities.deleteAttachments(deletable);
                    EntityService.getInstance().delete(deletable);
                }
            }

            for (QuestionWrapper questionWrapper : formWrapper.getQuestions())
            {
                if (questionWrapper.getQuestion().getId() == 0)
                {
                    questionWrapper.setQuestion((Question) EntityService.getInstance().save(questionWrapper.getQuestion()));
                }

                else
                {
                    questionWrapper.setQuestion((Question) EntityService.getInstance().update(questionWrapper.getQuestion()));
                }

                AttachmentUtilities.saveAttachments(questionWrapper);
            }

            formWrapper.setForm((Form) EntityService.getInstance().update(formWrapper.getForm()));
        }
    }

    public static void exportForm(Form form)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("formulario" + System.currentTimeMillis());
        fileChooser.getExtensionFilters().add(XML_FILTER);
        File file = fileChooser.showSaveDialog(ApplicationController.getInstance().getRootWindow());

        if (file != null)
        {
            try
            {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.newDocument();

                Element rootElement = doc.createElement("form");
                doc.appendChild(rootElement);

                Element name = doc.createElement("name");
                name.appendChild(doc.createTextNode(form.getName()));
                rootElement.appendChild(name);

                Element info = doc.createElement("info");
                info.appendChild(doc.createTextNode(form.getInfo()));
                rootElement.appendChild(info);

                Element viewersId = doc.createElement("viewersId");
                viewersId.appendChild(doc.createTextNode(String.valueOf(form.getViewersId())));
                rootElement.appendChild(viewersId);

                Element state = doc.createElement("state");
                state.appendChild(doc.createTextNode(String.valueOf(form.getState())));
                rootElement.appendChild(state);

                Element questions = doc.createElement("questions");
                rootElement.appendChild(questions);

                for (Question question : getQuestions(form))
                {
                    Element element = doc.createElement("question");
                    questions.appendChild(element);
                    
                    Attr attrName = doc.createAttribute("name");
                    attrName.setValue(question.getName());
                    element.setAttributeNode(attrName);
                    
                    Attr attrType = doc.createAttribute("type");
                    attrType.setValue(String.valueOf(question.getType()));
                    element.setAttributeNode(attrType);
                    
                    Attr attrConstraints = doc.createAttribute("constraints");
                    attrConstraints.setValue(question.getConstraints());
                    element.setAttributeNode(attrConstraints);
                    
                    Attr attrOptionId = doc.createAttribute("optionId");
                    attrOptionId.setValue(String.valueOf(question.getOptionId()));
                    element.setAttributeNode(attrOptionId);
                    
                    Attr attrState = doc.createAttribute("state");
                    attrState.setValue(String.valueOf(question.getState()));
                    element.setAttributeNode(attrState);
                }

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(file);

                transformer.transform(source, result);
            }
            catch (Exception e)
            {
                ApplicationController.logException(e);
            }
        }
    }
    
    public static FormWrapper importForm(File file)
    {
//        try
//        {
//            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//            Document doc = docBuilder.parse(file);
//
//            NodeList rootElement = doc.getElementsByTagName("form");
//            
//            if(rootElement != null)
//            {
//                
//            }
//
//            Element name = doc.createElement("name");
//            name.appendChild(doc.createTextNode(form.getName()));
//            rootElement.appendChild(name);
//
//            Element info = doc.createElement("info");
//            info.appendChild(doc.createTextNode(form.getInfo()));
//            rootElement.appendChild(info);
//
//            Element viewersId = doc.createElement("viewersId");
//            viewersId.appendChild(doc.createTextNode(String.valueOf(form.getViewersId())));
//            rootElement.appendChild(viewersId);
//
//            Element state = doc.createElement("state");
//            state.appendChild(doc.createTextNode(String.valueOf(form.getState())));
//            rootElement.appendChild(state);
//
//            Element questions = doc.createElement("questions");
//            rootElement.appendChild(questions);
//
//            for (Question question : getQuestions(form))
//            {
//                Element element = doc.createElement("question");
//                questions.appendChild(element);
//
//                Attr attrName = doc.createAttribute("name");
//                attrName.setValue(question.getName());
//                element.setAttributeNode(attrName);
//
//                Attr attrType = doc.createAttribute("type");
//                attrType.setValue(String.valueOf(question.getType()));
//                element.setAttributeNode(attrType);
//
//                Attr attrConstraints = doc.createAttribute("constraints");
//                attrConstraints.setValue(question.getConstraints());
//                element.setAttributeNode(attrConstraints);
//
//                Attr attrOptionId = doc.createAttribute("optionId");
//                attrOptionId.setValue(String.valueOf(question.getOptionId()));
//                element.setAttributeNode(attrOptionId);
//
//                Attr attrState = doc.createAttribute("state");
//                attrState.setValue(String.valueOf(question.getState()));
//                element.setAttributeNode(attrState);
//            }
//
//            TransformerFactory transformerFactory = TransformerFactory.newInstance();
//            Transformer transformer = transformerFactory.newTransformer();
//            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//            DOMSource source = new DOMSource(doc);
//            StreamResult result = new StreamResult(file);
//
//            transformer.transform(source, result);
//        }
//        catch (Exception e)
//        {
//            ApplicationController.logException(e);
//        }
        return null;
    }

    public static Form getForm(int formId)
    {
        Form result = null;

        try
        {
            result = (Form) EntityService.getInstance().getValue(Form.class, formId);
        }

        catch (Exception e)
        {
            ApplicationController.logException(e);
        }

        return result;
    }

    public static List<Form> getForms()
    {
        List<Form> result = new ArrayList<>();

        try
        {
            result = EntityService.getInstance().getValues(Form.class);
        }

        catch (Exception e)
        {
            ApplicationController.logException(e);
        }

        return result;
    }

    public static Item getQuestionType(int questionType)
    {
        Item result = null;

        for (Item i : getQuestionTypes())
        {
            if (i.getValue() == questionType)
            {
                result = i;
                break;
            }
        }

        return result;
    }

    public static List<Item> getQuestionTypes()
    {
        return Arrays.asList(TYPE_CATEGORY,
                TYPE_SMALL_TEXT,
                TYPE_LARGE_TEXT,
                TYPE_SINGLE_CHOICE,
                TYPE_MULTI_CHOICE,
                TYPE_DATE,
                TYPE_NUMBER);
    }

    public static Option getOption(Integer optionId)
    {
        Option result = null;

        if (optionId != null)
        {
            try
            {
                result = (Option) EntityService.getInstance().getValue(Option.class, optionId);
            }

            catch (Exception e)
            {
                ApplicationController.logException(e);
            }
        }

        return result;
    }

    public static List<Option> getOptions()
    {
        List<Option> result = new ArrayList<>();

        try
        {
            result = EntityService.getInstance().getValues(Option.class);
        }

        catch (Exception e)
        {
            ApplicationController.logException(e);
        }

        return result;
    }

    public static List<Question> getQuestions(Form form)
    {
        return getQuestions(form != null ? form.getId() : 0);
    }

    public static List<Question> getQuestions(int formId)
    {
        List<Question> result = new ArrayList();

        if (formId != 0)
        {
            try
            {
                result = EntityService.getInstance().getValues(Question.class,
                        new Parameter(Question.class.getDeclaredField("formId"),
                                formId,
                                Parameter.COMPARATOR_EQUALS));
            }

            catch (Exception e)
            {
                ApplicationController.logException(e);
            }
        }

        return result;
    }
}
