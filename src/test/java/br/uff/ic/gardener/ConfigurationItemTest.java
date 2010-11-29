package br.uff.ic.gardener;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import br.uff.ic.gardener.CIType;
import br.uff.ic.gardener.ConfigurationItem;
import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.util.FileHelper;

/**
 * Test the Configuration Item in principal the FileInputStream stress-open capacity 
 * @author Marcos
 *
 */
public class ConfigurationItemTest 
{
	static byte[] CHAR_BIG_LIST = null;
	
	static int SIZE_TEST = 1024;
	@BeforeClass
	public static void setUpClass()
	{
		CHAR_BIG_LIST = new byte[SIZE_TEST];
		Random random = new Random();
		random.nextBytes(CHAR_BIG_LIST);
	}
	
	@AfterClass
	static public void tearDownClass()
	{
		
	}
	
	@Test
	public void stressFileInputStream()
	{
		File path = null;
		try {
			path = FileHelper.createTemporaryRandomPath();
		} catch (IOException e) {
			fail("Do not create temporary directory");
			FileHelper.deleteDirTree(path);
		}
		
		List<ConfigurationItem> arrayCI = new LinkedList<ConfigurationItem>();
		
		for(int i = 0; i < SIZE_TEST; i++)
		{
			File temp = new File(path, String.format("%d", i));
			try {
				temp.createNewFile();
				FileOutputStream fileOutputStream = new FileOutputStream(temp);
				fileOutputStream.write(CHAR_BIG_LIST);
				fileOutputStream.close();
			} catch (IOException e) {
				fail("Não foi possível criar o arquivo: " + temp.toString());
				FileHelper.deleteDirTree(path);
			}
			
			try {
				ConfigurationItem item = new ConfigurationItem(path.toURI().relativize(temp.toURI()), new FileInputStream(temp),CIType.file, RevisionID.ZERO_REVISION, "eu, o teste");
				arrayCI.add(item);
			} catch (FileNotFoundException e) {
				fail(String.format("Do not open file %s", temp.toString()));
			}
		}
		
		
		//tenta abrir um de cada vez
		for(ConfigurationItem ci: arrayCI)
		{
			InputStream is = ci.getItemAsInputStream();
			byte [] tempChar = new byte[CHAR_BIG_LIST.length];
			try {
				is.read(tempChar);
			} catch (IOException e) {
				fail("Cannot read InputStream from ConfigurationItem");
			}
			assertArrayEquals(CHAR_BIG_LIST, tempChar);
		}
		
		InputStream[] arrayIS = new InputStream[arrayCI.size()];
		//tenta abrir todo mundo de uma vez só
		{
			int i = 0;
			for(ConfigurationItem ci: arrayCI)
			{
				InputStream is = ci.getItemAsInputStream();
				arrayIS[i] = is;
				i++;
			}
			
			for(int j = 0; j < SIZE_TEST; j++)
			{
				for(InputStream is: arrayIS)
				{
					try {
						is.read();
					} catch (IOException e) {
						fail("Cannot read inputstreams in the same time");
					}
				}
			}
		}
		
		FileHelper.deleteDirTree(path);
	}
}
