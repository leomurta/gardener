/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gardener.comm;

import br.uff.ic.gardener.CIType;
import br.uff.ic.gardener.ConfigurationItem;
import br.uff.ic.gardener.RevisionCommited;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

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
        // String ack = null;

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
            receiveMessage();
//            sendMessage(uriStr);
//
//            //USER
//            receiveMessage();
//            sendMessage(user); // os acks são "autorizações" de envio
//
//            // ID
//            receiveMessage();
//            sendMessage(id);
//
//
//            // REVID
//            receiveMessage();
//            sendMessage(revID);
//
//
//            // ITEMTYPE
//            receiveMessage();
//            sendMessage(itemType);

            String fullString = uriStr + ":" + user + ":" + id + ":" + revID + ":" + itemType;
            sendMessage(fullString);

            //ITEM
            receiveMessage();

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
            byte[] arr = bOut.toByteArray();
            byte[] compressed = compressByteArray(arr);
            sendByteArray(compressed);
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

        // devo iterar enquanto houve itens. Vou mandar então duas marcações
        // uma de inicio e outra de fim.
        String ackCI = "";
        ArrayList<ConfigurationItem> items = new ArrayList<ConfigurationItem>();

        ackCI = receiveMessage(); // recebendo a mensagem que vai ser iniciado o processo

        do {
            // its = receiveMessage();
            sendMessage("ack"); //significa "pode mandar"
//            //URI
//            uriStr = receiveMessage();
//            sendMessage("ack");
//            //USER
//            user = receiveMessage();
//            sendMessage("ack");
//            // ID
//            id = receiveMessage();
//            sendMessage("ack");
//            // REVID
//            revID = receiveMessage();
//            sendMessage("ack");
//            // ITEMTYPE
//            itemType = receiveMessage();

            String[] split = receiveMessage().split(":");
            uriStr = split[0];
            user = split[1];
            id = split[2];
            revID = split[3];
            itemType = split[4];

            sendMessage("ack"); //mensagem de ok, prossiga com o próximo passo

            System.out.println(uriStr + " " + user + " " + id + " " + revID + " " + itemType);
            //ITEM
            //      ByteArrayInputStream byteIn = new ByteArrayInputStream(receiveBytes());
            //  sendMessage("ack"); //will this be the problem?

            InputStream byteIn = byteArrayToInputStream(deCompressByteArray(receiveBytes()));

            //criar novo item de configuração e adicionar à lista
            ConfigurationItem configItem = new ConfigurationItem(URI.create(uriStr), byteIn, CIType.valueOf(itemType), RevisionID.fromString(revID), user);

            items.add(configItem);
            // verificando se existem mais itens

            ackCI = receiveMessage();
            //  System.out.println("ACKCI HERE " + ackCI);
        } while (ackCI.compareTo("ackCI") == 0);

        return items; //i hope it's all

    }

    public ArrayList<RevisionCommited> receiveRevisionCommitedList() throws IOException, ParseException {

        ArrayList<RevisionCommited> list = new ArrayList<RevisionCommited>();

        String user;
        Date dateCommit;
        String message;
        RevisionID id;

        String ackCI = "";

        ackCI = receiveMessage(); // recebendo a mensagem que vai ser iniciado o processo
        do {

            // its = receiveMessage();
            sendMessage("ack"); //significa "pode mandar"
            //URI
            user = receiveMessage();
            sendMessage("ack");
            //USER
            dateCommit = DateFormat.getInstance().parse(receiveMessage());
            sendMessage("ack");
            // ID
            message = receiveMessage();
            sendMessage("ack");
            // REVID
            id = RevisionID.fromString(receiveMessage());
            sendMessage("ack");

            RevisionCommited revComm = new RevisionCommited(id, user, message, dateCommit);

            list.add(revComm);
            // verificando se existem mais itens

            ackCI = receiveMessage();
            //System.out.println("ACKCI HERE " + ackCI);
        } while (ackCI.compareTo("ackRI") == 0);


        return list;
    }

    public void sendRevisionCommitedList(Collection<RevisionCommited> items) throws IOException {

        //sendMessage("ITS");
        for (RevisionCommited item : items) {

            String user = item.getUser();
            Date dateCommit = item.getDateCommit();
            String message = item.getMessage();
            RevisionID id = item.getId();

            //depois de pegar, é hora de enviar (e esperar ack's)
            sendMessage("ackCI");

            //user
            receiveMessage();
            sendMessage(user);

            //date
            receiveMessage();
            sendMessage(DateFormat.getInstance().format(dateCommit)); // os acks são "autorizações" de envio

            // mesage
            receiveMessage();
            sendMessage(message);

            // REVID
            receiveMessage();
            sendMessage(id.toString());

        }
        sendMessage("EORI");

    }

    public String receiveMessage() throws IOException {
        InputStreamReader isr = new InputStreamReader(getIn());
        BufferedReader br = new BufferedReader(isr);
        String linha = br.readLine();


        return linha;
    }

    public void sendMessage(String message) throws IOException {
        //   System.out.println("sending message" + message);
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
            //   System.out.println("lendo");
            //leia e armazene em mybytearray
            bytesRead =
                    in.read(a, current, (a.length - current));
            //enquanto houver bytes ainda...
            if (bytesRead >= 0) {
                current += bytesRead;
            }
            /// System.out.println("curent " + current);
            //  System.out.println("bytesRead " + bytesRead);
        } while (bytesRead > 0);

        // System.out.println("sizeOfbytes = " + a.length);
        return a;

    }

    private void sendByteArray(byte[] arr) throws IOException {
        sendMessage(String.valueOf(arr.length)); // enviando o tamanho do arquivo

        //   System.out.println("sending " + arr.length + " bytes");

        String ack = receiveMessage();

        out.write(arr, 0, arr.length);

        // System.out.println("sent array");

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

    public void sendConfigurationItemsNew(Collection<ConfigurationItem> items) throws IOException {
        //TODO
        String ack = null;

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

            String fullString = uriStr + "\n" + user + "\n" + id + "\n" + revID + "\n" + itemType;

            sendMessage(fullString);

            //ITEM
            ack = receiveMessage();

//            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
//            // byte array[] = new byte[6022386];
//            //int a = item.read();
//
//            byte[] bytes = new byte[512];
//            // Read bytes from the input stream in bytes.length-sized chunks and write
//            // them into the output stream
//            int readBytes;
//            while ((readBytes = item.read(bytes)) > 0) {
//                bOut.write(bytes, 0, readBytes);
//            }

            //ENVIAR ISSO AQUI
            //  byte arr[] = bOut.toByteArray();
            byte arr[] = inputStreamToByteArray(item);

            sendByteArray(arr);
        }
        sendMessage("EOCI");

    }

    public byte[] compressByteArray(byte[] input) {

        // Create the compressor with highest level of compression
        Deflater compressor = new Deflater();
        compressor.setLevel(Deflater.BEST_COMPRESSION);

        // Give the compressor the data to compress
        compressor.setInput(input);
        compressor.finish();

        // Create an expandable byte array to hold the compressed data.
        // You cannot use an array that's the same size as the orginal because
        // there is no guarantee that the compressed data will be smaller than
        // the uncompressed data.
        ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);

        // Compress the data
        byte[] buf = new byte[1024];
        while (!compressor.finished()) {
            int count = compressor.deflate(buf);
            bos.write(buf, 0, count);
        }
        try {
            bos.close();
        } catch (IOException e) {
        }

        // Get the compressed data
        byte[] compressedData = bos.toByteArray();

        return compressedData;

    }

    public byte[] deCompressByteArray(byte[] input) {
        // Create the decompressor and give it the data to compress
        Inflater decompressor = new Inflater();
        decompressor.setInput(input);

// Create an expandable byte array to hold the decompressed data
        ByteArrayOutputStream bos = new ByteArrayOutputStream(input.length);

// Decompress the data
        byte[] buf = new byte[1024];
        while (!decompressor.finished()) {
            try {
                int count = decompressor.inflate(buf);
                bos.write(buf, 0, count);
            } catch (DataFormatException e) {
            }
        }
        try {
            bos.close();
        } catch (IOException e) {
        }

// Get the decompressed data
        byte[] decompressedData = bos.toByteArray();

        
        return decompressedData;
    }

    private byte[] inputStreamToByteArray(InputStream item) throws IOException {

        byte[] array = null;

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

        array = bOut.toByteArray();

        return array;
    }

    private InputStream byteArrayToInputStream(byte[] arr) throws IOException {

        ByteArrayInputStream byteIn = new ByteArrayInputStream(arr);

        return byteIn;
    }
}
