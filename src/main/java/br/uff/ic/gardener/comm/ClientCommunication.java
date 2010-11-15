/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gardener.comm;

import br.uff.ic.gardener.ConfigurationItem;
import br.uff.ic.gardener.RevisionID;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author Cleyton
 */
public class ClientCommunication extends Communication {

    Socket mySocket;
    InputStream in;
    OutputStream out;

    public ClientCommunication(String serverURL) throws IOException {

        mySocket = new Socket(serverURL, 50000);

        in = mySocket.getInputStream();
        out = mySocket.getOutputStream();

        System.out.println("Connected");

    }

    public Collection<ConfigurationItem> doCheckout(String project, RevisionID revision) throws IOException {
        //TODO test!
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
        Collection<ConfigurationItem> items = receiveConfigurationItems();

        //preciso mandar um ack pro servidor voltar a escutar a porta?

        return items;

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
    public RevisionID doCheckin(String strProject, String strMessage, Collection<ConfigurationItem> items) throws MalformedURLException, IOException {

        //String project = "default";
        // 1 - enviar a solicitação de checkin e aguardar
        sendMessage("CI");
        String ack = receiveMessage();

        // 2 - depois da resposta, enviar nome do projeto (não há como pegar isso, ainda)
        sendMessage(strProject);
        ack = receiveMessage();

        // 3 - enviar a msg
        sendMessage(strMessage);
        ack = receiveMessage();

        // 4 - depois da confirmação enviar os itens
        sendConfigurationItems(items);

        //5 - por fim, receber o número da revisão
        String revN = receiveMessage();

        System.out.println("new Revision " + revN);

        return new RevisionID(Long.parseLong(revN));
    }

    public void doInit(String project, String user) throws IOException {
        //TODO test!
        String ack = receiveMessage();

        sendMessage(project);

        ack = receiveMessage();

        sendMessage(user);

    }

    public RevisionID getLastRevision(String project) throws IOException {
    //TODO test
        String revN;

        sendMessage("LR");

        String ack = receiveMessage();

        sendMessage(project);

        revN = receiveMessage();

        return new RevisionID(Long.parseLong(revN));
    }

    public static void main(String[] args) throws IOException {

        ClientCommunication comm = new ClientCommunication("localhost");

        //  comm.doCheckout("172.16.0.240", new RevisionID(0));
        // comm.doCheckout("192.168.0.101", new RevisionID(0));
        comm.doCheckin("meuProjeto", "minha Mensagem", null);

    }
}
