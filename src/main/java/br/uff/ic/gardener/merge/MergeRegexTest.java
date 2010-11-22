package br.uff.ic.gardener.merge;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MergeRegexTest {

	public static void main(String[] args) {

		Pattern p = Pattern.compile("(.*(?=BC))?(BC)?(.*(?=E))?(E)?(.*(?=FG))?(FG)?(.*(?=W))?(W)?(.*(?=X))?(X)?(.*)", Pattern.DOTALL);
		String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Matcher m = p.matcher(base);

		boolean result = false;
		result = m.matches();

		// Get all groups for this match
		for (int i = 0; i <= m.groupCount(); i++) {
			String groupStr = m.group(i);
			System.out.println(i + " - " + groupStr + " begin: " + m.start(i) + " end: " + (m.end(i) - 1));
		}

		System.out.println(result);
	}
}