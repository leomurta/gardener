package br.uff.ic.gardener.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

import java.io.FileFilter;
import br.uff.ic.gardener.util.ComparatorFile;

public class FileHelper {
	
	/**
	 * Delete a directory with it files and subdirectories
	 * @param dir the directory 
	 * @throws SecurityException
	 */
	public static void deleteDirTree(File dir) throws SecurityException 
	{
		if(dir.isFile())
		{
			dir.delete();
		}else if(dir.isDirectory())
		{
			for(File f: dir.listFiles())
			{
				deleteDirTree(f);
			}
			dir.delete();
		}
	}

	public static File createTemporaryRandomFile() throws IOException {
		return File.createTempFile("temp", UUID.randomUUID().toString());
	}
	
	/**
	 * Cria um arquivo considerando possíveis pastas no seu nome
	 * @param pathParent
	 * @param name
	 * @return
	 */
	public static File createFile(File pathParent, String name)throws IOException, IllegalArgumentException
	{
		//tenta criar primeiro
		//File newFile = new File(pathParent, name);
		
		if(name == null || name.length() == 0)
			throw new IllegalArgumentException("The parameter "  + name + "cannot be null or empty.");
		
		if(name.indexOf(File.separatorChar) == -1 && name.indexOf('/')==-1)
		{
			File f = new File(pathParent, name);
			f.createNewFile();
			return f;
		}
		
		String[] vecName = name.split(File.separator + '/');
			
		
		for(int i = 0; i < vecName.length-1; i++)
		{
			File fileTemp = new File(pathParent, vecName[i]);
			if(!fileTemp.exists())
			{
				fileTemp.mkdir();
				pathParent = fileTemp;
			}else
			{
				if(!fileTemp.isDirectory())
				{
					throw new IOException(fileTemp.toString() + " do not a directory", null);
				}else
				{
					pathParent = fileTemp;
				}
			}
		}
		
		File f = new File(pathParent, vecName[vecName.length-1]);
		f.createNewFile();
		return f;
	}
	
	
	/**
	 * Encontra os arquivos em uma árvore de diretórios
	 * @param path O diretório raíz a ser procurado
	 * @param collDest A colecção de destino
	 * @param strGlob uma string formato Glob usada para comparação
	 * @param allowPath Se aceita todos os diretórios para buscar ou os elimina usando a regra do glob também.
	 * @return A coleção preenchida
	 */
	public static Collection<File> findFiles(File path, Collection<File> collDest, final String strGlob, final boolean allowPath)
	{
		GlobFilenameFilter filter = new GlobFilenameFilter(strGlob, allowPath);//força permitir os paths
		
		Queue<File> queuePath = new LinkedList<File>();
		queuePath.offer(path);
		
		while(queuePath.size() >0)
		{
			
			path = queuePath.remove();
			File[] childs = path.listFiles(filter);
			for(File fileChild: childs)
			{
				if(fileChild.isDirectory())
				{
					queuePath.offer(fileChild);
				}else
				{
					collDest.add(fileChild);
				}
			}
		}
		
		return collDest;
	}
	
	public static Collection<File> findFiles(File path, Collection<File> collDest, final FileFilter fileFilter)
	{		
		Queue<File> queuePath = new LinkedList<File>();
		queuePath.offer(path);
		
		while(queuePath.size() >0)
		{
			
			path = queuePath.remove();
			File[] childs = path.listFiles(new ORFileFilter(new DirectoryFileFilter(), fileFilter));
			for(File fileChild: childs)
			{
				if(fileChild.isDirectory())
				{
					queuePath.offer(fileChild);
				}else
				{
					collDest.add(fileChild);
				}
			}
		}
		return collDest;	
	}
	
	public static File createTemporaryRandomPath() throws IOException
	{
		File file = createTemporaryRandomFile();
		if(!file.delete())
			throw new IOException("Cannot create temporary random directory");
		
		if(!file.mkdir())
			throw new IOException("Cannot create temporary random directory");
		
		return file;
	}
	
	/**
	 * This method returns a relative path of a file from other. However, it does not work with dots (., ..) 
	 * Because the project do not need it.  
	 * @param pathRadix The source path (example: /public/path/a/)
	 * @param path	The absolute target (example: /public/path/b/text.txt)
	 * @return the relatived path (example: /b/text.txt)
	 */
	public static URI getRelative(File pathRadix, File path)
	{
		return pathRadix.toURI().relativize(path.toURI());
		//return new File(TextHelper.getRelative(pathRadix.toString(), path.toString(), File.separator));
	}
	
	/**
	 * This method returns a relative path of a file from other. However, it does not work with dots (., ..) 
	 * Because the project do not need it.  
	 * @param pathRadix The source path (example: /public/path/a/)
	 * @param path	The absolute target (example: /public/path/b/text.txt)
	 * @return the relatived path (example: /b/text.txt)
	 */
	public static URI getRelative(URI pathRadix, URI path)
	{
		return pathRadix.relativize(path);
		//return new URI(TextHelper.getRelative(pathRadix.toString(), path.toString(), '/'));
	}

	/**
	 * Return a File from a URI what can has somewhere scheme
	 * @param uriServ
	 * @return
	 */
	public static File getFileFromURI(URI uri) 
	{
		String path = uri.getPath();
		StringBuilder sb = new StringBuilder();
		int i = 0; 
		for(; i < path.length(); i++)
		{
			if(path.charAt(i) != '/')
				break;
			//sb.append(path.charAt(i));
			//faznada para ignorar os //// do inicio
		}
		for(; i < path.length(); i++)
		{
			final char c =path.charAt(i); 
			if( c == '/')
				sb.append(File.separator);
			else
				sb.append(c);
		}
		String ret = sb.toString().trim();
		if("".equals(ret))
			return null;
		
		File f = new File(ret);
		return f;
	}

	
	public static boolean comparDirs(File pathA, File pathB)
	{
		if(!pathA.isDirectory() || !pathB.isDirectory())
			return false;
		
		File[] vecA = pathA.listFiles();
		File[] vecB = pathB.listFiles();

		final int size =vecA.length;
		
		if(size != vecB.length)
			return false;
		
		ComparatorFile comparator = new ComparatorFile();
		Arrays.sort(vecA, comparator );
		Arrays.sort(vecB, comparator);
		
		
		for( int i = 0; i < size; i++)
		{
			if(vecA[i].compareTo(vecB[i]) != 0)
				return false;
			else
			{
				if(vecA[i].isDirectory())
				{
					if(!comparDirs(vecA[i], vecB[i]))
						return false;
				}
			}
		}
		
		return true;
	}
}
