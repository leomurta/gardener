package br.uff.ic.gardener.database;

//~--- non-JDK imports --------------------------------------------------------

import br.uff.ic.gardener.versioning.*;

import com.mongodb.*;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

//~--- JDK imports ------------------------------------------------------------

import java.io.DataInputStream.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.nio.channels.FileChannel;

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
     * Verifica sistema operacional para definir raiz do repositorio
     *
     * @return      retorna a raiz do repositorio, de acordo com o sistema operacional
     */
    private String getRoot() {
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
     * @param rep   repositorio
     * @return      retorna a conexao com o BD
     */
    public DB Connection(Repository rep) {
        try {
            ConfigurationBD serv    = new ConfigurationBD(27017, "localhost");
            Mongo           connect = new Mongo(serv.getServer(), serv.getPort());

            conn = connect.getDB(rep.getRepository());

            return conn;
        } catch (Exception err) {
            err.getMessage();

            return null;
        }
    }

    /**
     * Cria o Repositorio
     *
     * @param rep   repositorio
     */
    public void createRepository(Repository rep) {
        File root = new File(getRoot());

        if (!root.exists()) {
            root.mkdir();
        }

        File dir            = new File(getRoot() + rep.getRepository());
        File dirOutStanding = new File(getRoot() + rep.getRepository() + "/fileOutStanding");

        dir.mkdir();
        dirOutStanding.mkdir();
    }

    /**
     * armazena no repositorio os itens de uma versao
     *
     * @param vers       versao
     */
    public void save(Version vers) {
        FileInputStream  source;
        FileOutputStream target;
        FileChannel      fcSource;
        FileChannel      fcTarget;

        try {
            Repository rep = new Repository(vers.getItemVersion().getProjectItem().getNameProject());
            boolean    dir = new File(getRoot() + rep.getRepository() + "/" + vers.getVersionNumber()).mkdir();

            this.conn = Connection(rep);

            DBCollection  collection = this.conn.getCollection(rep.getRepository());
            BasicDBObject revision   = new BasicDBObject();

            revision.put("version", vers.getVersionNumber());
            revision.put("date", vers.getTransactionVersion().getDate());
            revision.put("messagelog", vers.getTransactionVersion().getMessage());
            revision.put("user", vers.getTransactionVersion().getUserTransaction().getUserName());
            revision.put("path", getRoot() + rep.getRepository() + "/" + vers.getVersionNumber());
            source = (FileInputStream) vers.getItemVersion().getItemAsInputStream();
            target = new FileOutputStream(getRoot() + rep.getRepository() + "/" + vers.getVersionNumber() + "/"
                                          + vers.getItemVersion().getNameItem());
            fcSource = source.getChannel();
            fcTarget = target.getChannel();
            fcSource.transferTo(0, fcSource.size(), fcTarget);
            collection.insert(revision);
            fcSource.close();
            fcTarget.close();
        } catch (Exception err) {
            System.out.println("Erro: " + err.getMessage());
        }
    }

    /**
     * Consulta o metadado
     *
     * @param rep       repositorio
     * @param vers      versao
     * @return  retorna o metadado do repositorio
     */
    public ArrayList queryMetadata(Repository rep, Version vers) {
        this.conn = Connection(rep);

        DBCollection  collection = this.conn.getCollection(rep.getRepository());
        BasicDBObject revision   = new BasicDBObject();

        revision.put("revision", vers.getVersionNumber());

        DBCursor resultCollection = collection.find();

        resultCollection = collection.find(revision);

        ArrayList resultQuery = new ArrayList();

        while (resultCollection.hasNext()) {
            DBObject resultCollectionAux;

            resultCollectionAux = resultCollection.next();

            String user    = resultCollectionAux.get("user").toString();
            String dt      = resultCollectionAux.get("date").toString();
            String mensage = resultCollectionAux.get("messagelog").toString();
            String path    = resultCollectionAux.get("path").toString();

            resultQuery.add(user);
            resultQuery.add(dt);
            resultQuery.add(mensage);
            resultQuery.add(path);
        }

        return resultQuery;
    }

    /**
     * Lista arquivos de uma revisao de um repositorio
     *
     * @param rep       repositorio
     * @param vers      versao
     * @return          lista de arquivos
     */
    public File[] listFiles(Repository rep, Version vers) {
        File   dir      = new File(getRoot() + rep.getRepository() + "/" + vers.getVersionNumber());
        File[] listFile = dir.listFiles();

        return listFile;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
