/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gardener.comm;

import br.uff.ic.gardener.CIType;
import br.uff.ic.gardener.ConfigurationItem;
import br.uff.ic.gardener.RevisionID;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Cleyton
 */
public class Communication {

    private InputStream in;
    private OutputStream out;

    /**
     * This method is responsible for sending a configurationItems list.
     * It does that applying the following steps:
     * For each item, get all the info and transform it to strings.
     * @param items
     * @throws IOException
     */
    public void sendConfigurationItems(Collection<ConfigurationItem> items) throws IOException {
        //TODO
        String ack = null;
        int filesize = 6022386; //harcoded

        //sendMessage("ITS");
        for (ConfigurationItem configItem : items) {
            String uriStr = configItem.getUri().toString();
            String user = configItem.getUser();
            String id = configItem.getStringID();
            String revID = Long.toString(configItem.getRevision().getNumber());
            String itemType = configItem.getType().toString();
            InputStream item = configItem.getItemAsInputStream();

            //depois de pegar, é hora de enviar (e esperar ack's)
            sendMessage("ackCI");

            //URI
            ack = receiveMessage();
            sendMessage(uriStr);

            //USER
            ack = receiveMessage();
            sendMessage(user); // os acks são "autorizações" de envio

            // ID
            ack = receiveMessage();
            sendMessage(id);


            // REVID
            ack = receiveMessage();
            sendMessage(revID);


            // ITEMTYPE
            ack = receiveMessage();
            sendMessage(itemType);


            //ITEM
            ack = receiveMessage();

            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            // byte array[] = new byte[6022386];
            //int a = item.read();

            byte[] bytes = new byte[512];
            // Read bytes from the input stream in bytes.length-sized chunks and write
            // them into the output stream
            int readBytes;
            while ((readBytes = item.read(bytes)) > 0) {
                bOut.write(bytes, 0, readBytes);
            }

            //ENVIAR ISSO AQUI
            byte arr[] = bOut.toByteArray();

            sendByteArray(arr);
        }
        sendMessage("EOCI");

    }

    public Collection<ConfigurationItem> receiveConfigurationItems() throws IOException {
        //TODO
        String uriStr = null;//configItem.getUri().toString();
        String user = null;// configItem.getUser();
        String id = null; //configItem.getStringID();
        String revID = null; //Long.toString(configItem.getRevision().getNumber());
        String itemType = null;//configItem.getType().toString();
        String itemStr = null;

        // devo iterar enquanto houve itens. Vou mandar então duas marcações
        // uma de inicio e outra de fim.
        String ackCI = "";
        ArrayList<ConfigurationItem> items = new ArrayList<ConfigurationItem>();

        ackCI = receiveMessage(); // recebendo a mensagem que vai ser iniciado o processo

        do {
            // its = receiveMessage();
            sendMessage("ack"); //significa "pode mandar"
            //URI
            uriStr = receiveMessage();
            sendMessage("ack");
            //USER
            user = receiveMessage();
            sendMessage("ack");
            // ID
            id = receiveMessage();
            sendMessage("ack");
            // REVID
            revID = receiveMessage();
            sendMessage("ack");
            // ITEMTYPE
            itemType = receiveMessage();
            sendMessage("ack"); //mensagem de ok, prossiga com o próximo passo

            System.out.println(uriStr + " " + user + " " + id + " " + revID + " " + itemType);
            //ITEM
            ByteArrayInputStream byteIn = new ByteArrayInputStream(receiveBytes());
            //  sendMessage("ack"); //will this be the problem?

            //criar novo item de configuração e adicionar à lista
            ConfigurationItem configItem = new ConfigurationItem(URI.create(uriStr), byteIn, CIType.valueOf(itemType), RevisionID.fromString(revID), user);

            items.add(configItem);
            // verificando se existem mais itens

            ackCI = receiveMessage();
            System.out.println("ACKCI HERE " + ackCI);
        } while (ackCI.compareTo("ackCI") == 0);

        return items; //i hope it's all

    }

    public String receiveMessage() throws IOException {
        InputStreamReader isr = new InputStreamReader(getIn());
        BufferedReader br = new BufferedReader(isr);
        String linha = br.readLine();

        return linha;
    }

    public void sendMessage(String message) throws IOException {
        System.out.println("sending message" + message);
        //  PrintWriter pw = new PrintWriter(out, true);
        PrintWriter pw = new PrintWriter(getOut(), true);
        pw.println(message);

    }

    private byte[] receiveBytes() throws IOException {

        int sizeBytes = Integer.parseInt(receiveMessage()); //recebendo o tamanho do arquivo

        sendMessage("ack"); //ok, prossiga

        byte a[] = new byte[sizeBytes];

        int bytesRead = 0;
        int current;

        //leia do inputstream até não existir mais bytes
        bytesRead = in.read(a, 0, a.length);
        current = bytesRead;
        do {
            System.out.println("lendo");
            //leia e armazene em mybytearray
            bytesRead =
                    in.read(a, current, (a.length - current));
            //enquanto houver bytes ainda...
            if (bytesRead >= 0) {
                current += bytesRead;
            }
            System.out.println("curent " + current);
            System.out.println("bytesRead " + bytesRead);
        } while (bytesRead > 0);

        System.out.println("sizeOfbytes = " + a.length);
        return a;

    }

    private void sendByteArray(byte[] arr) throws IOException {
        sendMessage(String.valueOf(arr.length)); // enviando o tamanho do arquivo

        System.out.println("sending " + arr.length + " bytes");

        String ack = receiveMessage();

        out.write(arr, 0, arr.length);

        System.out.println("sent array");

    }

    /**
     * @return the in
     */
    public InputStream getIn() {
        return in;
    }

    /**
     * @param in the in to set
     */
    public void setIn(InputStream in) {
        this.in = in;
    }

    /**
     * @return the out
     */
    public OutputStream getOut() {
        return out;
    }

    /**
     * @param out the out to set
     */
    public void setOut(OutputStream out) {
        this.out = out;
    }
}
