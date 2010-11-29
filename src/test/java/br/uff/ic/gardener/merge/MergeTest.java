
package br.uff.ic.gardener.merge;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

public class MergeTest {
    
    protected File test1Left;
    protected File test1Right;
    protected File test1Base;
    protected File test1Result;
    protected File test1ExpectedResult;
    
    protected File test2Left;
    protected File test2Right;
    protected File test2Base;
    protected File test2Result;
    protected File test2ExpectedResult;
    
    protected File test3Left;
    protected File test3Right;
    protected File test3Base;
    protected File test3Result;
    protected File test3ExpectedResult;

    protected File test4Left;
    protected File test4Right;
    protected File test4Base;
    protected File test4Result;
    protected File test4ExpectedResult;

    protected File test5Left;
    protected File test5Right;
    protected File test5Result;
    protected File test5ExpectedResult;

    protected File test6Left;
    protected File test6Right;
    protected File test6Result;
    protected File test6ExpectedResult;
    
    @Before
    public void setUp() throws Exception {
        //test1
    	this.test1Left = this.getResourceFile("test1Left.txt");
        this.test1Right = this.getResourceFile("test1Right.txt");
        this.test1Base = this.getResourceFile("test1Base.txt");
        
        this.test1Result = this.getNewResourceFile("test1Result.txt");
        this.test1ExpectedResult = this.getResourceFile("test1ExpectedResult.txt");
        //test2
        this.test2Left = this.getResourceFile("test2Left.txt");
        this.test2Right = this.getResourceFile("test2Right.txt");
        this.test2Base = this.getResourceFile("test2Base.txt");
        
        this.test2Result = this.getNewResourceFile("test2Result.txt");
        this.test2ExpectedResult = this.getResourceFile("test2ExpectedResult.txt");
        //test3        
        this.test3Left = this.getResourceFile("test3Left.txt");
        this.test3Right = this.getResourceFile("test3Right.txt");
        this.test3Base = this.getResourceFile("test3Base.txt");
        
        this.test3Result = this.getNewResourceFile("test3Result.txt");
        this.test3ExpectedResult = this.getResourceFile("test3ExpectedResult.txt");
        //test4
        this.test4Left = this.getResourceFile("test4Left.txt");
        this.test4Right = this.getResourceFile("test4Right.txt");
        this.test4Base = this.getResourceFile("test4Base.txt");
        
        this.test4Result = this.getNewResourceFile("test4Result.txt");
        this.test4ExpectedResult = this.getResourceFile("test4ExpectedResult.txt");
        //test5
        this.test5Left = this.getResourceFile("test5Left.txt");
        this.test5Right = this.getResourceFile("test5Right.txt");
        
        this.test5Result = this.getNewResourceFile("test5Result.txt");
        this.test5ExpectedResult = this.getResourceFile("test5ExpectedResult.txt");
        //test6
        this.test6Left = this.getResourceFile("test6Left.txt");
        this.test6Right = this.getResourceFile("test6Right.txt");
        
        this.test6Result = this.getNewResourceFile("test6Result.txt");
        this.test6ExpectedResult = this.getResourceFile("test6ExpectedResult.txt");
    }

    @Test
    public void testMerge1() {
    	IMerge merge = new MergeWithRegEx();
    	Boolean hasConflict = null;
    	try {
    		hasConflict = merge.merge(this.test1Left, this.test1Right, this.test1Base, this.test1Result);
		} catch (MergeException e) {
			fail(e.getStackTrace().toString());
		}
    	String mergeResultAsString = this.convertFileToString(this.test1Result);
    	String expectedResultAsString = this.convertFileToString(this.test1ExpectedResult);
    	assertTrue(expectedResultAsString.equals(mergeResultAsString));
    	assertTrue(hasConflict); 
    }
    
    @Test
    public void testMerge2() {
    	IMerge merge = new MergeWithRegEx();
    	Boolean hasConflict = null;
    	try {
    		hasConflict = merge.merge(this.test2Left, this.test2Right, this.test2Base, this.test2Result);
		} catch (MergeException e) {
			fail(e.getStackTrace().toString());
		}
    	String mergeResultAsString = this.convertFileToString(this.test2Result);
    	String expectedResultAsString = this.convertFileToString(this.test2ExpectedResult);
    	assertTrue(expectedResultAsString.equals(mergeResultAsString));
    	assertTrue(hasConflict);
    }
    
    @Test
    public void testMerge3() {
    	IMerge merge = new MergeWithRegEx();
    	Boolean hasConflict = null;
    	try {
    		hasConflict = merge.merge(this.test3Left, this.test3Right, this.test3Base, this.test3Result);
		} catch (MergeException e) {
			fail(e.getStackTrace().toString());
		}
    	String mergeResultAsString = this.convertFileToString(this.test3Result);
    	String expectedResultAsString = this.convertFileToString(this.test3ExpectedResult);
    	assertTrue(expectedResultAsString.equals(mergeResultAsString));
    	assertTrue(hasConflict);
    }

    @Test
    public void testMerge4() {
    	IMerge merge = new MergeWithRegEx();
    	Boolean hasConflict = null;
    	try {
    		hasConflict = merge.merge(this.test4Left, this.test4Right, this.test4Base, this.test4Result);
		} catch (MergeException e) {
			fail(e.getStackTrace().toString());
		}
    	String mergeResultAsString = this.convertFileToString(this.test4Result);
    	String expectedResultAsString = this.convertFileToString(this.test4ExpectedResult);
    	assertTrue(expectedResultAsString.equals(mergeResultAsString));
    	assertTrue(hasConflict);
    }

    @Test
    public void testMerge5WithoutBaseFile() {
    	IMerge merge = new MergeWithRegEx();
    	Boolean hasConflict = null;
    	try {
    		hasConflict = merge.merge(this.test5Left, this.test5Right, this.test5Result);
		} catch (MergeException e) {
			fail(e.getStackTrace().toString());
		}
    	String mergeResultAsString = this.convertFileToString(this.test5Result);
    	String expectedResultAsString = this.convertFileToString(this.test5ExpectedResult);
    	assertTrue(expectedResultAsString.equals(mergeResultAsString));
    	assertTrue(hasConflict);
    }

    @Test
    public void testMerge6WithoutBaseFile() {
    	IMerge merge = new MergeWithRegEx();
    	Boolean hasConflict = null;
    	try {
    		hasConflict = merge.merge(this.test6Left, this.test6Right, this.test6Result);
		} catch (MergeException e) {
			fail(e.getStackTrace().toString());
		}
    	String mergeResultAsString = this.convertFileToString(this.test6Result);
    	String expectedResultAsString = this.convertFileToString(this.test6ExpectedResult);
    	assertTrue(expectedResultAsString.equals(mergeResultAsString));
    	assertTrue(hasConflict);
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
    
    private File getNewResourceFile(String file) {
        String root = "./target/test-classes/merge/";
        String path = root + file;;
		return new File(path);
    }
	
	protected String convertFileToString(File file) {
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
