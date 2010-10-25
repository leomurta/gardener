package br.uff.ic.gardener.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Classe que implementa tokenizer tratando o caso dos quotes ("")
 * @author Marcos
 *
 */
public class TokenizerWithQuote {
	
	
	private static final int INT_END_OF_STREAM = -1;

	public static char[] DEFAULT_ITEMS_SEPARATOR = {'\n', ' ', '\t', '\r'};
	
	private char[] itemsSeparator = DEFAULT_ITEMS_SEPARATOR;
	
	private static int QUOTE_CHAR = '\"';
	
	
	/**
	 * InputStream de origem
	 */
	private InputStream inputStream = null;
	
	private int lastToken = 0;
	
	/**
	 * Construtor, usa um inputStream para realizar o trabalho
	 * @param _inputStream
	 */
	public TokenizerWithQuote(InputStream _inputStream)
	{
		inputStream = _inputStream;
		try {
			lastToken = inputStream.read();
		} catch (IOException e) {
			lastToken = INT_END_OF_STREAM;
		}
	}
	
	public TokenizerWithQuote(String _str)
	{
		this(new ByteArrayInputStream(_str.getBytes()));
	}
	
	private int getLastToken()
	{
		return lastToken;
	}
	
	private int nextChar() throws IOException
	{
		lastToken = inputStream.read();
		return lastToken;
	}
	
	public boolean hasNextToken()
	{
		return lastToken != INT_END_OF_STREAM;
	}
	
	public String nextToken()
	{
		String temp = generateNextToken();
		if(temp != null)
		{
			if(isSeparator(getLastToken()))
				try {
					processSeparator();
				} catch (IOException e) {
					return "";
				}
		}
		return temp;
		
	}
	
	private String generateNextToken()
	{
		try
		{
			if(!hasNextToken())
				return null;
			
			if(isQuote(getLastToken()))
					return nextQuote();
			else if(isSeparator(getLastToken()))
			{
				processSeparator();
				return nextToken();
			}else //word
				return nextValid();
		}catch(IOException e)
		{
			return "";
		}		
	}

	private String nextValid() throws IOException 
	{
		StringBuilder sb = new StringBuilder();
		sb.append((char)getLastToken());		
		int c = 0;
		do
		{
			c = nextChar();
			if(isValid(c))
			{
				sb.append((char)c);
			}else
			{
				break;
			}
		}while(true);
		return sb.toString();
	}
	
	private String nextQuote() throws IOException 
	{
		StringBuilder sb = new StringBuilder();
		int c = 0;
		do
		{
			c = nextChar();
			if(!isQuote(c))
			{
				sb.append((char)c);
			}else
			{
				c = nextChar(); //consome o quote achado
				break;
			}
		}while(true);
		
		return sb.toString();
	}
	

	private void processSeparator() throws IOException 
	{
		int c = 0;
		do
		{
			c = nextChar();
			if(isSeparator(c))
			{
				//faznada
			}else
			{
				break;
			}
		}while(true);
	}

	private boolean isSeparator(int c) {
		for(char other: itemsSeparator)
		{
			if(other == c)
				return true;
		}
		
		return false;
	}

	private boolean isQuote(int q) 
	{
		return q == QUOTE_CHAR;
	}
	
	private boolean isValid(int v)
	{
		return !isQuote(v) & !isSeparator(v) && v != INT_END_OF_STREAM;
	}

	/**
	 * força fechar o stream
	 */
	public void close() {
		
		try {
			inputStream.close();
			lastToken = -1;
		} catch (IOException e) {
			//faznada
		}
			
	}

	
	
	
	
	
	///**
	// * guarda o último item lido
	// */
	//private boolean hasQuoteInLastRead = false;
	//private int lastCharRead = '1';//pode ser qq um menos o quote
	
//	/**
//	 * verifica se o ultimo item lido é um quote. 
//	 * @return
//	 */
//	private boolean hasQuoteInLastRead()
//	{
//		return lastCharRead == QUOTE_CHAR;
//	}
//	
//	/**
//	 * 
//	 */
//	private int readInput() throws IOException
//	{
//		lastCharRead = inputStream.read();
//		return lastCharRead;
//	}
//	
//	/**
//	 * Verifica se o char é um separador
//	 * @param c
//	 * @return
//	 */
//	private boolean isSeparator(int c)
//	{
//		for(char other: itemsSeparator)
//		{
//			if(other == c)
//				return true;
//		}
//		
//		return false;
//	}
//	
//	private boolean isValid(int c)
//	{
//		return !isQuote(c) && !isSeparator(c); 
//	}
//	/**
//	 * Verifica se é um quote
//	 * @param c
//	 * @return
//	 */
//	private static boolean isQuote(int c)
//	{
//		return c == QUOTE_CHAR;
//	}
//	
//	/**
//	 * Construtor, usa um inputStream para realizar o trabalho
//	 * @param _inputStream
//	 */
//	public TokenizerWithQuote(InputStream _inputStream)
//	{
//		inputStream = _inputStream;
//	}
//	
//	public TokenizerWithQuote(String _str)
//	{
//		this(new ByteArrayInputStream(_str.getBytes()));
//	}
//	
//	/**
//	 * Verifica se ainda há Token no InputStream
//	 * @return
//	 */
//	public boolean hasToken()
//	{
//		return this.lastCharRead != -1;
//	}
//	
//	/**
//	 * Obtém um próximo token considerando que está tudo dentro de um quote, ignorando os caracteres separadores.
//	 * @param sb
//	 * @throws IOException
//	 */
//	private void nextQuoteToken(StringBuilder sb) throws IOException
//	{
//		int tempChar = '\0';
//		while((tempChar = readInput()) != -1)
//		{
//			if(!isQuote(tempChar))
//				sb.append((char)tempChar);
//			else
//			{
//				readInput
//				break;
//			}
//		}
//	}
//	
//	/**
//	 * Retorna o próximo token.
//	 * @return
//	 */
//	public String nextToken()
//	{
//		if(!hasToken())
//			return null;
//		
//		StringBuilder b = new StringBuilder();
//		int tempChar = '\0';
//		
//		try {
//			
//			//vê se o último foi um quote
//			if(hasQuoteInLastRead())
//			{
//				nextQuoteToken(b);
//			}else
//			{
//			
//				//lê todos os nulos e para no próximo não nulo
//				while((tempChar = readInput()) != -1)
//				{
//					if(isQuote(tempChar))
//					{
//						nextQuoteToken(b);
//						break;
//					}else if(isValid(tempChar))
//					{
//						b.append((char)tempChar);
//						break;
//					}
//				}
//				
//				while((tempChar = readInput()) != -1)
//				{
//					if(isValid(tempChar))
//						b.append((char)tempChar);
//					else if(isQuote(tempChar))
//					{
//						break;
//					}
//					else//separator
//						break;
//					
//				}
//			}
//		} catch (IOException e) {
//			return "";
//		}
//		return b.toString();
//	}
	
}
