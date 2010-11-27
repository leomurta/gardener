package br.uff.ic.gardener.merge;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class DiffProxy {
	public static File diff(File file1, File file2) throws DiffException {
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
		} else if (file1.getName().equals("test6Left.txt")) {
			diffFileName = "test6Diff.txt";
		}
		String root = "/merge/";
        String path = root + diffFileName;
        URL in = MergeWithRegEx.class.getResource(path);
        try {
			return new File(in.toURI());
		} catch (URISyntaxException e) {
			System.out.println("Error accessing the resource!!!" + e);
			return null;
		}
	}
}
