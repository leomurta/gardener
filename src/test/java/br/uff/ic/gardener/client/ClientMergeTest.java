package br.uff.ic.gardener.client;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import junit.framework.Assert;


import org.junit.Test;

import br.uff.ic.gardener.ConfigurationItem;
import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.client.ClientMerge.ClientMergeException;
import br.uff.ic.gardener.merge.MergeException;
import br.uff.ic.gardener.merge.MergeTest;
import br.uff.ic.gardener.util.FileHelper;
import br.uff.ic.gardener.util.UtilStream;

public class ClientMergeTest extends MergeTest {

	private void genericTest(File a, File b, File base, File expected)
	{
		//Merge merge = new MergeWithRegEx();
		ClientMerge merge = null;
		try {
			merge = new ClientMerge();
		} catch (IOException e1) {
			fail(e1.getStackTrace().toString());
		}
    	Boolean hasConflict = null;
    	InputStream in = null;
    	try {
    		in = merge.merge(
    				new ConfigurationItem(new URI("a"), new FileInputStream(a), RevisionID.LAST_REVISION),
    				new ConfigurationItem(new URI("b"), new FileInputStream(b), RevisionID.LAST_REVISION),
    				new ConfigurationItem(new URI("base"), new FileInputStream(base), RevisionID.LAST_REVISION)
    				);
    		hasConflict = merge.lastConflict();
		} catch (MergeException e) {
			fail(e.getStackTrace().toString());
		} catch (FileNotFoundException e) {
			fail(e.getStackTrace().toString());
		} catch (ClientMergeException e) {
			fail(e.getStackTrace().toString());
		} catch (URISyntaxException e) {
			fail(e.getStackTrace().toString());
		}
		
		File tempResult = null;
		FileOutputStream fos = null;
		try {
			tempResult = FileHelper.createTemporaryRandomFile();
			fos = new FileOutputStream(tempResult);
			UtilStream.copy(in, fos);
		} catch (IOException e) {
			fail(e.getStackTrace().toString());
		}
		finally
		{
			try {
				if(in !=null)in.close();
				if(fos!=null)fos.close();
			} catch (IOException e) {
				fail(e.getStackTrace().toString());
			}
		}
		
    	String mergeResultAsString = this.convertFileToString(tempResult);
    	String expectedResultAsString = this.convertFileToString(expected);
    	Assert.assertEquals(expectedResultAsString, mergeResultAsString);
    	assertTrue(hasConflict); 
    	
		if(tempResult!=null)tempResult.delete();
	}
	
	private void genericTest(File a, File b, File expected)
	{
		//Merge merge = new MergeWithRegEx();
		ClientMerge merge = null;
		try {
			merge = new ClientMerge();
		} catch (IOException e1) {
			fail(e1.getStackTrace().toString());
		}
    	Boolean hasConflict = null;
    	InputStream in = null;
    	try {
    		in = merge.merge2(
    				new ConfigurationItem(new URI("a"), new FileInputStream(a), RevisionID.LAST_REVISION),
    				new ConfigurationItem(new URI("b"), new FileInputStream(b), RevisionID.LAST_REVISION)
    				);
    		hasConflict = merge.lastConflict();
		} catch (MergeException e) {
			fail(e.getStackTrace().toString());
		} catch (FileNotFoundException e) {
			fail(e.getStackTrace().toString());
		} catch (ClientMergeException e) {
			fail(e.getStackTrace().toString());
		} catch (URISyntaxException e) {
			fail(e.getStackTrace().toString());
		}
		
		File tempResult = null;
		FileOutputStream fos = null;
		try {
			tempResult = FileHelper.createTemporaryRandomFile();
			fos = new FileOutputStream(tempResult);
			UtilStream.copy(in, fos);
		} catch (IOException e) {
			fail(e.getStackTrace().toString());
		}
		finally
		{
			try {
				if(in !=null)in.close();
				if(fos!=null)fos.close();
			} catch (IOException e) {
				fail(e.getStackTrace().toString());
			}
		}
		
    	String mergeResultAsString = this.convertFileToString(tempResult);
    	String expectedResultAsString = this.convertFileToString(expected);
    	Assert.assertEquals(expectedResultAsString, mergeResultAsString);
    	assertTrue(hasConflict); 
		if(tempResult!=null)tempResult.delete();
	}
	
	@Test
	public void test1()
	{
		genericTest(this.test1Left, this.test1Right, this.test1Base, this.test1Result);
	}
	
	@Test
	public void test2()
	{
		genericTest(this.test2Left, this.test2Right, this.test2Base, this.test2Result);
	}
	
	@Test
	public void test3()
	{
		genericTest(this.test3Left, this.test3Right, this.test3Base, this.test3Result);
	}
	
	@Test
	public void test4()
	{
		genericTest(this.test4Left, this.test4Right, this.test4Base, this.test4Result);
	}
	
	@Test
	public void test5()
	{
		genericTest(this.test5Left, this.test5Right, this.test5Result);
	}
	
	@Test
	public void test6()
	{
		genericTest(this.test6Left, this.test6Right, this.test6Result);
	}
	
}
