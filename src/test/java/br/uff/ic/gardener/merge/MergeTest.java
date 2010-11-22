
package br.uff.ic.gardener.merge;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

public class MergeTest {

    private File test1Left;
    private File test1Right;
    private File test1Base;
    private File test1Result;
    private File test1ExpectedResult;
    
    private File test2Left;
    private File test2Right;
    private File test2Base;
    private File test2Result;
    private File test2ExpectedResult;
    
    private File test3Left;
    private File test3Right;
    private File test3Base;
    private File test3Result;
    private File test3ExpectedResult;

    @Before
    public void setUp() throws Exception {
        this.test1Left = getResourceFile("test1Left.txt");
        this.test1Right = getResourceFile("test1Right.txt");
        this.test1Base = getResourceFile("test1Base.txt");
        this.test1Result = getResourceFile("test1Result.txt");
        this.test1ExpectedResult = getResourceFile("test1ExpectedResult.txt");
        
        this.test2Left = getResourceFile("test2Left.txt");
        this.test2Right = getResourceFile("test2Right.txt");
        this.test2Base = getResourceFile("test2Base.txt");
        this.test2Result = getResourceFile("test2Result.txt");
        this.test2ExpectedResult = getResourceFile("test2ExpectedResult.txt");
        
        this.test3Left = getResourceFile("test3Left.txt");
        this.test3Right = getResourceFile("test3Right.txt");
        this.test3Base = getResourceFile("test3Base.txt");
        this.test3Result = getResourceFile("test3Result.txt");
        this.test3ExpectedResult = getResourceFile("test3ExpectedResult.txt");
    }

    @Test
    public void testMerge1() {
    	IMerge merge = new MergeWithRegEx();
    	File mergeResult = merge.merge(this.test1Base, this.test1Left, this.test1Right, this.test1Result);
    	String mergeResultAsString = this.convertFileToString(mergeResult);
    	String expectedResultAsString = this.convertFileToString(this.test1ExpectedResult);
    	assertTrue(expectedResultAsString.equals(mergeResultAsString));
    }
    
    @Test
    public void testMerge2() {
    	IMerge merge = new MergeWithRegEx();
    	File mergeResult = merge.merge(this.test2Base, this.test2Left, this.test2Right, this.test2Result);
    	String mergeResultAsString = this.convertFileToString(mergeResult);
    	String expectedResultAsString = this.convertFileToString(this.test2ExpectedResult);
    	assertTrue(expectedResultAsString.equals(mergeResultAsString));
    }
    
    @Test
    public void testMerge3() {
    	IMerge merge = new MergeWithRegEx();
    	File mergeResult = merge.merge(this.test3Base, this.test3Left, this.test3Right, this.test3Result);
    	String mergeResultAsString = this.convertFileToString(mergeResult);
    	String expectedResultAsString = this.convertFileToString(this.test3ExpectedResult);
    	assertTrue(expectedResultAsString.equals(mergeResultAsString));
    }

    private File getResourceFile(String file) {
        String root = "/merge/";
        String path = root + file;
        URL in = this.getClass().getResource(path);
        try {
			return new File(in.toURI());
		} catch (URISyntaxException e) {
			System.out.println("Error accessing the resource!!!" + e);
			return null;
		}
    }
	
	private String convertFileToString(File file) {
		StringBuilder result = new StringBuilder();

		try {
			BufferedReader baseBufferedReader = new BufferedReader(new FileReader(file));
			String baseLine = baseBufferedReader.readLine();

			while (baseLine != null) {
				result.append(baseLine);
				result.append("\n");
				baseLine = baseBufferedReader.readLine();
			}
		} catch (IOException e) {
			System.out.println("Error accessing the file !!!");
		}
		return result.toString();
	}
}
