/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gardener.comm;

//import br.uff.ic.gardener.server.Project;
import br.uff.ic.gardener.ConfigurationItem;
import br.uff.ic.gardener.server.Server;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.zip.*;

/**
 *
 * @author Cleyton
 */
public class ServerListener extends Communication implements Runnable {

    ServerSocket serverSocket;
    //int filesize = 6022386; // filesize temporarily hardcoded
    Socket mySocket;

    public ServerListener() {
    }

    @Deprecated
    public void listenPort() {
        //  long start = System.currentTimeMillis();
//        mySocket = new Socket();
//        int bytesRead;
//        int current = 0;
//        try {
//            //abrindo a conexão...
//            serverSocket = new ServerSocket(20102);
//            //System.out.println("InetAdress: " + serverSocket.getInetAddress());
//
//            // esperado alguém conectar: podemos criar uma thread para permitir múltiplas conexões
//            mySocket = serverSocket.accept();
//            //imprime o endereço do socket aberto
//            System.out.println("InetAdress: " + mySocket.getInetAddress() + " porta: " + mySocket.getPort());
//
//            //BufferedReader br = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
//
//            // System.out.println("Connecting...");
//            InputStream is = mySocket.getInputStream();
//
//            ZipInputStream zis = new ZipInputStream(is);
//
//
//            //ZipInputStream zis = new ZipInputStream(is);
//
//            ZipEntry zipEntry = zis.getNextEntry();
//
//            zipEntry.isDirectory();
//
//
//            ZipOutputStream zos = new ZipOutputStream(mySocket.getOutputStream());
//            byte[] a = new byte[filesize];
//            FileOutputStream fos = new FileOutputStream("c:\text.txt");
//
//        } catch (IOException ex) {
//            Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//            try {
//                mySocket.close();
//                serverSocket.close();
//            } catch (IOException ex) {
//                Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
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
                String versionNumber = receiveCheckin();
                sendMessage(versionNumber);

            } else {
                if (intent.compareTo("LS") == 0) {
                    sendList();
                } else {
                    if (intent.compareTo("IN") == 0) {
                        doInit();
                    } else {
                        if (intent.compareTo("LR") == 0) {
                            String revN = doLastRevision();
                            sendMessage(revN);
                        }
                    }
                }

            }
        }
    }

    @Override
    public void run() {

        mySocket = new Socket();

        try {
            //1- abrir o servidor socket
            serverSocket = new ServerSocket(50000);
            System.out.println("InetAdress: " + serverSocket.getInetAddress().getHostAddress() + " Port: " + serverSocket.getLocalPort());

            // 2- aguardar uma conexão
            mySocket = serverSocket.accept();
            //2.1 - depois de aberto, imprimir o endereço da conexão originária
            System.out.println("Conectado a InetAdress: " + mySocket.getInetAddress() + " porta: " + mySocket.getPort());

            setIn(mySocket.getInputStream());
            setOut(mySocket.getOutputStream());

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

        //recebo o projeto de onde eu quero os itens
        String project = receiveMessage();
        sendMessage("ack");
        //recebo qual a revisão eu quero
        String revN = receiveMessage();
        sendMessage("ack");

        // envio os arquivos
        //TODO precisa chamar o checkout do servidor (que não está implementado)
        ArrayList<ConfigurationItem> items = new ArrayList<ConfigurationItem>();
        sendConfigurationItems(items);


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

//        // recebendo o usuário
//        user = receiveMessage();
//        //manipula o resultado
//        sendMessage("ack");

        message = receiveMessage();
        //manipula o resultado
        sendMessage("ack");

        //TODO receber itens
        Collection<ConfigurationItem> configItems = receiveConfigurationItems();

        Server server = Server.getInstance();
        result = server.ckeckIn(project, user, date, message, (ArrayList<ConfigurationItem>) configItems);
        // result = server.ckeckIn(project, user, date, message, "", itens);
        return result;
    }

    private void sendList() {
        //TODO implementar

        throw new UnsupportedOperationException("Not yet implemented");
    }

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
    private void doInit() throws IOException {

        sendMessage("ack");
        //receber nome do projeto que eu quero criar
        String project = receiveMessage();

        sendMessage("ack");
        //receber nome do projeto que eu quero criar
        String user = receiveMessage();

        //criar a parada
        Server server = Server.getInstance();

        server.init(project, project);
        //tchau

    }

    private String doLastRevision() throws IOException {

        sendMessage("ack");

        //receive project name
        String project = receiveMessage();

        return String.valueOf(Server.getInstance().getLastRevision(project));

    }

    public static void main(String[] args) {
        ServerListener server = new ServerListener();
        server.run();
    }
}
