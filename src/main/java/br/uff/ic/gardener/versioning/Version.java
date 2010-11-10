package br.uff.ic.gardener.versioning;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.FileInputStream.*;
import java.io.DataInputStream.*;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;

/**
 *
 * @author Evaldo de Oliveira
 */

public class Version {
   
   private String versionNumber;   
   public  Transaction transaction;
   public  Item item;
  
   /**
    *
    * @param versionNumber
    */

   public void createVersion(String nameConfigurationItem
                      , Project projectItem
                      , int currentVersion
                      , int nextVersion
                      , String name
                      , User user
                      , Transaction trans)
   {
        try{

                item = new Item();
                item.setNameItem(name);

                transaction = trans;

                Element newConfigurationItem = null;
                Element newProjectVersion = null;

                DocumentBuilderFactory logVersion = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = logVersion.newDocumentBuilder();
                Document doc = docBuilder.parse(new File("/gardener/itensVersion.xml"));

                Element root = doc.getDocumentElement();                

                NodeList nodeConfigurationItem = root.getElementsByTagName(item.getNameItem());
                NodeList nodeProjectVersion = root.getElementsByTagName(projectItem.getNameProject());

                Element newVersion = doc.createElement("version");
                
                newVersion.setAttribute("nameItem", item.getNameItem());
                newVersion.setAttribute("numberVersion", "1");                

                Element newTransactionVersion = doc.createElement("transaction");

                newTransactionVersion.setAttribute("dateVersion", transaction.getDate());
                newTransactionVersion.setAttribute("message", transaction.getMessage());
                newTransactionVersion.setAttribute("userTransaction", user.getUserName());

                newVersion.appendChild(newTransactionVersion);

                if (nodeConfigurationItem.getLength() > 0)
                {

                    Element configurationItem = (Element) nodeConfigurationItem.item(0);

                    configurationItem.insertBefore(newVersion, null);

                    newVersion.setAttribute("numberVersion", Integer.toString(currentVersion));

                    configurationItem.setAttribute("stateConfigurationItem", "ci");                    

                    configurationItem.appendChild(newVersion);

                    if (nodeProjectVersion.getLength() > 0)
                    {
                        Element proj = (Element) nodeProjectVersion.item(0);

                        proj.setAttribute("currentVersion", Integer.toString(currentVersion));
                        proj.setAttribute("nextVersion",  Integer.toString(nextVersion));

                        proj.insertBefore(configurationItem, null);
                        root.insertBefore(proj, null);

                    }else{

                        newProjectVersion = doc.createElement(projectItem.getNameProject());

                        newProjectVersion.setAttribute("currentVersion", Integer.toString(currentVersion));
                        newProjectVersion.setAttribute("nextVersion",  Integer.toString(nextVersion));

                        newProjectVersion.appendChild(configurationItem);
                        root.appendChild(newProjectVersion);

                    }

                }else{

                    newConfigurationItem = doc.createElement(nameConfigurationItem);                   

                    if (nodeProjectVersion.getLength() > 0)
                    {

                        newConfigurationItem.setAttribute("stateConfigurationItem", "ci");

                        newConfigurationItem.appendChild(newVersion);

                        Element proj = (Element) nodeProjectVersion.item(0);

                        proj.setAttribute("currentVersion", Integer.toString(currentVersion));
                        proj.setAttribute("nextVersion",  Integer.toString(nextVersion));

                        proj.insertBefore(newConfigurationItem, null);
                        root.insertBefore(proj, null);

                    }else{                      

                        newProjectVersion = doc.createElement(projectItem.getNameProject());
                        
                        newConfigurationItem.setAttribute("stateConfigurationItem", "ci");

                        newConfigurationItem.appendChild(newVersion);

                        newProjectVersion.setAttribute("currentVersion", "1");
                        newProjectVersion.setAttribute("nextVersion",  "2");

                        newProjectVersion.appendChild(newConfigurationItem);
                        root.appendChild(newProjectVersion);

                    }
                }

                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new FileOutputStream("/gardener/itensVersion.xml"));
                TransformerFactory transFactory = TransformerFactory.newInstance();
                Transformer transformer = transFactory.newTransformer();
                transformer.transform(source, result);

            }catch(Exception err)
            {
                System.out.println(err.getMessage());
            }

   }


   public String getCurrentVersionProject(String projectItem)
   {
       
       String currentVersion;

       try
       {

            DocumentBuilderFactory logVersion = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = logVersion.newDocumentBuilder();
            Document doc = docBuilder.parse(new File("/gardener/itensVersion.xml"));

            Element root = doc.getDocumentElement();

            NodeList listProject = root.getElementsByTagName(projectItem);

            if (listProject.getLength() > 0)
            {
            
                Element proj = (Element) listProject.item(0);
                currentVersion = proj.getAttribute("currentVersion").toString();
            }else{
                currentVersion = "0";
            }
            
            return  currentVersion;

        }catch(Exception err)
            {
                return err.getMessage();
            }

   }      


   public void setVersionNumber(String versionNumber)
   {
       this.versionNumber = versionNumber;
   }

   /**
    *
    * @return
    */
   public String getVersionNumber()
   {
        return this.versionNumber;
   }
  
}




