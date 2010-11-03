/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.ic.gardener.server;

import java.io.*;
import java.util.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

//classe Log de Comandos
/**
 *
 * @author Evaldo de Oliveira
 */
public class LogCommandServer {
        
        private static LogCommandServer instance = new LogCommandServer();

        /**
         *
         */
        public LogCommandServer(){
	}

        /**
         *
         * @return
         */
        public static LogCommandServer getInstance() {
            return instance;
        }

        /**
         *
         * @param command
         * @param user
         * @param project
         */
        public void addLog(String command, String user, String project) {

            try{               

                Date dtCommand = new Date();

                DocumentBuilderFactory logServer = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = logServer.newDocumentBuilder();
                Document doc = docBuilder.parse(new File("/gardener/logServer.xml"));

                Element root = doc.getDocumentElement();

                Element elementCommand = doc.createElement(command);
                elementCommand.setAttribute("stateCommand", "1");

                Element dateCommand = doc.createElement("dateIncluding");
                Element userCommand = doc.createElement("userIncluding");
                Element messageCommand = doc.createElement("message");
                Element alterDateCommand = doc.createElement("dateChanging");
                Element projectCommand = doc.createElement("project");

                Text valueDate = doc.createTextNode(dtCommand.toString());
                Text valueUser = doc.createTextNode(user);
                Text valueCommand = doc.createTextNode("Comando " + command + " executado.");
                Text valueAlterDate = doc.createTextNode(" ");
                Text valueProject = doc.createTextNode(project);

                dateCommand.appendChild(valueDate);
                userCommand.appendChild(valueUser);
                messageCommand.appendChild(valueCommand);
                alterDateCommand.appendChild(valueAlterDate);
                projectCommand.appendChild(valueProject);

                elementCommand.appendChild(dateCommand);
                elementCommand.appendChild(userCommand);
                elementCommand.appendChild(messageCommand);
                elementCommand.appendChild(alterDateCommand);
                elementCommand.appendChild(projectCommand);

                root.appendChild(elementCommand);

                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new FileOutputStream("/gardener/logServer.xml"));
                TransformerFactory transFactory = TransformerFactory.newInstance();
                Transformer transformer = transFactory.newTransformer();
                transformer.transform(source, result);

            }catch(Exception err)
            {
                System.out.println(err.getMessage());
            }
            
	}
	
        /**
         *
         * @param command
         * @param project
         * @param indUpdate
         */
        public void updateLog(String command, String project, int indUpdate){

            try
            {

              Date dtCommand = new Date();
              String messageLog;

              DocumentBuilderFactory logServer = DocumentBuilderFactory.newInstance();
              DocumentBuilder docBuilder = logServer.newDocumentBuilder();
              Document doc = docBuilder.parse(new File("/gardener/logServer.xml"));

              Element root = doc.getDocumentElement();              

              NodeList listCommand = root.getElementsByTagName(command);

              for (int i =listCommand.getLength()-1; i >=0 ; i--){

                    Element elementCommand = (Element) listCommand.item(i);

                    if (elementCommand.getAttribute("stateCommand").toString().equals("1"))
                    {
                        NodeList listProject = elementCommand.getElementsByTagName("project");
                        Node proj = listProject.item(0).getFirstChild();

                        if (proj.getNodeValue().toString().equals(project))
                        {
                            if (indUpdate == 2)
                            {
                                messageLog = "Comando concluido. Nao pode ser desfeito.";
                            } else {
                                messageLog = "Commando desfeito.";
                            }

                            elementCommand.setAttribute("stateCommand", Integer.toString(indUpdate));

                            NodeList listMessage = elementCommand.getElementsByTagName("message");
                            Node message = listMessage.item(0);
                            message.setTextContent(messageLog);

                            NodeList listAlterDate = elementCommand.getElementsByTagName("dateChanging");
                            Node alterDate = listAlterDate.item(0);
                            alterDate.setTextContent(dtCommand.toString());

                            break;
                        }                   
                }
              }

              //grava o documento XML editado
              DOMSource source = new DOMSource(doc);
	      StreamResult result = new StreamResult(new FileOutputStream("/gardener/logServer.xml"));
	      TransformerFactory transFactory = TransformerFactory.newInstance();
	      Transformer transformer = transFactory.newTransformer();
	      transformer.transform(source, result);

           }catch(Exception err)
            {
                System.out.println(err.getMessage());
            }

	}
 
        /**
         *
         * @param command
         * @param project
         */
        public void listLog(String command, String project){

            try{

                DocumentBuilderFactory logServer = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = logServer.newDocumentBuilder();
                Document doc = docBuilder.parse(new File("/gardener/logServer.xml"));

                Element root = doc.getDocumentElement();                               

                NodeList listCommand = root.getElementsByTagName(command);
                
                for (int i =listCommand.getLength()-1; i >=0 ; i--)
                {

                     //como cada elemento do NodeList é um nó, precisamos fazer o cast
                     Element elementCommand = (Element) listCommand.item(i);

                     NodeList listDate = elementCommand.getElementsByTagName("dateIncluding");
                     Node date = listDate.item(0).getFirstChild();

                     NodeList listUser = elementCommand.getElementsByTagName("userIncluding");
                     Node user = listUser.item(0).getFirstChild();

                     NodeList listMessage = elementCommand.getElementsByTagName("message");
                     Node message = listMessage.item(0).getFirstChild();

                     NodeList listAlterDate = elementCommand.getElementsByTagName("dateChanging");
                     Node alterDate = listAlterDate.item(0).getFirstChild();

                     NodeList listProject = elementCommand.getElementsByTagName("project");
                     Node proj = listProject.item(0).getFirstChild();

                     System.out.println(date.getNodeValue() + ";"
                                         + user.getNodeValue() + ";"
                                         + message.getNodeValue() + ";"
                                         + alterDate.getNodeValue() + ";"
                                         + proj.getNodeValue());

                }                

            }catch(Exception err){

                System.out.println(err.getMessage());

            }
      }

        /**
         *
         * @param command
         * @param project
         * @return
         */
        public String commandOutStanding(String command, String project){

            try{

                String valueReturn = "FALSE";

                DocumentBuilderFactory logServer = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = logServer.newDocumentBuilder();
                Document doc = docBuilder.parse(new File("/gardener/logServer.xml"));

                Element root = doc.getDocumentElement();

                NodeList listCommand = root.getElementsByTagName(command);

                for (int i =listCommand.getLength()-1; i >=0 ; i--){

                    //como cada elemento do NodeList é um nó, precisamos fazer o cast
                    Element elementCommand = (Element) listCommand.item(i);

                    if (elementCommand.getAttribute("stateCommand").toString().equals("1"))
                    {

                        NodeList listProject = elementCommand.getElementsByTagName("project");
                        Node proj = listProject.item(0).getFirstChild();

                        if (proj.getNodeValue().toString().equals(project))
                        {
                            valueReturn = "TRUE";
                        }

                    }
                }

                return valueReturn;

            }catch(Exception err){

                return err.getMessage();

            }
      }

    /**
     *
     * @param command
     * @param project
     * @param user
     * @param date
     * @param message
     * @param path
     * @param itens
     */
    public void addOutStanding(String command
                      , String project
                      , String user
                      , String date
                      , String message
                      , String path
                      , ArrayList itens) {

            try{                

                DocumentBuilderFactory logServer = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = logServer.newDocumentBuilder();
                Document doc = docBuilder.parse(new File("/gardener/dataOutStanding.xml"));

                Element root = doc.getDocumentElement();

                Element elementOut = doc.createElement(command);

                Element dateOut = doc.createElement("date");
                Element userOut = doc.createElement("user");
                Element messageOut = doc.createElement("message");
                Element pathOut = doc.createElement("path");
                Element projectOut = doc.createElement("project");
                
                Text valueDate = doc.createTextNode(date);
                Text valueUser = doc.createTextNode(user);
                Text valueMessage = doc.createTextNode(message);
                Text valuePath = doc.createTextNode(path);
                Text valueProject = doc.createTextNode(project);

                dateOut.appendChild(valueDate);
                userOut.appendChild(valueUser);
                messageOut.appendChild(valueMessage);
                pathOut.appendChild(valuePath);
                projectOut.appendChild(valueProject);

                Element configurationItens = doc.createElement("configurationItens");
                
                for (int i = 0; i < itens.size(); i++)
                {
                     Element file = doc.createElement("file");
                     file.setAttribute("id", Integer.toString(i+1));

                     Text valueIten = doc.createTextNode(itens.get(i).toString());
                     file.appendChild(valueIten);

                     configurationItens.appendChild(file);

                 }
                
                elementOut.appendChild(dateOut);
                elementOut.appendChild(userOut);
                elementOut.appendChild(messageOut);
                elementOut.appendChild(pathOut);
                elementOut.appendChild(projectOut);
                elementOut.appendChild(configurationItens);

                root.appendChild(elementOut);

                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new FileOutputStream("/gardener/dataOutStanding.xml"));
                TransformerFactory transFactory = TransformerFactory.newInstance();
                Transformer transformer = transFactory.newTransformer();
                transformer.transform(source, result);

            }catch(Exception err)
            {
                System.out.println(err.getMessage());
            }

	}   


    /**
     *
     * @param command
     * @param project
     */
    public ArrayList regOutStanding(String command, String project){
        
        try{

                ArrayList reg = new ArrayList();

                DocumentBuilderFactory logServer = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = logServer.newDocumentBuilder();
                Document doc = docBuilder.parse(new File("/gardener/dataOutStanding.xml"));

                Element root = doc.getDocumentElement();                

                NodeList listCommand = root.getElementsByTagName(command);               

                for (int i =listCommand.getLength()-1; i >=0 ; i--){

                    //como cada elemento do NodeList é um nó, precisamos fazer o cast
                    Element elementCommand = (Element) listCommand.item(i);

                    NodeList listProject = elementCommand.getElementsByTagName("project");
                    Node proj = listProject.item(0).getFirstChild();

                    if (proj.getNodeValue().toString().equals(project))
                    {

                        NodeList listDate = elementCommand.getElementsByTagName("date");
                        Node date = listDate.item(0).getFirstChild();

                        NodeList listUser = elementCommand.getElementsByTagName("user");
                        Node user = listUser.item(0).getFirstChild();

                        NodeList listMessage = elementCommand.getElementsByTagName("message");
                        Node message = listMessage.item(0).getFirstChild();

                        NodeList listPath = elementCommand.getElementsByTagName("path");
                        Node path = listPath.item(0).getFirstChild();                        

                        reg.add(user.getNodeValue().toString());
                        reg.add(date.getNodeValue().toString());
                        reg.add(message.getNodeValue().toString());
                        reg.add(path.getNodeValue().toString());                        

                        break;
                    }

                }
                return reg;
                
            }catch(Exception err){

                ArrayList regReturn = new ArrayList();

                regReturn.add(err.getMessage());
                return regReturn;

            }
      }


    public ArrayList regFileOutStanding(String command, String project){

        try{

                ArrayList file= new ArrayList();

                DocumentBuilderFactory logServer = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = logServer.newDocumentBuilder();
                Document doc = docBuilder.parse(new File("/gardener/dataOutStanding.xml"));

                Element root = doc.getDocumentElement();

                NodeList listCommand = root.getElementsByTagName(command);

                for (int i =listCommand.getLength()-1; i >=0 ; i--){

                    //como cada elemento do NodeList é um nó, precisamos fazer o cast
                    Element elementCommand = (Element) listCommand.item(i);

                    NodeList listProject = elementCommand.getElementsByTagName("project");
                    Node proj = listProject.item(0).getFirstChild();

                    if (proj.getNodeValue().toString().equals(project))
                    {

                        NodeList listConfigurationItens = elementCommand.getElementsByTagName("configurationItens");

                        Element elementConfigurationItem = (Element) listConfigurationItens.item(0);
                        NodeList configurationItem = elementConfigurationItem.getElementsByTagName("file");

                        for (int itens = 0; itens < configurationItem.getLength();itens++)
                        {
                            Node item = configurationItem.item(itens);
                            file.add(item.getTextContent().toString());
                        }
                        
                    }
                    break;

                }
                return file;

            }catch(Exception err){

                ArrayList regReturn = new ArrayList();

                regReturn.add(err.getMessage());
                return regReturn;

            }
      }


    }


