/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.gardener.comm;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cleyton
 */
public class ServerListener {

    ServerSocket serverSocket;
    int filesize=6022386; // filesize temporary hardcoded

    public ServerListener() {
    }

    public void listen() {
      //  long start = System.currentTimeMillis();
        Socket mySocket = new Socket();
        int bytesRead;
        int current = 0;
        try {
            //abrindo o servidor...
            serverSocket = new ServerSocket(20102);
            System.out.println("InetAdress: " + serverSocket.getInetAddress());

            // esperado alguém conectar: podemos criar uma thread para permitir múltiplas conexões
            mySocket = serverSocket.accept();
            //imprime o endereço do socket aberto
            System.out.println("InetAdress: " + mySocket.getInetAddress()+" porta: "+mySocket.getPort());

            //BufferedReader br = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));

           // System.out.println("Connecting...");

            // receive file
            //criando o bytearray que vai receber o arquivo
            byte[] mybytearray = new byte[filesize];

            //pegando o inputstream, que vai receber o arquivo (o byte[])
            
            InputStream is = mySocket.getInputStream();
          
            FileOutputStream fos = new FileOutputStream("C:\\Users\\Cleyton\\Desktop\\text.txt");
            BufferedOutputStream bos = new BufferedOutputStream(fos);

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
            bos.write(mybytearray, 0, current);
            bos.flush();
            long end = System.currentTimeMillis();
           // System.out.println(end - start);
            System.out.println("Sucesso! I guess so...");
            bos.close();

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

    public static void main(String[] args) {
        ServerListener sl = new ServerListener();
        sl.listen();
    }
}
