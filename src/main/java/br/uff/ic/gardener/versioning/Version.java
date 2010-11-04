package br.uff.ic.gardener.versioning;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

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
   private String date;
   private String message;
   private String user;   
  
   /**
    *
    * @param versionNumber
    */

   public void createVersion(String nameConfigurationItem
                      , String project
                      , String user
                      , String date
                      , String message
                      , String path)
   {
        try{

                int currentVersion=0;
                int nextVersion=0;

                Element newConfigurationItem = null;
                Element newProjectVersion = null;

                DocumentBuilderFactory logVersion = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = logVersion.newDocumentBuilder();
                Document doc = docBuilder.parse(new File("/gardener/itensVersion.xml"));

                Element root = doc.getDocumentElement();                

                NodeList nodeConfigurationItem = root.getElementsByTagName(nameConfigurationItem);
                NodeList nodeProjectVersion = root.getElementsByTagName(project);                

                Element newVersion = doc.createElement("version");
                
                newVersion.setAttribute("pathVersion", path);
                newVersion.setAttribute("numberVersion", "1");                

                Element newTransactionVersion = doc.createElement("transaction");

                newTransactionVersion.setAttribute("dateVersion", date);
                newTransactionVersion.setAttribute("message", message);
                newTransactionVersion.setAttribute("userTransaction", user);
                newTransactionVersion.setAttribute("typeTransaction", "ci");

                newVersion.appendChild(newTransactionVersion);

                if (nodeConfigurationItem.getLength() > 0)
                {

                    Element configurationItem = (Element) nodeConfigurationItem.item(0);

                    configurationItem.insertBefore(newVersion, null);

                    currentVersion = Integer.parseInt(this.currentVersion(project, nameConfigurationItem));
                    nextVersion    = currentVersion + 1;

                    newVersion.setAttribute("numberVersion", Integer.toString(currentVersion));

                    configurationItem.setAttribute("currentVersion", Integer.toString(currentVersion));
                    configurationItem.setAttribute("nextVersion",  Integer.toString(nextVersion));
                    configurationItem.setAttribute("stateConfigurationItem", "ci");

                    configurationItem.appendChild(newVersion);

                    if (nodeProjectVersion.getLength() > 0)
                    {
                        Element proj = (Element) nodeProjectVersion.item(0);
                        proj.insertBefore(configurationItem, null);
                        root.insertBefore(proj, null);

                    }else{

                        newProjectVersion = doc.createElement(project);
                        newProjectVersion.appendChild(configurationItem);
                        root.appendChild(newProjectVersion);

                    }

                }else{

                    newConfigurationItem = doc.createElement(nameConfigurationItem);                   

                    if (nodeProjectVersion.getLength() > 0)
                    {

                        newConfigurationItem.setAttribute("currentVersion", "1");
                        newConfigurationItem.setAttribute("nextVersion",  "2");
                        newConfigurationItem.setAttribute("stateConfigurationItem", "ci");

                        newConfigurationItem.appendChild(newVersion);

                        Element proj = (Element) nodeProjectVersion.item(0);
                        proj.insertBefore(newConfigurationItem, null);
                        root.insertBefore(proj, null);

                    }else{                      


                        newProjectVersion = doc.createElement(project);

                        newConfigurationItem.setAttribute("currentVersion", "1");
                        newConfigurationItem.setAttribute("nextVersion",  "2");
                        newConfigurationItem.setAttribute("stateConfigurationItem", "ci");

                        newConfigurationItem.appendChild(newVersion);
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

   public String currentVersion(String project
                         , String nameConfigurationItem)
   {
       try
       {

            String currentVersion = "0";
            int currentVersiontemp= 0;

            DocumentBuilderFactory logVersion = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = logVersion.newDocumentBuilder();
            Document doc = docBuilder.parse(new File("/gardener/itensVersion.xml"));

            Element root = doc.getDocumentElement();
            NodeList listProject = root.getElementsByTagName(project);

            for (int i = 0; i < listProject.getLength(); i++){

                  Element listItem = (Element) listProject.item(i);
                  NodeList listItemVersion = listItem.getElementsByTagName(nameConfigurationItem);

                  Element item = (Element) listItemVersion.item(i);

                  currentVersion = item.getAttribute("currentVersion").toString();

                  break;
                  
            }

            if (currentVersion.equals("0"))
            {
                currentVersion = "1";
            }else{
                currentVersiontemp = Integer.parseInt(currentVersion)+1;
                currentVersion = Integer.toString(currentVersiontemp);
            }

            return currentVersion;

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

   /**
    *
    * @param date
    */
   public void setDate(String date)
   {
       this.date = date;
   }

   /**
    *
    * @return
    */
   public String getDate()
   {
       return this.date;
   }

   /**
    *
    * @param messageLog
    */
   public void setMessageLog(String message)
   {
       this.message = message;
   }

   /**
    *
    * @return
    */
   public String getMessageLog()
   {
       return this.message;
   }

   /**
    *
    * @param user
    */
   public void setUser(String user)
   {
       this.user = user;
   }

   /**
    *
    * @return
    */
   public String getUser()
   {
       return this.user;
   }


}




