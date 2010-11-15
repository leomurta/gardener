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
            //   sendMessage("ack");

            // REVID
            ack = receiveMessage();
            sendMessage(revID);
            //   sendMessage("ack");

            // ITEMTYPE
            ack = receiveMessage();
            sendMessage(itemType);
            //  sendMessage("ack");

            //ITEM
            ack = receiveMessage();
            // String itemStr = item.toString();

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

            // Convert the contents of the output stream into a byte array
           // byte[] byteData = outputStream.toByteArray();


//            int size = 0;
//            while (a != -1) {
//                bOut.write(a);
//                size++;
//                System.out.println("size " + size + " bytes");
//                 bOut.flush();
//                a = item.read();
//            }
//            System.out.println("finished reading");


//            byte array[] = new byte[6022386];
//            InputStream is = item;
//            int bytesRead, current;
//            //leia do inputstream até não existir mais bytes
//            bytesRead = is.read(array, 0, array.length);
//            current = bytesRead;
//            do {
//                //System.out.println("lendo");
//                //leia e armazene em mybytearray
//                bytesRead =   is.read(array, current, (array.length - current));
//                //enquanto houver bytes ainda...
//                if (bytesRead >= 0) {
//                    current += bytesRead;
//                }
//                System.out.println("curent " + current);
//                System.out.println("bytesRead " + bytesRead);
//            } while (bytesRead > 0);


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
            sendMessage("ack");
            //ITEM
            //  itemStr = receiveMessage();
            //receieveing the item

            ByteArrayInputStream byteIn = new ByteArrayInputStream(receiveBytes());
            sendMessage("ack");

            //criar novo item de configuração e adicionar à lista
            ConfigurationItem configItem = new ConfigurationItem(URI.create(uriStr), byteIn, CIType.valueOf(itemType), RevisionID.fromString(revID), user);

            items.add(configItem);
            // verificando se existem mais itens
            ackCI = receiveMessage();
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

    /**
     * Not working.
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
//    private Object receiveObject() throws IOException, ClassNotFoundException {
//
//        Object obj = null;
//
//        ObjectInputStream objInp = new ObjectInputStream(getIn());
//
//        obj = objInp.readObject();
//
//        return obj;
//    }
//
//    private void sendObject(Object obj) throws IOException {
//        ObjectOutputStream objOut = new ObjectOutputStream(getOut());
//
//        objOut.writeObject(obj);
//    }
    private byte[] receiveBytes() throws IOException {

        int sizeBytes = Integer.parseInt(receiveMessage());
        sendMessage("ack");

        //  System.out.println("receieving"+sizeBytes+"bytes");
        InputStreamReader isr = new InputStreamReader(getIn());

        ByteArrayOutputStream byOut = new ByteArrayOutputStream();
        byte a[] = new byte[sizeBytes];
        //int a = getIn().read();

        // while (bytesRead < sizeBytes) {
        // bytesRead = bytesRead + getIn().read(a, bytesRead, 65536);
        // }
        //  bytesRead = getIn().read(a, 0, a.length);

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

        // System.out.println("bytesRead = " + bytesRead);


        // int size = 0;
//        while (a != -1) {
//            System.out.println(a);
//            byOut.write(a);
//            a = getIn().read();
//            System.out.println(++size);
//            //System.out.println(a);
//        }
        //    System.out.println("bytes received");
        //    System.out.println(byOut.toByteArray().length + " bytes received");

        // InputStream byIn = new ByteArrayInputStream(byOut.toByteArray());

        //BufferedReader br = new BufferedReader(isr);
        //String linha = br.readLine();
        System.out.println("sizeOfbytes = " + a.length);
        return a;

    }

    private void sendByteArray(byte[] arr) throws IOException {
        sendMessage(String.valueOf(arr.length));
        System.out.println("sending " + arr.length + " bytes");

        //if file is bigger than 65536 bytes, I must break the sending
        //it feels like i'm doing something wrong...


        String ack = receiveMessage();
        //  int sizeSent = 0;
        // while (sizeSent > arr.length) {
        out.write(arr, 0, arr.length);
        //      sizeSent = sizeSent + 65536;
        //}
        //  out.flush();
        System.out.println("sent array");

    }

//    private File receiveFile() throws IOException {
//        // TODO e se fizermos receber um arquivo zip sempre, facilitaria?
//        int filesize = 6022386; //hardcoded
//        File file = null;
//        int bytesRead;
//        int current;
//
//        //  receive file
//        // criando o bytearray que vai receber o arquivo
//        //  (eu poderia pedir o tamanho do arquivo no gardener
//        byte[] mybytearray = new byte[filesize];
//
//        //pegando o inputstream, que vai receber o arquivo (o byte[])
//        InputStream is = in;
//
//        //leia do inputstream até não existir mais bytes
//        bytesRead = is.read(mybytearray, 0, mybytearray.length);
//        current = bytesRead;
//        do {
//            //leia e armazene em mybytearray
//            bytesRead =
//                    is.read(mybytearray, current, (mybytearray.length - current));
//            //enquanto houver bytes ainda...
//            if (bytesRead >= 0) {
//                current += bytesRead;
//            }
//        } while (bytesRead > -1);
//
//
//        //jogue o lido para um InputStream e pronto!
//
//
//        //escreva buffered output stream
//        //TODO o nome do arquivo precisa ser informado de outra forma.
//        // na verdade, precisamos só armazenar o array de bytes em algum lugar.
//        FileOutputStream fos = new FileOutputStream("C:\\Users\\Cleyton\\Desktop\\file.txt");
//        BufferedOutputStream bos = new BufferedOutputStream(fos);
//        bos.write(mybytearray, 0, current);
//        bos.flush();
//        //TODO o nome do arquivo precisa ser informado de outra forma.
//        file = new File("C:\\Users\\Cleyton\\Desktop\\file.txt");
//        //  System.out.println("Sucesso! I guess so...");
//
//        bos.close();
//        return file;
//    }
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
