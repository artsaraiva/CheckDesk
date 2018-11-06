	/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.checkdesk.control;

import com.checkdesk.control.util.UserUtilities;
import com.checkdesk.model.data.User;
import com.checkdesk.model.db.service.EntityService;
import com.checkdesk.model.util.ServerRequest;
import java.io.File;
import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            Document document = builder.parse(ResourceLocator.getInstance().getConfigStream("profiles.xml"));
            
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
    
    public Object handleRequest(ServerRequest request)
    {
        Object result = null;
        
        if (request.getParameter("permissionName") != null)
        {
            result = getUserTypes((String) request.getParameter("permissionName"));
        }
        
        else
        {
            result = hasPermission((User) request.getParameter("user"), (String) request.getParameter("role"));
        }
        
        return result;
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

                List<Object[]> result = EntityService.getInstance().getViewValue(Arrays.asList("count(*)"), "user_permissions", parameters );
                long count = 0;
                
                if (!result.isEmpty() && result.get(0) != null && result.get(0).length > 0)
                {
                    count = (long) result.get(0)[0];
                }
                
                hasPermission = count != 0;
            }

            catch (Exception ex)
            {
                ApplicationController.logException(ex);
            }
        }

        return hasPermission;
    }
}