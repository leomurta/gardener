package br.uff.ic.gardener.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import br.uff.ic.gardener.ConfigurationItem;
import br.uff.ic.gardener.merge.IMerge;
import br.uff.ic.gardener.merge.MergeException;
import br.uff.ic.gardener.merge.MergeWithRegEx;
import br.uff.ic.gardener.util.FileHelper;
import br.uff.ic.gardener.util.UtilStream;

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

	public InputStream merge(		
			InputStream inS,
			InputStream inW,
			InputStream inO
			) throws ClientMergeException
	{
		//cria arquivos
		File f1 	= createFile(inS);
		File f2 	= createFile(inW);
		File fBase	= createFile(inO);
		File fDest  = internalMerge(f1, f2, fBase);
		
		f1.delete();
		f2.delete();
		fBase.delete();
		
		try {
			
			InputStream is = FileHelper.generateByteInputStreamFromFile(fDest);
			fDest.delete();
			return is;
		} catch (FileNotFoundException e) {
			throw new ClientMergeException("Error at create File Destiny", e);
		} catch (IOException e) {
			throw new ClientMergeException("Error at generate InputStream", e);
		} 				
	}
	
	public InputStream merge(		
			Reader inS,
			Reader inW
			) throws ClientMergeException
	{
		//cria arquivos
		File f1 	= createFile(inS);
		File f2 	= createFile(inW);
		File fDest  = internalMerge(f1, f2);
		
		f1.delete();
		f2.delete();
		
		try {
			
			InputStream is = FileHelper.generateByteInputStreamFromFile(fDest);
			fDest.delete();
			return is;
		} catch (FileNotFoundException e) {
			throw new ClientMergeException("Error at create File Destiny", e);
		} catch (IOException e) {
			throw new ClientMergeException("Error at generate InputStream", e);
		} 			
	}
	public InputStream merge(		
			InputStream inS,
			InputStream inW
			) throws ClientMergeException
	{
		//cria arquivos
		File f1 	= createFile(inS);
		File f2 	= createFile(inW);
		File fDest  = internalMerge(f1, f2);
		
		f1.delete();
		f2.delete();
		
		try {
			
			InputStream is = FileHelper.generateByteInputStreamFromFile(fDest);
			fDest.delete();
			return is;
		} catch (FileNotFoundException e) {
			throw new ClientMergeException("Error at create File Destiny", e);
		} catch (IOException e) {
			throw new ClientMergeException("Error at generate InputStream", e);
		} 				
	}
	
	
	
	public InputStream merge(		
			ConfigurationItem ciServ,
			ConfigurationItem ciWork
			) throws ClientMergeException
	{
		//cria arquivos
		File f1 	= createFile(ciServ);
		File f2 	= createFile(ciWork);
		File fBase	= f2;
		File fDest  = internalMerge(f1, f2, fBase);
		
		f1.delete();
		f2.delete();
		//fBase.delete();
		
		try {
			
			InputStream is = FileHelper.generateByteInputStreamFromFile(fDest);
			fDest.delete();
			return is;
		} catch (FileNotFoundException e) {
			throw new ClientMergeException("Error at create File Destiny", e);
		} catch (IOException e) {
			throw new ClientMergeException("Error at generate InputStream", e);
		} 				
	}
	
	/**
	 * Two away merge	
	 */
	public InputStream merge2(
	ConfigurationItem ciServ,
	ConfigurationItem ciWork
	) throws ClientMergeException
	{
		//cria arquivos
		File f1 	= createFile(ciServ);
		File f2 	= createFile(ciWork);
		
		lastConflict = false;
		IMerge realMerge =  new MergeWithRegEx();
		//faz o merge	
		File fDest = createFile();
		try
		{
			Boolean b = realMerge.merge(f1, f2, fDest);
			lastConflict = (b!=null)?b:false;
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		f1.delete();
		f2.delete();
		
		try {
			
			InputStream is = FileHelper.generateByteInputStreamFromFile(fDest);
			fDest.delete();
			return is;
		} catch (FileNotFoundException e) {
			throw new ClientMergeException("Error at create File Destiny", e);
		} catch (IOException e) {
			throw new ClientMergeException("Error at generate InputStream", e);
		} 
		
		
	}
	
	private File internalMerge(File f1, File f2, File fBase) throws ClientMergeException
	{
		lastConflict = false;
		IMerge realMerge =  new MergeWithRegEx();
		//faz o merge	
		File fDest = createFile();
		try
		{
			Boolean b = realMerge.merge(f1, f2, fBase, fDest);
			lastConflict = (b!=null)?b:false;
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		f1.delete();
		f2.delete();
		fBase.delete();
		
		return fDest;
	}
	
	private File internalMerge(File f1, File f2) throws ClientMergeException
	{
		lastConflict = false;
		IMerge realMerge =  new MergeWithRegEx();
		//faz o merge	
		File fDest = createFile();
		try
		{
			Boolean b = realMerge.merge(f1, f2, fDest);
			lastConflict = (b!=null)?b:false;
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		f1.delete();
		f2.delete();
		
		return fDest;
	}
	
	/**
	 * 
	 * @param ciLast ci of the Last version in the serv
	 * @param ciWork ci of workspace (a version <= ciLast and it can be modified by user.
	 * @param ciBase ci in the same version of workspace
	 * @return
	 * @throws MergeException
	 * @throws ClientMergeException
	 */
	public InputStream merge(
			ConfigurationItem ciLast,
			ConfigurationItem ciWork,
			ConfigurationItem ciBase) throws ClientMergeException
	{
		//cria arquivos
		File f1 	= createFile(ciLast);
		File f2 	= createFile(ciWork);
		File fBase	= createFile(ciBase);
		File fDest  = internalMerge(f1, f2, fBase);
		
		f1.delete();
		f2.delete();
		fBase.delete();
		
		try {
			
			InputStream is = FileHelper.generateByteInputStreamFromFile(fDest);
			fDest.delete();
			return is;
		} catch (FileNotFoundException e) {
			throw new ClientMergeException("Error at create File Destiny", e);
		} catch (IOException e) {
			throw new ClientMergeException("Error at generate InputStream", e);
		} 		
	}
	/*public InputStream merge(ConfigurationItem ciServ,
			ConfigurationItem ciWork) throws MergeException, ClientMergeException
	{
		lastConflict = false;
		IMerge realMerge =  new MergeWithRegEx();
		//cria arquivos
		File f1 	= createFile(ciServ);
		File f2 	= createFile(ciWork);
		File fDest  = createFile();
		//faz o merge	
		try
		{
			Boolean b = realMerge.merge(f1, f2, f1, fDest);
			lastConflict = (b!=null)?b:false;
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		f1.delete();
		f2.delete();
		try {
			
			InputStream is = FileHelper.generateByteInputStreamFromFile(fDest);
			fDest.delete();
			return is;
		} catch (FileNotFoundException e) {
			throw new ClientMergeException("Error at create File Destiny", e);
		} catch (IOException e) {
			throw new ClientMergeException("Error at generate InputStream", e);
		} 
	}
	
	
	public InputStream merge2(ConfigurationItem ciServ,
			ConfigurationItem ciWork) throws MergeException, ClientMergeException
	{
		lastConflict = false;
		IMerge realMerge =  new MergeWithRegEx();
		//cria arquivos
		File f1 	= createFile(ciServ);
		File f2 	= createFile(ciWork);
		File fDest  = createFile();
		//faz o merge	
		try
		{
			Boolean b = realMerge.merge(f1, f2, fDest);
			lastConflict = (b!=null)?b:false;
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		f1.delete();
		f2.delete();
		try {
			
			InputStream is = FileHelper.generateByteInputStreamFromFile(fDest);
			fDest.delete();
			return is;
		} catch (FileNotFoundException e) {
			throw new ClientMergeException("Error at create File Destiny", e);
		
		} catch (IOException e) {
			throw new ClientMergeException("Error at generate InputStream", e);
		} 
		
	}
*/
	
	private File createFile() throws ClientMergeException
	{
		File f = new File(pathTemp, Long.toString(fileID)+".tmp");
		try {
			f.createNewFile();
			fileID++;
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
			InputStream in = ci.getItemAsInputStream();
			UtilStream.copy(in, fOut);
			fOut.close();
		} catch (FileNotFoundException e) {
			throw new ClientMergeException("Cannot open file " + f.toString(), e);
		} catch (IOException e) {
			throw new ClientMergeException("File output problem: " + f.toString(), e);
		}
		
		return f;
	}
	

	private File createFile(Reader in) throws ClientMergeException {
		File f = createFile();
		FileWriter fOut = null;
		try {
			fOut = new FileWriter(f);
			//in.r
			char[] cbuf = new char[1024];
			in.read(cbuf);
			int byteRead = 0;
	        while ((byteRead = in.read(cbuf)) > 0) {
	        	fOut.write(cbuf, 0, byteRead);
	        }
			fOut.close();
		} catch (FileNotFoundException e) {
			throw new ClientMergeException("Cannot open file " + f.toString(), e);
		} catch (IOException e) {
			throw new ClientMergeException("File output problem: " + f.toString(), e);
		}
		
		return f;
	}
	
	private File createFile(InputStream in) throws ClientMergeException {
		File f = createFile();
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
			UtilStream.copy(in, fOut);
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
