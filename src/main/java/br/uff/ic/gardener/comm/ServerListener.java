/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gardener.comm;

//import br.uff.ic.gardener.server.Project;
import br.uff.ic.gardener.CIType;
import br.uff.ic.gardener.ConfigurationItem;
import br.uff.ic.gardener.RevisionCommited;
import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.server.Server;
import br.uff.ic.gardener.util.FileHelper;
import br.uff.ic.gardener.util.UtilStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cleyton
 */
public class ServerListener extends Communication implements Runnable {

    ServerSocket serverSocket;
    Socket mySocket;

    public ServerListener() {
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
                        } else {
                            if (intent.compareTo("LG") == 0) {
                                sendLog();
                                /// sendMessage(revN);
                            }
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
            serverSocket = new ServerSocket(50000);
        } catch (IOException ex) {
            Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("InetAdress: " + serverSocket.getInetAddress().getHostAddress() + " Port: " + serverSocket.getLocalPort());

        //enquanto vivo...
        while (true) {
            try {
                //1- abrir o servidor socket
                System.out.println("Aguardando conexão...");
                // 2- aguardar uma conexão
                mySocket = serverSocket.accept();
                //2.1 - depois de aberto, imprimir o endereço da conexão originária
                System.out.println("Conectado a InetAdress: " + mySocket.getInetAddress() + " porta: " + mySocket.getPort());

                setIn(mySocket.getInputStream());
                setOut(mySocket.getOutputStream());

                /*
                 * Pelo procotolo, vamos esperar uma mensagem dizendo o que é que vai ser feito:
                 *  CO, CI (funcionando), LS, LR (TODO)
                 */
                //3 - pegando a intenção
                String intent = receiveMessage();

                //4 - de acordo com cada opção, uma operação vai ser executada
                doOperation(intent);

            } catch (IOException ex) {
                Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    System.out.println("Fechando conexão");
                    mySocket.close();
                    //  serverSocket.close();
                } catch (IOException ex) {
                    Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex);
                }
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

        //Server servidor = Server.getInstance();

        //1 - servidor precisa dar um "ack" para continuar o processo de envio de informações
        sendMessage("ack");

//recebo o projeto de onde eu quero os itens
//        String project = receiveMessage();
//        sendMessage("ack");
//        //recebo qual a revisão eu quero
//        String revN = receiveMessage();

        String fullString = receiveMessage();
        String[] splitted = fullString.split(":");
        String project = splitted[0];
        String revN = splitted[1];

        System.out.println("Received project " + project + " and revN " + revN);

        sendMessage("ack");

        // envio os arquivos
        //TODO precisa chamar o checkout do servidor (que não está implementado)
        ArrayList<ConfigurationItem> items = new ArrayList<ConfigurationItem>();

        // TESTS PURPOSES
        //ArrayList<ConfigurationItem> itemsz = new ArrayList<ConfigurationItem>();

        File file = FileHelper.createTemporaryRandomFile();
        FileOutputStream finp = new FileOutputStream(file);

        UtilStream.fillRandomData(finp, 34);
        finp.close();

        ConfigurationItem item;
        InputStream in = new FileInputStream(file);
        try {
            item = new ConfigurationItem(new URI("/foo/bar"), in, CIType.file, new RevisionID(0), "cleyton");
            items.add(item);
            items.add(item);

        } catch (URISyntaxException ex) {
            Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex);
        }

        // TESTS PURPOSES

        sendConfigurationItems(items);

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
//        project = receiveMessage();
//        //manipula o resultado
//        sendMessage("ack");
//
//        message = receiveMessage();
        //manipula o resultado

        String full = receiveMessage();
        String[] splitted = full.split(":");
        project = splitted[0];
        message = splitted[1];

        sendMessage("ack");

        //receber itens

        System.out.println(project + " " + message + " ok! in Checkin!");

        Collection<ConfigurationItem> configItems = receiveConfigurationItems();

        Server server = Server.getInstance();
        //result = server.ckeckIn(project, user, date, message, (ArrayList<ConfigurationItem>) configItems);
        result = "20102";

        return result;
    }

    private void sendList() {
        //TODO implementar

        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void doInit() throws IOException {

        sendMessage("ack");
        //receber nome do projeto que eu quero criar
//        String project = receiveMessage();
//
//        sendMessage("ack");
//        //receber nome do projeto que eu quero criar
//        String user = receiveMessage();

        String project;
        String user;

        String[] splitted = receiveMessage().split(":");
        project = splitted[0];
        user = splitted[1];

        System.out.println("Project " + project + " created by user " + user);

        //criar a parada
        // Server server = Server.getInstance();
        // server.init(project, project);

    }

    private String doLastRevision() throws IOException {

        sendMessage("ack");

        //receive project name
        String project = receiveMessage();
        System.out.println("Last revision from project " + project);

        //return String.valueOf(Server.getInstance().getLastRevision(project));
        return "20102";
    }

    private void sendLog() throws IOException {
        sendMessage("ack");

//        String first = receiveMessage();
//
//        sendMessage("ack");
//
//        String last = receiveMessage();

        String fullString = receiveMessage();

        String[] splitter = fullString.split(":");

        String first = splitter[0];
        String last = splitter[1];

        System.out.println("Getting log from " + first + " to " + last + ".");

        Collection<RevisionCommited> items = new ArrayList<RevisionCommited>();

        RevisionCommited commit = new RevisionCommited(RevisionID.NEW_REVISION, "cslaviero", "messageTest", Calendar.getInstance().getTime());

        items.add(commit);
        items.add(commit);
        items.add(commit);
        items.add(commit);
        items.add(commit);

        this.sendRevisionCommitedList(items);
    }

    public static void main(String[] args) {
        ServerListener server = new ServerListener();
        server.run();
    }
}
