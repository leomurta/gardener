package br.uff.ic.gardener.util;

//~--- JDK imports ------------------------------------------------------------

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;


/**
 * Classe para operações rotineiras de manipulação de Streams
 */
public class UtilStream {
	public static final int DEFAULT_BUFFER_SIZE = 2156;

	private static byte[] BUFFER = new byte[DEFAULT_BUFFER_SIZE];

    private static String ENCONDING = "UTF-8";

    private static char[] CHARBUFFER = new char[DEFAULT_BUFFER_SIZE];
	
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


    /**
     *
     * @param input
     * @param output
     * @throws IOException
     */
    public static void copy( InputStream input, OutputStream output ) throws IOException {
        copy( input, output, BUFFER );
    }

    /**
     *
     * @param input
     * @param output
     * @param sizeBuffer
     * @throws IOException
     */
    public static void copy( InputStream input, OutputStream output, int sizeBuffer ) throws IOException {
        byte[] buffer = null;

        if (sizeBuffer != DEFAULT_BUFFER_SIZE) {
            buffer = new byte[sizeBuffer];
        } else {
            buffer = BUFFER;
        }

        copy( input, output, buffer );
    }

    /**
     *
     * @param input
     * @param output
     * @param buffer
     * @throws IOException
     */
    public static void copy( InputStream input, OutputStream output, byte[] buffer ) throws IOException {
        int byteRead = 0;

        while ((byteRead = input.read( buffer )) > 0) {
            output.write( buffer, 0, byteRead );
        }
    }

    /**
     * Method description
     *
     *
     * @param strFile
     * @param strVec
     *
     * @throws IOException
     */
    public static void fillFile( String strFile, String... strVec ) throws IOException {
        fillFile( new File( strFile ), strVec );
    }


    /**
     * Convert InputStream to String (only support UTF-8).
     * @param input
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     * @throws InterruptedException
     */
    public static String toString( InputStream input )
            throws UnsupportedEncodingException, IOException, InterruptedException {
        StringBuilder     text   = new StringBuilder();
        InputStreamReader reader = new InputStreamReader( input, ENCONDING );
        int               read   = 0;

        read = reader.read( CHARBUFFER );

        while (read > -1) {
            if (read > 0) {
                text.append( CHARBUFFER, 0, read );
            } else {
                Thread.sleep( 50 );
            }

            read = reader.read( CHARBUFFER );
        }

        reader.close();

        return text.toString();
    }

    /**
     * Method description
     *
     *
     * @param output
     *
     * @return
     *
     * @throws UnsupportedEncodingException
     */
    public static String toString( ByteArrayOutputStream output ) throws UnsupportedEncodingException {
        return output.toString( ENCONDING );
    }

    /**
     * Method description
     *
     *
     * @param text
     *
     * @return
     *
     * @throws IOException
     */
    public static OutputStream toOutputStream( String text ) throws IOException 
    {
        byte[]                stringByteArray = text.getBytes();
        ByteArrayOutputStream out             = new ByteArrayOutputStream( stringByteArray.length );

        out.write( stringByteArray );
        out.close();

        return out;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
