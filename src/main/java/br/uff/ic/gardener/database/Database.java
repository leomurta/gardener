package br.uff.ic.gardener.database;

import br.uff.ic.gardener.versioning.*;

import com.mongodb.*;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import java.io.FileInputStream;
import java.io.DataInputStream.*;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.io.File;
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

    private DB conn;

    /**
     * Verifica sistema operacional para definir raiz do repeto
     *
     * @return      retorna a raiz do repeto, de acordo com o sistema operacional
     */
    private String getRoot()
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
     * @param serv
     * @return      retorna a conexao com o BD
     */
    public DB Connection(Repository rep)
    {
            try
		{

                    ConfigurationBD serv = new ConfigurationBD(27017, "localhost");

                    Mongo connect = new Mongo(serv.getServer(), serv.getPort());
                    conn = connect.getDB(rep.getRepository());
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
     * @param serv
     */
    public void createRepository(Repository rep)
    {
       File root =  new File(getRoot());
       if (!root.exists()) root.mkdir();

       File dir = new File(getRoot() + rep.getRepository());
       File dirOutStanding = new File(getRoot() + rep.getRepository() + "/fileOutStanding");

       dir.mkdir();
       dirOutStanding.mkdir();

    }

    /**
     * Cria o diretorio da revisao e armazena os metadados no Diretorio do Repositorio de uma determinada versao
     *
     * @param serv
     * @param vers
     */
    public void save(Version vers)
    {

        FileInputStream source;
        FileOutputStream target;

        FileChannel fcSource;
        FileChannel fcTarget;

        try
        {
                Repository rep = new Repository(vers.item.projectItem.getNameProject());

                boolean dir = new File(getRoot() + rep.getRepository() + "/" + vers.getVersionNumber()).mkdir();                

                this.conn =  Connection(rep);
                DBCollection collection = this.conn.getCollection(rep.getRepository());

                BasicDBObject revision = new BasicDBObject();

                revision.put("version", vers.getVersionNumber());
                revision.put("date", vers.transaction.getDate());
                revision.put("messagelog", vers.transaction.getMessage());
                revision.put("user", vers.transaction.user.getUserName());
                revision.put("path", getRoot() + rep.getRepository() + "/" + vers.getVersionNumber());

                source = (FileInputStream)vers.item.getItemAsInputStream();
                target = new FileOutputStream(getRoot() + rep.getRepository() + "/" + vers.getVersionNumber() + "/" + vers.item.getNameItem());

                fcSource  = source.getChannel();
                fcTarget = target.getChannel();

                fcSource.transferTo(0, fcSource.size(), fcTarget);

                collection.insert(revision);

                fcSource.close();
                fcTarget.close();


        }catch(Exception err)
         {
             System.out.println("Erro: " + err.getMessage());
         }

    }    

    /**
     * Consulta o metadado
     *
     * @param serv
     * @param vers
     * @return  retorna o metadado do repeto
     */
    public ArrayList queryMetadata(Repository rep, Version vers)
    {

        this.conn =  Connection(rep);
        DBCollection collection = this.conn.getCollection(rep.getRepository());

        BasicDBObject revision = new BasicDBObject();
        revision.put("revision", vers.getVersionNumber());

        DBCursor resultCollection = collection.find();
        resultCollection = collection.find(revision);

        ArrayList resultQuery = new ArrayList();

        while(resultCollection.hasNext()) {

            DBObject resultCollectionAux;
            resultCollectionAux = resultCollection.next();

            String user = resultCollectionAux.get("user").toString();
            String dt = resultCollectionAux.get("date").toString();
            String mensage = resultCollectionAux.get("messagelog").toString();
            String path = resultCollectionAux.get("path").toString();

            resultQuery.add(user);
            resultQuery.add(dt);
            resultQuery.add(mensage);
            resultQuery.add(path);

        }

        return resultQuery;

    }
    /**
     * Lista arquivos de uma revisao de um repeto
     *
     * @param pServ
     * @param pVers
     * @return              lista de arquivos
     */
    public File[] listFiles(Repository rep, Version vers)
    {
            File dir = new File(getRoot() + rep.getRepository() + "/" + vers.getVersionNumber());
            File[] listFile = dir.listFiles();

            return listFile;
    }
 }