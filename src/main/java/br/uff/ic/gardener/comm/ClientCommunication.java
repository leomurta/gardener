/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gardener.comm;

import br.uff.ic.gardener.RevisionID;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
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

    Socket mySocket;

    public ClientCommunication() {
    }

    Map<String, OutputStream> doCheckout(String serverURL, RevisionID revision) {

        Object checkOut = null;
        mySocket = null;
        BufferedWriter bw = null;
        try {
            mySocket = new Socket(serverURL, 50000);
            
            System.out.println("Connected");
            PrintStream ps = new PrintStream(mySocket.getOutputStream());
            ps.println("Estou enviando dados para o servidor");

            //peguei o nome do arquivo e vou salvar no diretório do working space
            File myFile = new File("c:\\text.txt");
            //crie um novo byte array do tamanho do arquivo
            byte[] mybytearray = new byte[(int) myFile.length()];
            //crie o fileinputstream
            FileInputStream fis = new FileInputStream(myFile);
            //crie um buffered inputstream a partir do fileinputstream
            BufferedInputStream bis = new BufferedInputStream(fis);
            //leia PARA o bytearray
            bis.read(mybytearray);
            OutputStream os = mySocket.getOutputStream();
            System.out.println("Sending...");

            //envia o bytearray
            os.write(mybytearray);
            // flush para dar "vazão"
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

    public String receiveMessage() throws IOException {
        InputStreamReader isr = new InputStreamReader(mySocket.getInputStream());

       BufferedReader br = new BufferedReader(isr);

        return br.readLine();
    }

    public void sendMessage(String message) throws IOException {
        PrintWriter pw = new PrintWriter(mySocket.getOutputStream());

        pw.print(message);
    }

    public RevisionID doCheckin(String serverURL, Map<String, OutputStream> items, String user, String message) {
        return null;
    }

    public static void main(String[] args) {

        ClientCommunication comm = new ClientCommunication();

        comm.doCheckout("172.16.0.240", new RevisionID(0));
       // comm.doCheckout("192.168.0.101", new RevisionID(0));

    }
}
