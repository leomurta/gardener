package Database;

import com.mongodb.*;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import java.io.*;
import java.io.FileInputStream.*;
import java.io.DataInputStream.*;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

/**
 * Esta classe permite fornecer métodos para acesso e manutenção da
 * base de dados do Sistema de Controle de Versão Gardener.
 * O Gardener é resultado de um trabalho da disciplina de Laboratório de
 * Gerencia de Configuração.
 *
 * Criação em 12 de Outubro de 2010.
 *
 * @author Alessandreia Marta de Oliveira
 *
 */
public class Database {

    //Atributos para conexao
    public String stgServer;
    public int intPort;
    public String stgProject;
    public DB conn;

    //Atributos para inserção dos dados
    public String stgRevisao;
    public String stgData;
    public String stgComentario;
    public String stgUsuario;
    public String stgCaminho;

    /**
     * Verifica sistema operacional para definir raiz do projeto
     *
     * @return      retorna a raiz do projeto, de acordo com o sistema operacional
     */
    private String Gardener_Root()
    {
           String os = System.getProperty("os.name");
           if (os.contains("Windows")) {
               return "c:/Gardener/";
           } else {
               return "/Gardener/";
           }
    }

    /**
     * cria a conexao com o BD
     *
     * @return      retorna a conexao com o BD
     */
    public DB Connection()
    {
            try
		{

                    Mongo connect = new Mongo(this.stgServer, this.intPort);
                    conn = connect.getDB( this.stgProject );
                    return conn;

		}
		catch(Exception err)
        	{
                    err.getMessage();
                    return null;
                }
    }

    /**
     * Cria o diretorio do Repositorio
     *
     * @param stgProject    nome do projeto
     */
    public void createDirRepository(String stgProject)
    {
       File root =  new File(Gardener_Root());
       if (!root.exists()) root.mkdir();

       File dir = new File(Gardener_Root() + stgProject);
       dir.mkdir();

       try{

            FileOutputStream newFile = new FileOutputStream(Gardener_Root() + stgProject + "/configVersion.conf");
            newFile.close();

            FileReader fr = new FileReader(Gardener_Root() + stgProject + "/configVersion.conf");
            BufferedReader buff = new BufferedReader(fr);

            buff.close();

            FileWriter fw = new FileWriter(Gardener_Root() + stgProject + "/configVersion.conf", false);
            fw.write("0");//First Revision
            fw.close();

        }catch(Exception err)
            {
              System.out.println("Erro: " + err.getMessage());
            }

    }

    /**
     * Cria o diretorio da revisao e armazena os metadados no Diretorio do Repositorio de uma determinada versao
     *
     * @param stgProject    nome do projeto
     * @param intRevision   numero da revisao do projeto
     */
    public void storeDirRepository(String stgProject, int intRevision)
    {

        try
        {

            boolean dir = new File(Gardener_Root() + stgProject + "/" + intRevision).mkdir();
            if (dir == true) {
                
                this.storeMetadata();


            }

        }catch(Exception err)
         {
             System.out.println("Erro: " + err.getMessage());
         }

    }

    /**
     * Armazena um arquivo de revisao em uma determinada versao de um projeto
     *
     * @param stgProject    nome do projeto
     * @param intRevision   numero da revisao
     * @param stgFileSource arquivo a ser armazenado
     * @param fileName      nome do arquivo armazenado
     */
    public void storeFileRepository(String stgProject, int intRevision, String stgFileSource, String fileName)
    {
        //streams
        FileInputStream source;
        FileOutputStream target;

        FileChannel fcSource;
        FileChannel fcTarget;

        try
        {
            source = new FileInputStream(stgFileSource);
            target = new FileOutputStream(Gardener_Root() + stgProject + "/" + intRevision + "/" + fileName);

            fcSource  = source.getChannel();
            fcTarget = target.getChannel();

            fcSource.transferTo(0, fcSource.size(), fcTarget);

         }catch(Exception err)
         {
             System.out.println("Erro: " + err.getMessage());
         }

    }

    /**
     * Armazena o metadado no BD
     */
    private void storeMetadata()
    {

        conn =  Connection();
        DBCollection collection = conn.getCollection(this.stgProject);

        BasicDBObject revision = new BasicDBObject();

        revision.put("revisao", this.stgRevisao);
        revision.put("data", this.stgData);
        revision.put("comentario", this.stgComentario);
        revision.put("usuario", this.stgUsuario);
        revision.put("caminho", Gardener_Root() + this.stgProject + "/" + this.stgRevisao);

        collection.insert(revision);

    }

    /**
     * Consulta o metadado
     *
     * @return  retorna o metadado do projeto
     */
    public ArrayList queryMetadata()
    {

        conn =  Connection();
        DBCollection collection = conn.getCollection(this.stgProject);

        BasicDBObject revision = new BasicDBObject();
        revision.put("revisao", this.stgRevisao);

        DBCursor resultCollection = collection.find();
        resultCollection = collection.find(revision);

        ArrayList resultQuery = new ArrayList();

        while(resultCollection.hasNext()) {

            DBObject resultCollectionAux;
            resultCollectionAux = resultCollection.next();

            String user = resultCollectionAux.get("usuario").toString();
            String dt = resultCollectionAux.get("data").toString();
            String mensage = resultCollectionAux.get("comentario").toString();
            String path = resultCollectionAux.get("caminho").toString();

            resultQuery.add(user);
            resultQuery.add(dt);
            resultQuery.add(mensage);
            resultQuery.add(path);

        }

        return resultQuery;

    }

    /**
     * Atualiza revisao de uma revisao de um projeto
     *
     * @param stgProject    nome do projeto
     * @param intRevision   numero da revisao
     */
    public void updateRevision(String stgProject, int intRevision)
    {
        try
        {
            FileWriter fw = new FileWriter(Gardener_Root() + stgProject + "/configVersion.conf", false);
            fw.write(Integer.toString(intRevision));

            fw.close();            

         }
         catch(Exception err)
         {
             System.out.println("Erro: " + err.getMessage());
         }

    }

    /**
     * retorna a ultima revisao de um projeto
     *
     * @param stgProject    nome do projeto
     * @return              numero da revisao
     */
    public int lastRevision(String stgProject)
    {
        try
        {

            int tempRevision;
            FileReader fr = new FileReader(Gardener_Root() + stgProject + "/configVersion.conf");
            BufferedReader buff = new BufferedReader(fr);

            tempRevision = Integer.parseInt(buff.readLine());

            buff.close();

            return tempRevision;

        }
         catch(Exception err)
         {
             return -1;
         }

    }

    /**
     * Lista arquivos de uma revisao de um projeto
     *
     * @param stgProject    nome do projeto
     * @param intRevision   numero da revisao
     * @return              lista de arquivos
     */
    public File[] listFiles(String stgProject, int intRevision)
    {
            File dir = new File(Gardener_Root() + stgProject + "/" + intRevision);
            File[] listFile = dir.listFiles();

            return listFile;
    }
 }