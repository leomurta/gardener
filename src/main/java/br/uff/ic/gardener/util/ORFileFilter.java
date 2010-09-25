package br.uff.ic.gardener.util;

import java.io.File;
import java.io.FileFilter;

/**
 * This Class permits AND operations with others filters in the search process.
 * 
 * @author Marcos
 * 
 */
public class ORFileFilter implements FileFilter {

	private FileFilter[] fileVec;

	public ORFileFilter(FileFilter... vec) {
		fileVec = vec;
	}

	@Override
	public boolean accept(File dir) {
		for (FileFilter f : fileVec) {
			if (f.accept(dir))
				return true;
		}
		return false;
	}
}