package br.uff.ic.gardener.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

public class UtilStream {

	public static final int DEFAULT_BUFFER_SIZE = 2156;

	private static byte[] BUFFER = new byte[DEFAULT_BUFFER_SIZE];

	public static void copy(InputStream input, OutputStream output)
			throws IOException {
		copy(input, output, BUFFER);
	}

	public static void copy(InputStream input, OutputStream output,
			int sizeBuffer) throws IOException {
		byte[] buffer = null;
		if (sizeBuffer != DEFAULT_BUFFER_SIZE)
			buffer = new byte[sizeBuffer];
		else
			buffer = BUFFER;

		copy(input, output, buffer);
	}

	public static void copy(InputStream input, OutputStream output,
			byte[] buffer) throws IOException {
		int byteRead = 0;
		while ((byteRead = input.read(buffer)) > 0) {
			output.write(buffer, 0, byteRead);
		}
	}
	
	public static void fillFile(String strFile, String... strVec) throws IOException
	{
		fillFile(new File(strFile), strVec);
	}
	
	public static void fillStream(OutputStream os, String... strVec)throws IOException
	{
		//PrintWriter pw = new PrintWriter(os, false);
		PrintStream ps = new PrintStream (os);
		for (String str : strVec) 
		{
			ps.format("%s%s", str, getLineSeperator());
		}
		ps.flush();
	}
	
	public static void fillPrintStream(PrintStream ps, String... strVec)throws IOException
	{
		for (String str : strVec) 
		{
			ps.format("%s%s", str, getLineSeperator());
		}
	}
	
	/**
	 * Preenche um arquivo com várias linhas de String
	 * @param file o arquivo a ser preenchido
	 * @param strVec as linhas que o preencherão
	 */
	public static void fillFile(File file, String... strVec)throws IOException
	{
		FileWriter fw;
		fw = new FileWriter(file);
		PrintWriter pw = new PrintWriter(fw);

		for (String str : strVec) {
			pw.append(str + getLineSeperator());
		}
		pw.close();
	
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
	
	public static String getLineSeperator()
	{
		return System.getProperty("line.separator");
	}
}
