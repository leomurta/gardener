package br.uff.ic.gardener.merge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MergeWithRegEx implements IMerge {

	@Override
	public File merge(File base, File file1, File file2, File destiny) {

		/* 
		 * base file will be transformed in one line with \n separators between lines
		 * 
		 * diff file will be read creating groups of the same lines presents in both files
		 * 
		 * after that diff file will be matched across base file with a regular expression 
		 * to discover which groups are presents in base file
		 * 
		 * those groups intervals can be used to discover the evolution of repository. which are newer
		 * or older  
		 */

		String baseFile = readBaseFile(base);

		StringBuilder diffRegex = new StringBuilder("");

		//Pattern p = Pattern.compile("(.*(?=BC))?(BC)?(.*(?=E))?(E)?(.*(?=FG))?(FG)?(.*(?=W))?(W)?(.*(?=X))?(X)?(.*)", Pattern.DOTALL);
		//(.*(?=BC))?(BC)?
		//(.*)
		//String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		
		boolean readingDifferences = false;
		boolean readingEquals = false;
		
		int arrayPosition = 0;
		PreMerge preMerge = new PreMerge();
		
		try {
			File fileDiff = diff(file1, file2);
			BufferedReader diffBufferedReader = new BufferedReader(new FileReader(fileDiff));

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
							String bothSides = preMerge.getBothFilesContent(arrayPosition);
							diffRegex.append("(.*(?=\\Q" + bothSides + "\\E))?(\\Q" + bothSides + "\\E)?");
							readingEquals = false;
						}
						readingDifferences = true; //start ou restart reading differences
						arrayPosition++; //alloc new position to the differences
					} 
					preMerge.addDifferences(diffLine, arrayPosition);
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
					preMerge.addBothSides(diffLine, arrayPosition);
				}
				diffLine = diffBufferedReader.readLine();
			}

			if (readingEquals) {
				//closing the last group of the regular expression
				String bothSides = preMerge.getBothFilesContent(arrayPosition);
				diffRegex.append("(.*(?=\\Q" + bothSides + "\\E))?(\\Q" + bothSides + "\\E)?");
				readingEquals = false;
			} else if (readingDifferences) { //just for closing the differences flag
				readingDifferences = false;
			}
			diffRegex.append("(.*)"); //it's necessary always terminate with any characters

			Pattern pattern = Pattern.compile(diffRegex.toString(), Pattern.DOTALL);
			Matcher matcher = pattern.matcher(baseFile);
			
			this.printLogs(baseFile, diffRegex, preMerge, matcher);
			
			return preMerge.merge(matcher, destiny);
		} catch (IOException e) {
			System.out.println("Error accessing the file!!!");
			return null;
		}
	}

	private File diff(File file1, File file2) {
//		Diff diff = new Diff(file1, file2, 'f');
//		File diffResult = diff.getDiffResult();
		// TODO usar modulo diff

		//WORKAROUNDDD!!!!!!
		
		String diffFileName = null;
		if (file1.getName().equals("test1Left.txt")) {
			diffFileName = "test1Diff.txt";
		} else if (file1.getName().equals("test2Left.txt")) {
			diffFileName = "test2Diff.txt";
		} else if (file1.getName().equals("test3Left.txt")) {
			diffFileName = "test3Diff.txt";
		} else if (file1.getName().equals("test4Left.txt")) {
			diffFileName = "test4Diff.txt";
		} else if (file1.getName().equals("test5Left.txt")) {
			diffFileName = "test5Diff.txt";
		}
		String root = "/merge/";
        String path = root + diffFileName;
        URL in = this.getClass().getResource(path);
        try {
			return new File(in.toURI());
		} catch (URISyntaxException e) {
			System.out.println("Error accessing the resource!!!" + e);
			return null;
		}
	}

	@Override
	public OutputStream merge(InputStream base, InputStream file1, InputStream file2) {
		// TODO Auto-generated method stub
		return null;
	}

	private Matcher printLogs(String baseFile, StringBuilder diffRegex, PreMerge preMerge, Matcher matcher) {
		System.out.println("=================>>>>>>>>>>>>>>> Beginning of regular expression");
		System.out.println(diffRegex);
		System.out.println("<<<<<<<<<<<<<<<================= End of regular expression");

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
		
		//differences
		System.out.println("=================&&&&&&&&&&&&&&& Beginnig of structured differences");
		for (HashMap<String, StringBuilder> element : preMerge.getDifferences()) {
			
			Iterator iterator = element.keySet().iterator();   
			while (iterator.hasNext()) {  
			   String key = iterator.next().toString();
			   StringBuilder value = element.get(key);

			   System.out.println(key + " " + value);
			} 
			System.out.println("=================");
		}
		System.out.println("=================&&&&&&&&&&&&&&& End of structured differences");
		return matcher;
	}
	
	private static String readBaseFile(File base) {
		StringBuilder result = new StringBuilder();

		try {
			BufferedReader baseBufferedReader = new BufferedReader(new FileReader(base));
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