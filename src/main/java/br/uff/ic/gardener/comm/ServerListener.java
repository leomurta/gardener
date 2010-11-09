/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gardener.comm;

//import br.uff.ic.gardener.server.Project;
import br.uff.ic.gardener.server.Server;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.zip.*;

/**
 *
 * @author Cleyton
 */
public class ServerListener implements Runnable {

    ServerSocket serverSocket;
    int filesize = 6022386; // filesize temporarily hardcoded
    Socket mySocket;
    InputStream in;
    OutputStream out;

    public ServerListener() {
    }

    public void listenPort() {
        //  long start = System.currentTimeMillis();
        mySocket = new Socket();
        int bytesRead;
        int current = 0;
        try {
            //abrindo a conexão...
            serverSocket = new ServerSocket(20102);
            //System.out.println("InetAdress: " + serverSocket.getInetAddress());

            // esperado alguém conectar: podemos criar uma thread para permitir múltiplas conexões
            mySocket = serverSocket.accept();
            //imprime o endereço do socket aberto
            System.out.println("InetAdress: " + mySocket.getInetAddress() + " porta: " + mySocket.getPort());

            //BufferedReader br = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));

            // System.out.println("Connecting...");
            InputStream is = mySocket.getInputStream();

            ZipInputStream zis = new ZipInputStream(is);


            //ZipInputStream zis = new ZipInputStream(is);

            ZipEntry zipEntry = zis.getNextEntry();

            zipEntry.isDirectory();


            ZipOutputStream zos = new ZipOutputStream(mySocket.getOutputStream());
            byte[] a = new byte[filesize];
            FileOutputStream fos = new FileOutputStream("c:\text.txt");

            //zos.write();
            // receive file
            //criando o bytearray que vai receber o arquivo
            // (eu poderia pedir o tamanho do arquivo no gardener
//            byte[] mybytearray = new byte[filesize];
//
//            //pegando o inputstream, que vai receber o arquivo (o byte[])
//            InputStream is = mySocket.getInputStream();
//
//            //mySocket.get
//            // BufferedInputStream bs = new BufferedInputStream(is);
//
//            // BufferedReader as = new BufferedReader();
//
//            //br.readLine();
//
//
//            FileOutputStream fos = new FileOutputStream("C:\\Users\\Cleyton\\Desktop\\text.txt");
//            BufferedOutputStream bos = new BufferedOutputStream(fos);
//
//            InputStreamReader isr = new InputStreamReader(mySocket.getInputStream());
//
//            BufferedReader br = new BufferedReader(isr);
//
//            br.readLine();
//
//            //leia do inputstream até não existir mais bytes
//            bytesRead = is.read(mybytearray, 0, mybytearray.length);
//            current = bytesRead;
//            do {
//                //leia e armazene em mybytearray
//                bytesRead =
//                        is.read(mybytearray, current, (mybytearray.length - current));
//                //enquanto houver bytes ainda...
//                if (bytesRead >= 0) {
//                    current += bytesRead;
//                }
//            } while (bytesRead > -1);
//            //escreva buffered output stream
//            bos.write(mybytearray, 0, current);
//            bos.flush();
//            long end = System.currentTimeMillis();
//            // System.out.println(end - start);
//            System.out.println("Sucesso! I guess so...");
//
//
//            bos.close();

            //  System.out.println(br.readLine());


        } catch (IOException ex) {
            Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                mySocket.close();
                serverSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private String receiveMessage() throws IOException {
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(isr);
        String linha = br.readLine();

        return linha;
    }

    private void sendMessage(String message) throws IOException {
        // System.out.println("sending message" + message);
        //  PrintWriter pw = new PrintWriter(out, true);
        PrintWriter pw = new PrintWriter(out, true);
        pw.println(message);

    }
    /**
     * Comando para receber um objeto (não funciona)
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
     * Comando para enviar objeto (não funciona)
     * @param obj
     * @throws IOException
     */
    private void sendObject(Object obj) throws IOException {
        ObjectOutputStream objOut = new ObjectOutputStream(out);

        objOut.writeObject(obj);
    }

    /**
     * verifca a intenção e realiza a operação de acordo
     * @param intent
     * @throws IOException
     */
    private void doOperation(String intent) throws IOException {
        if (intent.compareTo("CO") == 0) {
            //faça o procedimento de checkout
            sendCheckout();
        } else {
            if (intent.compareTo("CI") == 0) {
                String message = receiveCheckin();
                sendMessage(message);

            } else {
                if (intent.compareTo("LS") == 0) {
                    sendList();
                }

            }
        }
    }

    @Override
    public void run() {
        //  long start = System.currentTimeMillis();
        mySocket = new Socket();
        //  int bytesRead;
        //  int current = 0;
        try {
            //1- abrir o servidor socket
            serverSocket = new ServerSocket(50000);
            System.out.println("InetAdress: " + serverSocket.getInetAddress().getHostAddress() + " Port: " + serverSocket.getLocalPort());

            // 2- aguardar uma conexão
            mySocket = serverSocket.accept();
            //2.1 - depois de aberto, imprimir o endereço da conexão originária
            System.out.println("Conectado a InetAdress: " + mySocket.getInetAddress() + " porta: " + mySocket.getPort());

            in = mySocket.getInputStream();
            out = mySocket.getOutputStream();

            /*
             * Pelo procotolo, vamos esperar uma mensagem dizendo o que é que vai ser feito:
             *  CO, CI ou LS
             */
            //3 - pegando a intenção
            String intent = receiveMessage();

            //4 - de acordo com cada opção, uma operação vai ser executada
            doOperation(intent);
            //nota: por enquanto, elefaz uma operação e para, mas podemos
            //colocar a execução num looping para esperar a próxima solicitação.


        } catch (IOException ex) {
            Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                mySocket.close();
                serverSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Este método é responsável por enviar o checkout ao cliente. 
     * Para isso, ele captura as informações do servidor (via classe Server)
     * e monta o stream (um arquivo zip) que vai ser recebido pelo cliente. 
     * O cliente deve saber o que fazer com o arquivo zip recebido.
     */
    private void sendCheckout() throws IOException {

        String projectName = null;
        String revisionNumber = null;
        Server servidor = Server.getInstance();

        //1 - servidor precisa dar um "ack" para continuar o processo de envio de informações
        sendMessage("ack");

        //2 - servidor precisa receber informações de checkout
        // para facilitar, o cliente vai construir um arquivo que terá 2 linhas:
        // uma contendo o nome do arquivo e outra contendo a revisão (dados que eu estou precisando agora)

        //TODO fazer um método que já jogue essas informações em um arquivo de projeto...
        // aqui, apenas estamos recebendo um arquivo qualquer. Talvez essa não seja a melhor forma,
        // e talvez tenhamos que pegar informação por informação (nome do projeto e revisão)
        // File configFile = receiveFile();

        // Project project = new Project(projectName);
        // String project  = "";
        //ArrayList metadataArray = servidor.ckeckout(null, project, revisionNumber);

        //assim que receber os dados de versão do servidor, preciso pegar os arquivos associados àquela revisão.

        //  File[] files = servidor.ckeckoutFile(project, revisionNumber);

        //3 - após pegar os arquivos, enviá-los ao cliente que solicitou. ele
        // será responsável por tratar os arquivos


    }

    private String receiveCheckin() throws IOException {
        String project = null;
        String user = null;
        String date = null;
        String message = null;
        ArrayList itens = null;
        String result = " it worked!";
        // neste momento, recebi o intent de checkout. O cliente está esperando um ok 
        sendMessage("ack");

        // 1 - Depois do ok, precisamos receber as informações.
        // Na sequência, receberemos o projeto, usuário, msg, e os arquivos.

        // recebendo o projeto
        project = receiveMessage();
        //manipula o resultado
        sendMessage("ack");

        // recebendo o usuário
        user = receiveMessage();
        //manipula o resultado
        sendMessage("ack");

        message = receiveMessage();
        //manipula o resultado
        sendMessage("ack");

        //TODO receber arquivo (s)

        System.out.println("Hi, i'm " + user + " and i'm trying to reach " + project + " project with this message: " + message);

        //TODO armazenar arquivos numa ArrayList

        //ConfigurationItem configItem = new ConfigurationItem(uri, InputStream, CIType.file, revision, user);

        Server server = Server.getInstance();
        // result = server.ckeckIn(project, user, date, message, "", itens);
        return result;
    }

    private void sendList() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Esse método é responsável por receber um arquivo. Não está pronto,
     * mas é o mais importantes na comunicação. Quando pronto, seremos capazes de enviar e receber arquivos
     * pela rede, ao invés de apenas trocar mensagens
     * @return
     */
    private File receiveFile() throws IOException {
        // TODO e se fizermos receber um arquivo zip sempre, facilitaria?

        File file = null;
        int bytesRead;
        int current;

        //  receive file
        // criando o bytearray que vai receber o arquivo
        //  (eu poderia pedir o tamanho do arquivo no gardener
        byte[] mybytearray = new byte[filesize];

        //pegando o inputstream, que vai receber o arquivo (o byte[])
        InputStream is = mySocket.getInputStream();

        //leia do inputstream até não existir mais bytes
        bytesRead = is.read(mybytearray, 0, mybytearray.length);
        current = bytesRead;
        do {
            //leia e armazene em mybytearray
            bytesRead =
                    is.read(mybytearray, current, (mybytearray.length - current));
            //enquanto houver bytes ainda...
            if (bytesRead >= 0) {
                current += bytesRead;
            }
        } while (bytesRead > -1);

        //escreva buffered output stream
        //TODO o nome do arquivo precisa ser informado de outra forma.
        // na verdade, precisamos só armazenar o array de bytes em algum lugar.
        FileOutputStream fos = new FileOutputStream("C:\\Users\\Cleyton\\Desktop\\file.txt");
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bos.write(mybytearray, 0, current);
        bos.flush();
        //TODO o nome do arquivo precisa ser informado de outra forma.
        file = new File("C:\\Users\\Cleyton\\Desktop\\file.txt");
        //  System.out.println("Sucesso! I guess so...");

        bos.close();
        return file;
    }
//        public static void main(String[] args) {
//        ServerListener sl = new ServerListener();
//        sl.listen();
//    }

    public static void main(String[] args) {
        ServerListener server = new ServerListener();
        server.run();
    }
}
