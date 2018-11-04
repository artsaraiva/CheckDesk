/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control;

import com.checkdesk.control.util.CategoryUtilities;
import com.checkdesk.control.util.FormUtilities;
import com.checkdesk.control.util.GroupUtilities;
import com.checkdesk.control.util.SurveyUtilities;
import com.checkdesk.control.util.UserUtilities;
import com.checkdesk.model.data.Survey;
import com.checkdesk.model.data.User;
import com.checkdesk.model.db.service.EntityService;
import com.checkdesk.model.util.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.StringJoiner;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author MNicaretta
 */
public class NotificationController
{
    public static NotificationController defaultInstance = null;
    
    public static NotificationController getInstance()
    {
        if (defaultInstance == null)
        {
            defaultInstance = new NotificationController();
        }
        
        return defaultInstance;
    }
    
    private NotificationController()
    {
    }
    
    public void sendNotification(Object object) throws Exception
    {
        if ( object instanceof Survey )
        {
            notifySurvey((Survey) object);
        }
    }
    
    private void notifySurvey(Survey survey) throws Exception
    {
        StringBuilder builder = new StringBuilder();
        
        StringJoiner joiner = new StringJoiner("\n");
        
        if (GroupUtilities.getGroup(survey.getParticipantsId()) != null )
        {
            for (User user : GroupUtilities.getGroup(survey.getParticipantsId()).getUsers())
            {
                joiner.add(user.toString());
            }
        }
        
        builder.append("<body>")
               .append("    <h1>")
               .append("        Nova pesquisa criada")
               .append("    </h1>")
               .append("    <h2>")
               .append(         survey.getTitle())
               .append("    </h2>")
               .append("    <table>")
               .append("        <tr>")
               .append("            <td>Data de criação:</td>")
               .append("            <td>").append(survey.getCreatedDate()).append("</td>")
               .append("        </tr>")
               .append("        <tr>")
               .append("            <td>Tipo:</td>")
               .append("            <td>").append(SurveyUtilities.getType(survey.getType())).append("</td>")
               .append("        </tr>")
               .append("        <tr>")
               .append("            <td>Autor:</td>")
               .append("            <td>").append(UserUtilities.getUser(survey.getOwnerId())).append("</td>")
               .append("        </tr>")
               .append("        <tr>")
               .append("            <td>Categoria:</td>")
               .append("            <td>").append(CategoryUtilities.getCategory(survey.getCategoryId())).append("</td>")
               .append("        </tr>")
               .append("        <tr>")
               .append("            <td>Participantes:</td>")
               .append("            <td>").append(joiner.toString()).append("</td>")
               .append("        </tr>")
               .append("        <tr>")
               .append("            <td>Informações:</td>")
               .append("            <td>").append(survey.getInfo()).append("</td>")
               .append("        </tr>")
               .append("        <tr>")
               .append("            <td>Formulário:</td>")
               .append("            <td>").append(FormUtilities.getForm(survey.getFormId())).append("</td>")
               .append("        </tr>")
               .append("    </table>")
               .append("</body>");
        
        notifyAll("Nova pesquisa criada", builder.toString());
    }
    
    private void notifyAll(String subject, String content) throws Exception
    {
        List<String> emails = UserUtilities.getUserEmails();
        
        send(emails, subject, content);
    }
    
    private void send(List<String> emails, String subject, String content) throws Exception
    {
        // Parâmetros de conexão com servidor Gmail
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        
        Session session = Session.getDefaultInstance(props,
                          new javax.mail.Authenticator() {
                               protected PasswordAuthentication getPasswordAuthentication() 
                               {
                                     return new PasswordAuthentication("checkdesk.a@gmail.com", "owNES6Kep4aA");
                               }
                          });
        
        session.setDebug(true);
        
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("info.checkdesk@gmail.com")); //Remetente

        //Destinatário(s)
        StringJoiner joiner = new StringJoiner(", ");
        
        for ( String email : emails)
        {
            if (email != null && !email.isEmpty())
            {
                joiner.add(email);
            }
        }
        
        Address[] toUser = InternetAddress.parse(joiner.toString());

        message.setRecipients(Message.RecipientType.TO, toUser);
        message.setSubject("CheckDesk - " + subject);//Assunto
        message.setContent(content, "text/html; charset=utf-8");
        
        //Método para enviar a mensagem criada
        Transport.send(message);
    }
}
