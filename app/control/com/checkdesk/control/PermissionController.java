	/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control;

import com.checkdesk.model.data.Permission;
import com.checkdesk.model.data.User;
import com.checkdesk.model.util.ServerRequest;
import com.checkdesk.views.util.PermissionItem;
import com.checkdesk.model.data.Group;
import com.checkdesk.model.db.service.EntityService;
import com.checkdesk.model.util.Parameter;
import javafx.scene.control.TreeItem;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author arthu
 */
public class PermissionController
{
    private static PermissionController defaultInstance;

    public static PermissionController getInstance()
    {
        if (defaultInstance == null)
        {
            defaultInstance = new PermissionController();
        }

        return defaultInstance;
    }
    
    private TreeItem rootItem = new TreeItem(new PermissionItem("", "Permissões"));

    private PermissionController()
    {
        initialize();
    }
    
    private void initialize()
    {
        try
        {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            Document document = builder.parse(ResourceLocator.getInstance().getConfigStream("permissions.xml"));
            
            Node root = document.getDocumentElement();
            loadTreeChildren(root, rootItem);
        }
        
        catch (Exception e)
        {
            ApplicationController.logException(e);
        }
    }
    
    private void loadTreeChildren(Node node, TreeItem parent)
    {
        if (node.getNodeType() == Node.ELEMENT_NODE)
        {
            Element element = (Element) node;
            
            TreeItem child = parent;
            
            if (element.hasAttribute("label"))
            {
                child = new TreeItem(new PermissionItem(element.getAttribute("name"), element.getAttribute("label")));
                parent.getChildren().add(child);
            }
            
            for (int i = 0; i < node.getChildNodes().getLength(); i++)
            {
                loadTreeChildren(node.getChildNodes().item(i), child);
            }
        }
    }
    
    public TreeItem getRootItem()
    {
        return rootItem;
    }

    public String getUserTypes(String permissionName)
    {
        String result = "";
        
        try
        {
            result = (String) ServerConnection.getInstance().say(new ServerRequest().setRequest(ServerRequest.PERMISSION)
                                                                                    .addParameter("permissionName", permissionName)
                                                                                    .setWaitResponse(true));
        }
        
        catch (Exception e)
        {
            ApplicationController.logException(e);
        }
        
        return result;
    }
    
    public boolean hasPermission(User user, String role)
    {
        boolean result = false;
        
        try
        {
            result = (boolean) ServerConnection.getInstance().say(new ServerRequest().setRequest(ServerRequest.PERMISSION)
                                                                                     .addParameter("user", user)
                                                                                     .addParameter("role", role)
                                                                                     .setWaitResponse(true));
        }
        
        catch (Exception e)
        {
            ApplicationController.logException(e);
        }
        
        return result;
    }
    
    public Permission getPermission(PermissionItem item)
    {
        Permission result = null;
        
        try
        {
            result = (Permission) EntityService.getInstance().getValue(Permission.class, new Parameter(Permission.class.getDeclaredField("name"),
                                                                                                       item.getName(),
                                                                                                       Parameter.COMPARATOR_EQUALS));
            
            if (result == null)
            {
                Group group = new Group();
                EntityService.getInstance().insert(group);
                
                result = new Permission();
                result.setViewersId(group.getGroupId());
                result.setName(item.getName());
                
                EntityService.getInstance().insert(result);
            }
        }
        
        catch (Exception e)
        {
            ApplicationController.logException(e);
        }
        
        return result;
    }
}