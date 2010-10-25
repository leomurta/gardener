package br.uff.ic.gardener.util;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class TokenizerWithQuoteTest {
	
	@Before
	public void setUp() throws Exception {

	}
	
	private void TestTokenizerWithQuoteCustom(String source, String[] dest)
	{
		TokenizerWithQuote twq = new TokenizerWithQuote(source);
		
		ArrayList<String> array = new ArrayList<String>();
		
		while(twq.hasNextToken())
		{
			array.add(twq.nextToken());
		}
		
		org.junit.Assert.assertArrayEquals("Não conseguiu separar direito"
				, array.toArray(new String[array.size()]) 
				, dest
				);
	}
	
	@Test
	public void TestTokenizerWithQuote_1()
	{
		String[] teste = 
		{
			"Sabia",
			"que",
			"tem",
			"algo",
			"errado",
			"nisso?"
		};
		TestTokenizerWithQuoteCustom("Sabia que tem algo errado nisso?", teste);
	
	}
	
	@Test
	public void TestTokenizerWithQuote_4()
	{
		String[] teste = 
		{
			"Quote"	
		};
		
		TestTokenizerWithQuoteCustom("\"Quote\"", teste);
		
	}
	
	@Test
	public void TestTokenizerWithQuote_2()
	{
		String source = "Quero ver se acha um \"quote\" sabia?";
		String[] dest = 
		{
			"Quero",
			"ver",
			"se",
			"acha",
			"um",
			"quote",
			"sabia?"
		};
		
		TestTokenizerWithQuoteCustom(source, dest);
	}
	
	@Test
	public void TestTokenizerWithQuote_3()
	{
		String source = " Olha que legal, um\"quote\" colado no \"um\". E de quebra tiveram dois Quotes!";
		String[] dest = 
		{
			"Olha",
			"que",
			"legal,",
			"um",
			"quote",
			"colado",
			"no",
			"um",
			".",
			"E",
			"de",
			"quebra",
			"tiveram",
			"dois",
			"Quotes!"
		};		
		TestTokenizerWithQuoteCustom(source, dest);
	}
	
	@Test
	public void TestTokenizerWithQuote_5()
	{
		String source = " Ah, Ah, Ah, , Ah , Ah, Ah , Ah Ah Ah Ah AhAhAhAh \"Ah,a,a,h,a,h,a,h,ss\" ahah hhh ahahah!";

		String[] dest = 
		{
			"Ah,",
			"Ah,",
			"Ah," ,
			",",
			"Ah",
			"," ,
			"Ah," ,
			"Ah" ,
			"," ,
			"Ah" ,
			"Ah" ,
			"Ah" ,
			"Ah" ,
			"AhAhAhAh",
			"Ah,a,a,h,a,h,a,h,ss",
			"ahah",
			"hhh",
			"ahahah!"
		};
		
		TestTokenizerWithQuoteCustom(source, dest);
	}
	
}
