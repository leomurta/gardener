/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package padraocommand;

import java.io.*;

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

    File dir = new File("c:/gardener");
    File[] listFile = dir.listFiles();

    svr.init("gardener", "evaldo");
    svr.commitInit("gardener");

    svr.init("gardener", "viviam");
    svr.rollbackInit("gardener");

    svr.init("gardener", "camila");
    svr.commitInit("gardener");

    svr.ckeckIn("gardener", "evado", "30/10/2010", "Atualização do sistema gardener", "c:/gardener", listFile);
    svr.roolbackCheckin("gardener");

    svr.ckeckIn("gardener", "viviam", "30/10/2010", "Alteração do codigo do sistema gardener", "c:/gardener", listFile);
    svr.commitCheckin("gardener");

    svr.ckeckIn("gardener", "camila", "30/10/2010", "Mudança em diretorio", "c:/gardener", listFile);
    svr.commitCheckin("gardener");
    
    svr.ckeckIn("gardener", "leandro", "30/10/2010", "Atualização do sistema gardener", "c:/gardener", listFile);
    svr.commitCheckin("gardener");

    svr.ckeckIn("gardener", "marcos", "30/10/2010", "Alteração de cálculo", "c:/gardener", listFile);
    svr.commitCheckin("gardener");

  }
}
