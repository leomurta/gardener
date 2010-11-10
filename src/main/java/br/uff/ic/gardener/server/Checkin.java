
package br.uff.ic.gardener.server;

import br.uff.ic.gardener.versioning.Item;
import br.uff.ic.gardener.versioning.Project;
import br.uff.ic.gardener.versioning.Transaction;
import br.uff.ic.gardener.versioning.User;
import br.uff.ic.gardener.versioning.Version;
import br.uff.ic.gardener.database.*;
import br.uff.ic.gardener.*;

import java.util.*;
import java.io.*;
import java.io.FileInputStream;
import java.io.DataInputStream.*;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

//classe OpenCommand
/**
 *
 * @author Evaldo de Oliveira
 */
public class Checkin extends Command {
    
    private int state;
    
    /**
     *
     * @param project
     * @param user
     * @param date
     * @param message
     * @param path
     * @param itens
     */
    @Override
	public void execute(String project
                      , String user
                      , String date
                      , String message                      
                      , ArrayList<ConfigurationItem> item) {

             FileInputStream source;
             FileOutputStream target;

             FileChannel fcSource;
             FileChannel fcTarget;

             ArrayList<String> it = new ArrayList<String>();
             ArrayList<String> tp = new ArrayList<String>();

             int j;
             int i;

             try{                
                
                LogCommandServer logCommand = LogCommandServer.getInstance();
                Item ci = new Item();

                for(i = 0; i < item.size(); i++)
                {
                    ci.setNameItem(item.get(i).getUri().toString());
                    ci.setItemAsInputStream(item.get(i).getItemAsInputStream());

                    for(j = item.get(i).getUri().toString().length()-1; j > 0; j--)
                    {
                        if (item.get(i).getUri().toString().charAt(j)=='/')
                            break;
                    }

                    source = (FileInputStream)item.get(i).getItemAsInputStream();
                    target = new FileOutputStream("/gardener/" + project + "/fileOutStanding/" + item.get(i).getUri().toString().substring(j+1));

                    fcSource  = source.getChannel();
                    fcTarget = target.getChannel();

                    fcSource.transferTo(0, fcSource.size(), fcTarget);

                    it.add(item.get(i).getUri().toString());
                    tp.add(item.get(i).getType().toString());
                    
                }

                if (logCommand.commandOutStanding(this.getClass().getSimpleName(), project).equals("FALSE"))
                {
                    logCommand.addLog(this.getClass().getSimpleName(), user, project);
                    logCommand.addOutStanding(this.getClass().getSimpleName(), project, user, date, message, tp, it);

                     this.setState(1);
                 }
                
             }catch(Exception err){
                 System.out.println(err.getMessage());
             }

	}

    /**
     * 
     * @param project
     */
    @Override
        public void unExecute(String project)
        {
            LogCommandServer logComm = LogCommandServer.getInstance();

            this.setState(3);

            logComm.updateLog(this.getClass().getSimpleName(), project, this.getState());

        }


    /**
     *
     * @param project
     */
    @Override
        public void commit(String project)
        {

            int i;

            try{
                ArrayList outStanding;                

                LogCommandServer logCommand = LogCommandServer.getInstance();
                Version vers = new Version();                
                Database db = new Database();
                Item ci = new Item();                

                outStanding = logCommand.regOutStanding(this.getClass().getSimpleName(), project);

                File dir = new File("/gardener/" + project + "/fileOutStanding/");
                File[] listFile = dir.listFiles();

                int currentVersion = Integer.parseInt(vers.getCurrentVersionProject(project)) + 1;
                int nextVersion    = currentVersion + 1;

                for (i = 0; i < listFile.length; i++)
                {

                    ci.createItem(
                            project
                          , currentVersion
                          , nextVersion
                          , outStanding.get(0).toString()
                          , outStanding.get(1).toString()
                          , outStanding.get(2).toString()
                          , project+"-"+listFile[i].getName().toString());
                          
                          Transaction trans = new Transaction(outStanding.get(2).toString(), outStanding.get(1).toString(), "ci");
                          User user = new User(outStanding.get(0).toString());
                          Project proj = new Project(project);

                          ci.setNameItem(listFile[i].getName().toString());
                          ci.setItemAsInputStream(new FileInputStream(listFile[i].getAbsolutePath()));
                    
                          vers.transaction = trans;
                          vers.transaction.user = user;
                          vers.setVersionNumber(Integer.toString(currentVersion));
                          vers.item = ci;
                          vers.item.projectItem = proj;

                          db.save(vers);                          

                }                                                                               

                this.setState(2);

                logCommand.updateLog(this.getClass().getSimpleName(),project, this.getState());

            }catch(Exception err){
                System.out.println(err.getMessage());
            }

        }

        public void setState(int state)
        {
            this.state = state;
        }

        public int getState()
        {
            return this.state;
        }
}
