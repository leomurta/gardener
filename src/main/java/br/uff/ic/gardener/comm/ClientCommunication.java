/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gardener.comm;

import br.uff.ic.gardener.CIType;
import br.uff.ic.gardener.ConfigurationItem;
import br.uff.ic.gardener.RevisionCommited;
import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.diff.Diff;
import br.uff.ic.gardener.patch.Patch;
import br.uff.ic.gardener.util.FileHelper;
import br.uff.ic.gardener.util.UtilStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Deflater;

/**
 *
 * @author Cleyton
 */
public class ClientCommunication extends Communication {

    Socket mySocket;
    private String serverURL;

    public ClientCommunication(String serverURL) throws IOException {

        this.serverURL = serverURL;
        mySocket = new Socket(serverURL, 50000);

        setIn(mySocket.getInputStream());
        setOut(mySocket.getOutputStream());

        System.out.println("Connected");

    }

    public Collection<ConfigurationItem> doCheckout(String project, RevisionID revision) throws IOException {
        //TODO test!
        // 1 - envia mensagem ao servidor solicitando o checkout
        sendMessage("CO");

        String ack = receiveMessage();

        //envio o projeto de onde eu quero os itens
//        sendMessage(project);
//
//        ack = receiveMessage();
//
//        //manda qual a revisão eu quero
//        sendMessage(project);

        String fullString = project + ":" + revision.toString();
        //   String[] str = fullString.split(":");
        //   System.out.println("str "+str.length);
        sendMessage(fullString);

        ack = receiveMessage();

        // recebe os arquivos
        Collection<ConfigurationItem> items = receiveConfigurationItems();

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
    public RevisionID doCheckin(String strProject, String strMessage, Collection<ConfigurationItem> items) throws Exception {

//        RevisionID revID = null;

        // 1 - enviar a solicitação de checkin e aguardar
        try {
            sendMessage("CI");
            String ack = receiveMessage();

            // 2 - depois da resposta, enviar nome do projeto 
//            sendMessage(strProject);
//            ack = receiveMessage();
//
//            // 3 - enviar a msg
//            sendMessage(strMessage);

            String fullString = strProject + ":" + strMessage;
            sendMessage(fullString);


            ack = receiveMessage();

            // 4 - depois da confirmação enviar os itens
            sendConfigurationItems(items);

            //5 - por fim, receber o número da revisão
            String revN = receiveMessage();

            return new RevisionID(Long.parseLong(revN));

        } catch (Exception e) {
//            if(e instanceof IOException){
            throw new Exception("Problem in checkin. Please, check if the connection is avaiable or the URL to the server is correct");
//            }
        }

        //   System.out.println("new Revision " + revN);


    }

    public void doInit(String project, String user) throws IOException {

        //TODO test!
        sendMessage("IN");

        String ack = receiveMessage();
//
//        sendMessage(project);
//
//        ack = receiveMessage();
//
//        sendMessage(user);

        sendMessage(project + ":" + user);


        //gotta receive a message to confirm? (it's good to do so)
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

    /**
     *
     * @param first
     * @param last
     * @return Collection of RevisionCommited items, or null if an error ocurred
     * @throws IOException
     */
    public Collection<RevisionCommited> doLog(RevisionID first, RevisionID last) throws IOException {

        sendMessage("LG");

        receiveMessage();

//        sendMessage(first.toString());
//
//        receiveMessage();
//
//        sendMessage(last.toString());

        sendMessage(first.toString() + ":" + last.toString());


        ArrayList<RevisionCommited> list;
        try {
            list = receiveRevisionCommitedList();
        } catch (ParseException ex) {
            list = null;
            Logger.getLogger(ClientCommunication.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static void main(String[] args) throws IOException, Exception {
        //    comm.doInit("myProject", "user");
        ClientCommunication comm = new ClientCommunication("localhost");

        System.out.println("Testing Checkin");
        comm.testCheckin();

        comm = new ClientCommunication("localhost");
        System.out.println("Testing checkout");
        comm.testCheckout();

        comm = new ClientCommunication("localhost");
        System.out.println("Testing init");
        comm.testInit();

        comm = new ClientCommunication("localhost");
        System.out.println("Testing lastRevision");
        comm.testGetLastRevision();

//        comm = new ClientCommunication("localhost");
//        System.out.println("Testing log");
//        comm.testDoLog();
//
    }

    public void testCheckout() throws IOException {

        doCheckout("default", RevisionID.NEW_REVISION);
    }

    public void testInit() throws IOException {
        doInit("projet", "user");
    }

    public void testDoLog() throws IOException {

        doLog(RevisionID.NEW_REVISION, RevisionID.LAST_REVISION);
    }

    public void testCheckin() throws IOException, Exception {

        File file = null;
        try {

            ArrayList<ConfigurationItem> itemsz = new ArrayList<ConfigurationItem>();

            file = FileHelper.createTemporaryRandomFile();
            FileOutputStream finp = new FileOutputStream(file);

            UtilStream.fillRandomData(finp, 34);
            finp.close();

            ConfigurationItem item;
            InputStream in = new FileInputStream(file);

            item = new ConfigurationItem(new URI("/foo/bar"), in, CIType.file, new RevisionID(0), "cleyton");

            itemsz.add(item);
            itemsz.add(item);
            itemsz.add(item);
            itemsz.add(item);
            itemsz.add(item);
            itemsz.add(item);

            RevisionID revID = doCheckin("myProject", "myMessage", itemsz);

            System.out.println("Created version no. : " + revID);


        } catch (URISyntaxException ex) {
            Logger.getLogger(ClientCommunication.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            FileHelper.deleteDirTree(file);
        }
    }

    public void testGetLastRevision() throws IOException {

        System.out.println("LastRevision is " + getLastRevision("project").toString());

    }

    /**
     * @return the serverURL
     */
    public String getServerURL() {
        return serverURL;
    }
}
