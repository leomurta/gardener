package br.uff.ic.gardener.util;

import java.io.File;
import java.util.Comparator;

public class ComparatorFile implements Comparator<File> {
		@Override
		public int compare(File arg0, File arg1) {
			return arg0.compareTo(arg1);
		}
}
