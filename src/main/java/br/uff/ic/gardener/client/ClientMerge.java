package br.uff.ic.gardener.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import br.uff.ic.gardener.ConfigurationItem;
import br.uff.ic.gardener.merge.IMerge;
import br.uff.ic.gardener.merge.MergeException;
import br.uff.ic.gardener.merge.MergeWithRegEx;
import br.uff.ic.gardener.util.FileHelper;
import br.uff.ic.gardener.util.UtilStream;
import br.uff.ic.gardener.workspace.Workspace;

/**
 * classe que encapsula o trabalho de merge
 * @author Marcos
 *
 */
public class ClientMerge {

	public class ClientMergeException extends Exception
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 920405757395138678L;

		public ClientMergeException(String msg, Throwable t) {
			super(msg, t);			
		}
		
	}
	private File pathTemp = null;

	private long fileID = 0;
	
	private boolean lastConflict = false;
	
	public ClientMerge() throws IOException
	{
		pathTemp = FileHelper.createTemporaryRandomPath();
	}
	
	public InputStream merge(ConfigurationItem ciServ,
			ConfigurationItem ciWork, Workspace workspace) throws MergeException, ClientMergeException
	{
		lastConflict = false;
		IMerge realMerge =  new MergeWithRegEx();
		//cria arquivos
		File f1 	= createFile(ciServ);
		File f2 	= createFile(ciWork);
		File fBase	= createFile();
		File fDest  = createFile();
		//faz o merge		
		lastConflict = realMerge.merge(f1, f2, fBase, fDest);
		
		f1.delete();
		f2.delete();
		fBase.delete();
		try {
			
			return new FileInputStream(fDest);
		} catch (FileNotFoundException e) {
			throw new ClientMergeException("Error at create File Destiny", e);
		}
	}

	
	private File createFile() throws ClientMergeException
	{
		File f = new File(pathTemp, Long.toString(fileID)+".tmp");
		try {
			f.createNewFile();
		} catch (IOException e) {
			throw new ClientMergeException("Error in create file" + f.toString(), e);
		}
		return f;
	}
	private File createFile(ConfigurationItem ci) throws ClientMergeException {
		File f = createFile();
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
			UtilStream.copy(ci.getItemAsInputStream(), fOut);
			fOut.close();
		} catch (FileNotFoundException e) {
			throw new ClientMergeException("Cannot open file " + f.toString(), e);
		} catch (IOException e) {
			throw new ClientMergeException("File output problem: " + f.toString(), e);
		}
		
		return f;
	}

	public void close()
	{
		FileHelper.deleteDirTree(pathTemp);
	}

	public boolean lastConflict() {
		return lastConflict;
	}
}
