package br.uff.ic.gardener.util;

import java.io.File;
import java.io.FileFilter;

public class NotDirectoryFileFilter implements FileFilter {

	@Override
	public boolean accept(File pathname) {
		// TODO Auto-generated method stub
		return !pathname.isDirectory();
	}

}
