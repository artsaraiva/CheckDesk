	/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control;

import com.checkdesk.control.util.UserUtilities;
import com.checkdesk.model.data.Group;
import com.checkdesk.model.data.Permission;
import com.checkdesk.model.data.User;
import com.checkdesk.model.db.service.EntityService;
import com.checkdesk.model.util.Parameter;
import com.checkdesk.views.util.PermissionItem;
import java.io.File;
import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.control.TreeItem;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
    private Map<String, List<Integer>> defaultPermissions = new HashMap<>();

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
            Document document = builder.parse(new File(new URI(ResourceLocator.getInstance().getConfigResource("permissions.xml"))));
            
            Node root = document.getDocumentElement();
            loadTreeChildren(root, rootItem);
            
            document = builder.parse(new File(new URI(ResourceLocator.getInstance().getConfigResource("profiles.xml"))));
            
            NodeList list = document.getElementsByTagName("type");
            
            for (int i = 0; i < list.getLength(); i++)
            {
                if (list.item(i).getNodeType() == Node.ELEMENT_NODE)
                {
                    loadDefaultPermission((Element) list.item(i));
                }
            }
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
    
    private void loadDefaultPermission(Element element)
    {
        Integer type = Integer.parseInt(element.getAttribute("value"));
        
        for (int i = 0; i < element.getChildNodes().getLength(); i++)
        {
            if (element.getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE)
            {
                Element child = (Element) element.getChildNodes().item(i);
                
                String name = child.getAttribute("name");
                
                List<Integer> types = defaultPermissions.get(name);
                
                if (types == null)
                {
                    defaultPermissions.put(name, types = new ArrayList<>());
                }
                
                types.add(type);
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
        
        if (defaultPermissions.get(permissionName) != null)
        {
            for (Integer i : defaultPermissions.get(permissionName))
            {
                result += UserUtilities.getType(i) + ", ";
            }
        }
        
        if (!result.isEmpty())
        {
            result = result.substring(0, result.lastIndexOf(", "));
        }
        
        return result;
    }
    
    public boolean hasPermission(User user, String role)
    {
        boolean hasPermission = defaultPermissions.containsKey(role) ? defaultPermissions.get(role).contains(user.getType()) : false;
        
        if (!hasPermission)
        {
            try
            {
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("user_id", user.getId());
                parameters.put("permission_name", role);

                BigInteger count = (BigInteger) EntityService.getInstance().getViewValue(Arrays.asList("count(*)"), "user_permissions", parameters );
                hasPermission = count.intValue() == 1;
            }

            catch (Exception ex)
            {
                ApplicationController.logException(ex);
            }
        }

        return hasPermission;
    }
    
    public Permission getPermission(PermissionItem item)
    {
        Permission result = null;
        
        try
        {
            Parameter parameter = new Parameter("name", Permission.class.getDeclaredField("name"), item.getName(), Parameter.COMPARATOR_EQUALS);
            
            result = (Permission) EntityService.getInstance().getValue(Permission.class, Arrays.asList(parameter));
            
            if (result == null)
            {
                Group group = new Group(0, "");
                EntityService.getInstance().save(group);
                
                result = new Permission(0, group, item.getName());
                EntityService.getInstance().save(result);
            }
        }
        
        catch (Exception e)
        {
            ApplicationController.logException(e);
        }
        
        return result;
    }
}