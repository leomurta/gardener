/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main.resources.versioning;

import br.uff.ic.gardener.server.*;
import br.uff.ic.gardener.*;
import java.io.*;
import java.util.*;
import java.net.URI;

//classe Application
/**
 *
 * @author Evaldo de Oliveira
 */
public class App{

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args)throws Exception{

    Server svr = Server.getInstance();

    String returnCommitCk;
    String returnCommitInit;
    String project="ufjf";
    
    URI uri_1 = new URI(project+"/e-mails2.txt");
    URI uri_2 = new URI(project+"/git_shell_ext_debug.txt");
    URI uri_3 = new URI(project+"/README.txt");
    URI uri_4 = new URI(project+"/THANKS.txt"); 

    ConfigurationItem ci1 = new ConfigurationItem(uri_1, new FileInputStream("/e-mails.txt"), CIType.file, null, "evaldo");
    ConfigurationItem ci2 = new ConfigurationItem(uri_2, new FileInputStream("/git_shell_ext_debug.txt"), CIType.file, null, "evaldo");
    ConfigurationItem ci3 = new ConfigurationItem(uri_3, new FileInputStream("/README.txt"), CIType.file, null, "evaldo");
    ConfigurationItem ci4 = new ConfigurationItem(uri_4, new FileInputStream("/THANKS.txt"), CIType.file, null, "evaldo");

    ArrayList<ConfigurationItem> listItens = new ArrayList<ConfigurationItem>();

    listItens.add(ci1);
    listItens.add(ci2);
    listItens.add(ci3);
    listItens.add(ci4);
    
    returnCommitInit = svr.init(project, "evaldo");
    System.out.println(returnCommitInit);

    returnCommitCk = svr.ckeckIn(project, "evado", "06/11/2010", "Atualizacao do sistema gardener", listItens);
    System.out.println(returnCommitCk);       
    
  }
}
