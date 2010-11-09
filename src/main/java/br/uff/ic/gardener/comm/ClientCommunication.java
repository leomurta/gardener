/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gardener.comm;

import br.uff.ic.gardener.ConfigurationItem;
import br.uff.ic.gardener.RevisionID;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.util.Map;

/**
 *
 * @author Cleyton
 */
public class ClientCommunication {

    Socket mySocket;
    InputStream in;
    OutputStream out;

    public ClientCommunication(String serverURL) throws IOException {

        mySocket = new Socket(serverURL, 50000);

        in = mySocket.getInputStream();
        out = mySocket.getOutputStream();

        System.out.println("Connected");

    }

    public Map<String, OutputStream> doCheckout(String project, RevisionID revision) throws IOException {


        // 1 - envia mensagem ao servidor solicitando o checkout
        sendMessage("CO");

        String ack = receiveMessage();

        //envio o projeto de onde eu quero os itens

        sendMessage(project);

        ack = receiveMessage();

        //manda qual a revisão eu quero
        sendMessage(project);

        ack = receiveMessage();

        // recebe os arquivos
        //TODO Criar método para receber os arquivos

        // organiza os arquivos e retorna
        return null;


    }

    private String receiveMessage() throws IOException {
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(isr);
        String linha = br.readLine();

        return linha;
    }

    private void sendMessage(String message) throws IOException {
        System.out.println("sending message" + message);
        //  PrintWriter pw = new PrintWriter(out, true);
        PrintWriter pw = new PrintWriter(out, true);
        pw.println(message);

    }
    /**
     * Not working.
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private Object receiveObject() throws IOException, ClassNotFoundException {

        Object obj = null;

        ObjectInputStream objInp = new ObjectInputStream(in);

        obj = objInp.readObject();

        return obj;
    }

    /**
     * Not working.
     * @param obj
     * @throws IOException
     */
    private void sendObject(Object obj) throws IOException {
        ObjectOutputStream objOut = new ObjectOutputStream(out);

        objOut.writeObject(obj);
    }

    /**
     * Creates a new revison on the server specified by the URL
     * @param serverURL
     * @param items
     * @param user
     * @param message
     * @return
     * @throws MalformedURLException
     */
    public RevisionID doCheckin(Map<String, OutputStream> items, String user, String message) throws MalformedURLException, IOException {

        String project = "default";
        // 1 - enviar a solicitação de checkin e aguardar

        sendMessage("CI");
        String ack = receiveMessage();

        // 2 - depois da resposta, enviar nome do projeto (não há como pegar isso, ainda)
        sendMessage(project);

        ack = receiveMessage();

        // 3 - enviar o usuário
        sendMessage(user);

        ack = receiveMessage();

        // 4 - enviar a msg
        sendMessage(message);

        ack = receiveMessage();

        // 5 - depois da confirmação enviar os arquivos
        //TODO send ConfigurationItems via ObjectInputStream

        // por fim, receber o número da revisão
        String worked = receiveMessage();

        System.out.println("I hope " + worked);

        return null;
    }

    public void doInit(String serverURL) {
        //TODO implement!

    }

    public static void main(String[] args) throws IOException {

        ClientCommunication comm = new ClientCommunication("localhost");

        //  comm.doCheckout("172.16.0.240", new RevisionID(0));
        // comm.doCheckout("192.168.0.101", new RevisionID(0));
        comm.doCheckin(null, "cleyton", "I'm working!");

    }
}
