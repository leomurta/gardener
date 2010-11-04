/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main.resources.versioning;

import br.uff.ic.gardener.server.*;

import java.util.*;

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

    ArrayList listFile = new ArrayList();

    String returnCommitCk;
    String returnCommitInit;
    String project="ufjf";

    returnCommitInit = svr.init(project, "evaldo");
    System.out.println(returnCommitInit);
	//
    listFile.add(project+"-"+"teste1.txt");
    listFile.add(project+"-"+"teste2.txt");
    listFile.add(project+"-"+"teste3.txt");
    listFile.add(project+"-"+"teste4.txt");

    returnCommitCk = svr.ckeckIn(project, "evado", "30/10/2010", "Atualizacao do sistema gardener", "c:/gardener", listFile);
    System.out.println(returnCommitCk);

    listFile.clear();

    listFile.add(project+"-"+"teste1.txt");
    listFile.add(project+"-"+"teste2.txt");
    listFile.add(project+"-"+"teste3.txt");
    listFile.add(project+"-"+"teste4.txt");

    returnCommitCk = svr.ckeckIn(project, "viviam", "30/10/2010", "Alteracao do codigo do sistema gardener", "c:/gardener", listFile);
    System.out.println(returnCommitCk);

    listFile.clear();
    
    listFile.add(project+"-"+"teste1.txt");
    listFile.add(project+"-"+"teste2.txt");
    listFile.add(project+"-"+"teste3.txt");
    listFile.add(project+"-"+"teste4.txt");

    returnCommitCk = svr.ckeckIn(project, "camila", "30/10/2010", "Mudanca em diretorio", "c:/gardener", listFile);
    System.out.println(returnCommitCk);
    
    listFile.clear();

    listFile.add(project+"-"+"teste1.txt");
    listFile.add(project+"-"+"teste2.txt");
    listFile.add(project+"-"+"teste3.txt");
    listFile.add(project+"-"+"teste4.txt");

    returnCommitCk = svr.ckeckIn(project, "leandro", "30/10/2010", "Atualizacao do sistema gardener", "c:/gardener", listFile);
    System.out.println(returnCommitCk);
    
    listFile.clear();

    listFile.add(project+"-"+"teste0.txt");
    listFile.add(project+"-"+"teste2.txt");
    listFile.add(project+"-"+"teste3.txt");
    listFile.add(project+"-"+"teste4.txt");
    
    returnCommitCk = svr.ckeckIn(project, "marcos", "30/10/2010", "Alteracao de calculo", "c:/gardener", listFile);
    System.out.println(returnCommitCk);
    
  }
}
