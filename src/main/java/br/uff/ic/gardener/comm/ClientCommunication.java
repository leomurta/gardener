/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gardener.comm;

import br.uff.ic.gardener.RevisionID;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cleyton
 */
public class ClientCommunication {

    public ClientCommunication() {
    }

     Map<String,OutputStream> doCheckout(String serverURL, RevisionID revision) {

        Object checkOut = null;
        Socket mySocket = null;
        BufferedWriter bw = null;
        try {
            mySocket = new Socket(serverURL, 20102);

            PrintStream ps = new PrintStream(mySocket.getOutputStream());
            ps.println("Estou enviando dados para o servidor");

            File myFile = new File("c:\\text.txt");
            byte[] mybytearray = new byte[(int) myFile.length()];
            FileInputStream fis = new FileInputStream(myFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(mybytearray);
            OutputStream os = mySocket.getOutputStream();
            System.out.println("Sending...");
            os.write(mybytearray);
            os.flush();


        } catch (UnknownHostException ex) {
            Logger.getLogger(ClientCommunication.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClientCommunication.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                mySocket.close();
            } catch (IOException ex) {
                Logger.getLogger(ClientCommunication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return null;
    }

    public RevisionID doCheckin(String serverURL, Map<String,OutputStream> items, String user, String message){
        return null;
    }

    public static void main(String[] args) {

        ClientCommunication comm = new ClientCommunication();

       comm.doCheckout("192.168.0.101",new RevisionID(0));

    }
}
