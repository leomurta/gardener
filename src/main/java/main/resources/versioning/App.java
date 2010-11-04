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

    svr.init("gardener", "evaldo");
    svr.commitInit("gardener");

    svr.ckeckIn("gardener", "evado", "30/10/2010", "Atualizacao do sistema gardener", "c:/gardener", listFile);
    svr.roolbackCheckin("gardener");
    
    listFile.add("teste111.txt");
    listFile.add("teste211.txt");
    listFile.add("teste311.txt");
    listFile.add("teste411.txt");

    svr.ckeckIn("gardener", "viviam", "30/10/2010", "Alteracao do codigo do sistema gardener", "c:/gardener", listFile);
    svr.commitCheckin("gardener");

    listFile.clear();
    
    listFile.add("teste111.txt");
    listFile.add("teste211.txt");
    listFile.add("teste311.txt");
    listFile.add("teste411.txt");

    svr.ckeckIn("gardener", "camila", "30/10/2010", "Mudanca em diretorio", "c:/gardener", listFile);
    svr.commitCheckin("gardener");

    listFile.clear();

    listFile.add("teste111.txt");
    listFile.add("teste211.txt");
    listFile.add("teste311.txt");
    listFile.add("teste411.txt");

    svr.ckeckIn("gardener", "leandro", "30/10/2010", "Atualizacao do sistema gardener", "c:/gardener", listFile);
    svr.commitCheckin("gardener");

    listFile.clear();

    listFile.add("teste111.txt");
    listFile.add("teste213.txt");
    listFile.add("teste314.txt");
    listFile.add("teste411.txt");
    
    svr.ckeckIn("gardener", "marcos", "30/10/2010", "Alteracao de calculo", "c:/gardener", listFile);
    svr.commitCheckin("gardener");

  }
}
