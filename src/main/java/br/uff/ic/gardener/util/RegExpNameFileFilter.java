package br.uff.ic.gardener.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

/**
 * return true to files that couple with a regular expression.
 * 
 * @author Marcos
 * 
 */
public class RegExpNameFileFilter implements FileFilter {

	//String strRegExp = "";
	Pattern pattern = null;
	
	boolean bAcceptAllPath = false;

	public RegExpNameFileFilter(String _regExp) {
		pattern = Pattern.compile(_regExp);
	}
	
	public RegExpNameFileFilter(String _regExp, boolean ignorePath) {
		pattern = Pattern.compile(_regExp);
		bAcceptAllPath = ignorePath;
		//pattern = _regExp.
	}



	@Override
	public boolean accept(File file) {
		if(bAcceptAllPath && file.isDirectory())
			return true;
			
		String name = file.getName();
		return pattern.matcher(name).matches();
	}

}
