package br.uff.ic.gardener.merge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.OperationNotSupportedException;

public class MergeWithRegEx implements IMerge {

	@Override
	public Boolean merge(File file1, File file2, File destinyFile) throws MergeException {
		StringBuilder diffRegex = new StringBuilder("");
		
		MergeContentStructured mergeContentStructured = new MergeContentStructured(destinyFile);
		
		this.buildMergeContentStructuredAndRegEx(file1, file2, diffRegex, mergeContentStructured);
		
		this.printLogs(diffRegex, mergeContentStructured, null);
		
		return mergeContentStructured.mergeWithoutBaseContent();
	}

	@Override
	public Boolean merge(File file1, File file2, File baseFile, File destinyFile) throws MergeException {

		/* 
		 * base file will be transformed in one line with \n separators between lines 
		 * 
		 * diff file will be read creating groups of the same lines presents in both files
		 * 
		 * after that diff file will be matched across base file with a regular expression 
		 * to discover which groups are presents in base file
		 * 
		 * those groups intervals can be used to discover the evolution of repository. which are newer or older
		 * 
		 * Pattern p = Pattern.compile("(.*(?=BC))?(BC)?(.*(?=E))?(E)?(.*(?=FG))?(FG)?(.*(?=W))?(W)?(.*(?=X))?(X)?(.*)", Pattern.DOTALL);
		 * (.*(?=BC))?(BC)? sequence of groups, this sequence represent BC group
		 * (.*) end of any regular expression
		 * base sample "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		 */

		String baseFileContent = this.readBaseFile(baseFile);

		StringBuilder diffRegex = new StringBuilder("");
		
		MergeContentStructured mergeContentStructured = new MergeContentStructured(destinyFile);
		
		this.buildMergeContentStructuredAndRegEx(file1, file2, diffRegex, mergeContentStructured);
		
		Pattern pattern = Pattern.compile(diffRegex.toString(), Pattern.DOTALL);
		Matcher matcher = pattern.matcher(baseFileContent);
		
		this.printLogs(diffRegex, mergeContentStructured, matcher);
		
		return mergeContentStructured.merge(matcher);
	}

	private void buildMergeContentStructuredAndRegEx(File file1, File file2, StringBuilder diffRegex, MergeContentStructured mergeContentStructured) throws DiffException, MergeException {
		
		boolean readingDifferences = false;
		boolean readingEquals = false;
		
		int arrayPosition = 0;
		
		BufferedReader diffBufferedReader = null;
		try {
			File fileDiff = DiffProxy.diff(file1, file2);
			
			diffBufferedReader = new BufferedReader(new FileReader(fileDiff));

			//reading the headers
			String diffLine = null;
			diffLine = diffBufferedReader.readLine(); //header of first File
			diffLine = diffBufferedReader.readLine(); //header of second File
			diffLine = diffBufferedReader.readLine(); //line skipped
			
			diffLine = diffBufferedReader.readLine(); //first line of differences

			String diffFileSinal = null;
			while (diffLine != null) { //start reading the DiffFile
				if (diffLine.length() > 0) {
					diffFileSinal = diffLine.substring(0, 1);
				}

				//there are differences between files
				if ("-".equals(diffFileSinal) || "+".equals(diffFileSinal)) {
					if (!readingDifferences) {
						if (readingEquals) {
							String bothSides = mergeContentStructured.getBothFilesContent(arrayPosition);
							diffRegex.append("(.*(?=\\Q" + bothSides + "\\E))?(\\Q" + bothSides + "\\E)?");
							readingEquals = false;
						}
						readingDifferences = true; //start ou restart reading differences
						arrayPosition++; //alloc new position to the differences
					} 
					mergeContentStructured.addDifferences(diffLine, arrayPosition);
				} else { //text present in both files 
					if (!readingEquals) {
						if (readingDifferences) {
							readingDifferences = false;
						}
						readingEquals = true;
						if (arrayPosition == 0) {
							arrayPosition++; //normal text must be always at even positions, jumping position 1. It will be easier processing later							
						}
						arrayPosition++; //alloc new position to the equals
					}
					mergeContentStructured.addBothSides(diffLine, arrayPosition);
				}
				diffLine = diffBufferedReader.readLine();
			}

			if (readingEquals) {
				//closing the last group of the regular expression
				String bothSides = mergeContentStructured.getBothFilesContent(arrayPosition);
				diffRegex.append("(.*(?=\\Q" + bothSides + "\\E))?(\\Q" + bothSides + "\\E)?");
				readingEquals = false;

				//forcing always terminate with differences. It will be easier processing later
				arrayPosition++; //alloc new position to the differences
				mergeContentStructured.addDifferences(null, arrayPosition);
			} else if (readingDifferences) { //just for closing the differences flag
				readingDifferences = false;
			}
			diffRegex.append("(.*)"); //it's necessary always terminate with any characters
		} catch (IOException e) {
			throw new MergeException("Error accessing the file", e);
		} finally {
			try {
				diffBufferedReader.close();
			} catch (IOException e) {
				throw new MergeException("Error accessing the file", e);
			}
		}
	}

	@Override
	public OutputStream merge(InputStream base, InputStream file1, InputStream file2) throws MergeException {
		throw new MergeException("This operation with streams is not supported yet.", new OperationNotSupportedException());
	}
	
	@Override
	public OutputStream merge(InputStream file1, InputStream file2) throws MergeException {
		throw new MergeException("This operation with streams is not supported yet.", new OperationNotSupportedException());
	}

	private void printLogs(StringBuilder diffRegex, MergeContentStructured mergeContentStructured, Matcher matcher) {
		System.out.println("=================>>>>>>>>>>>>>>> Beginning of regular expression");
		System.out.println(diffRegex);
		System.out.println("<<<<<<<<<<<<<<<================= End of regular expression");

		if (matcher != null) {
			boolean result = false;
			result = matcher.matches();
	
			System.out.println("=================############### Beginning of groups");
			// Get all groups for this match
			for (int i = 0; i <= matcher.groupCount(); i++) {
				String groupStr = matcher.group(i);
				System.out.println(i + " - " + groupStr + " begin: " + matcher.start(i) + " end: " + (matcher.end(i) - 1));
			}
			System.out.println("=================############### End of groups");
			
			System.out.println("Match result: " + result);
			System.out.println("Groups quantity: " + matcher.groupCount());
		}
		//differences
		System.out.println("=================&&&&&&&&&&&&&&& Beginnig of structured differences");
		for (HashMap<String, StringBuilder> element : mergeContentStructured.getDifferences()) {
			
			Iterator iterator = element.keySet().iterator();   
			while (iterator.hasNext()) {  
			   String key = iterator.next().toString();
			   StringBuilder value = element.get(key);

			   System.out.println(key + " " + value);
			} 
			System.out.println("=================");
		}
		System.out.println("=================&&&&&&&&&&&&&&& End of structured differences");
	}
	
	private String readBaseFile(File base) {
		if (base == null) {
			return null;
		}
		
		StringBuilder result = new StringBuilder();

		BufferedReader baseBufferedReader = null;
		try {
			baseBufferedReader = new BufferedReader(new FileReader(base));
			String baseLine = baseBufferedReader.readLine();

			while (baseLine != null) {
				result.append(baseLine);
				result.append("\n");
				baseLine = baseBufferedReader.readLine();
			}
		} catch (IOException e) {
			System.out.println("Error accessing the file !!!");
		} finally {
			try {
				baseBufferedReader.close();
			} catch (IOException e) {
				return null;
			}
		}
		return result.toString();
	}
}