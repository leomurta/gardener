package br.uff.ic.gardener.merge;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

public class PreMerge {
	
	private ArrayList<HashMap<String, StringBuilder>> structuredDiff; 
	
	public PreMerge() {
		super();
		this.structuredDiff = new ArrayList<HashMap<String, StringBuilder>>();;
		this.structuredDiff.add(new HashMap<String, StringBuilder>()); //skipping position 0
	}
	
	//REMOVE LATER
	public ArrayList<HashMap<String, StringBuilder>> getDifferences() {
		return this.structuredDiff;
	}

	public void addBothSides(String diffLine, int position) {
		HashMap<String, StringBuilder> structuredDiffElement = null;
		
		boolean newElement = false;
		try {
			structuredDiffElement = structuredDiff.get(position);
		} catch (IndexOutOfBoundsException e) {
			structuredDiffElement = new HashMap<String, StringBuilder>();
			structuredDiffElement.put("leftFileContent", null);
			structuredDiffElement.put("rightFileContent", null);
			structuredDiffElement.put("bothFilesContent", new StringBuilder());
			newElement = true;
		}

		structuredDiffElement.put("bothFilesContent", structuredDiffElement.get("bothFilesContent").append(diffLine + "\n"));
		
		if (newElement) {
			structuredDiff.add(structuredDiffElement);
		} else {
			structuredDiff.set(position, structuredDiffElement);
		}
	}
	
	public void addDifferences(String diffLine, int position) {
		HashMap<String, StringBuilder> structuredDiffElement = null;
		
		boolean newPosition = false;
		try {
			structuredDiffElement = structuredDiff.get(position);
		} catch (IndexOutOfBoundsException e) {
			structuredDiffElement = new HashMap<String, StringBuilder>();
			structuredDiffElement.put("leftFileContent", new StringBuilder());
			structuredDiffElement.put("rightFileContent", new StringBuilder());
			structuredDiffElement.put("bothFilesContent", new StringBuilder());
			newPosition = true;
		}

		String diffFileSinal = diffLine.substring(0, 1);
		String trueLine = diffLine.subSequence(1, diffLine.length()) + "\n";
		
		if ("-".equals(diffFileSinal)) {
			structuredDiffElement.put("leftFileContent", structuredDiffElement.get("leftFileContent").append(trueLine));
		} else if ("+".equals(diffFileSinal)) {
			structuredDiffElement.put("rightFileContent", structuredDiffElement.get("rightFileContent").append(trueLine));
		}
		//this next line probably could be removed
		structuredDiffElement.put("bothFilesContent", structuredDiffElement.get("bothFilesContent").append(trueLine));	
	
		if (newPosition) {
			structuredDiff.add(structuredDiffElement);
		} else {
			structuredDiff.set(position, structuredDiffElement);
		}
	}
	
	private FirstAndSecondGroupsFounded findFirstTwoGroupsMatched(Matcher matcher, int firstGroupPosition) {
		//possible "feature": transform in recursive algorithm
		//or just transform to looking for just one group. It will be necessary call 2 times.
		
		int secondGroupPosition = firstGroupPosition + 2;
		
		if (firstGroupPosition < 0 || firstGroupPosition > matcher.groupCount()) {
			return new FirstAndSecondGroupsFounded(-1, -1); //start not found
		}

		//looking for the first
		while (matcher.group(firstGroupPosition) == null && firstGroupPosition + 2 <= matcher.groupCount()) {
			firstGroupPosition += 2;
		}

		if (matcher.group(firstGroupPosition) == null) {
			return new FirstAndSecondGroupsFounded(-1, -1); //start not found
		}

		secondGroupPosition = firstGroupPosition + 2;
		
		if (secondGroupPosition > matcher.groupCount()) {
			return new FirstAndSecondGroupsFounded(firstGroupPosition, -1); //end not found
		}
		
		//looking for the second
		while (matcher.group(secondGroupPosition) == null && secondGroupPosition + 2 <= matcher.groupCount()) {
			secondGroupPosition += 2;
		}
		
		if (matcher.group(secondGroupPosition) == null) {
			return new FirstAndSecondGroupsFounded(firstGroupPosition, -1); //end not found
		}

		return new FirstAndSecondGroupsFounded(firstGroupPosition, secondGroupPosition);
	}

	public File merge(Matcher matcher, File destiny) {
		BufferedWriter bufferedWriterMerge = null;

		try {			
			bufferedWriterMerge = new BufferedWriter(new FileWriter(destiny));

			int firstGroupPointer = 0;
			boolean allMatcherProcessed = false;
			FirstAndSecondGroupsFounded firstAndSecondGroupsFounded = null;
			
			while (!allMatcherProcessed) {
				firstAndSecondGroupsFounded = this.findFirstTwoGroupsMatched(matcher, firstGroupPointer);
				
				if (firstAndSecondGroupsFounded.isConsecutive()) {
					int indexMiddleDifferences = firstAndSecondGroupsFounded.getFirstGroup() + 1;
					String middleDifferences = matcher.group(indexMiddleDifferences);

					if (middleDifferences.equals(this.getLeftFileContent(indexMiddleDifferences))) {
						bufferedWriterMerge.write(this.getRightFileContent(indexMiddleDifferences));
						System.out.print(this.getRightFileContent(indexMiddleDifferences));
					} else if (middleDifferences.equals(this.getRightFileContent(indexMiddleDifferences))) {
						bufferedWriterMerge.write(this.getLeftFileContent(indexMiddleDifferences));
						System.out.print(this.getLeftFileContent(indexMiddleDifferences));
					} else {
						//conflict
						this.printConflict(bufferedWriterMerge, indexMiddleDifferences);
					}
					bufferedWriterMerge.write(this.getBothFilesContent(firstAndSecondGroupsFounded.getSecondGroup()));
					System.out.print(this.getBothFilesContent(firstAndSecondGroupsFounded.getSecondGroup()));
				} else { //base file coudn't help, so print the result of diff
					if (firstAndSecondGroupsFounded.getFirstGroup() == -1) { //there aren't more groups, so all groups were processed
						allMatcherProcessed = true;
					} else { //there are one or more groups to process yet
						int endOfFor = firstAndSecondGroupsFounded.getSecondGroup(); //print until the next group
						if (firstAndSecondGroupsFounded.getSecondGroup() == -1) { //if there isn't another group
							endOfFor = matcher.groupCount() - 1; //I will just print until the end of matcher and finish the process
							allMatcherProcessed = true; 
						}

						for (int arrayPosition = firstAndSecondGroupsFounded.getFirstGroup() + 1; arrayPosition <= endOfFor; arrayPosition++) {
							if (arrayPosition % 2 != 0) { //even positions contains content present in both files and odd positions the differences
								printConflict(bufferedWriterMerge, arrayPosition);
							} else {
								bufferedWriterMerge.write(this.getBothFilesContent(arrayPosition));
								System.out.print(this.getBothFilesContent(arrayPosition));
							}
						}
					}
				}
				firstGroupPointer = firstAndSecondGroupsFounded.getSecondGroup();
			}

			bufferedWriterMerge.close();
			return destiny;
		} catch (IOException e) {
			System.out.println("Error accessing the file!!!");
			return null;
		}
	}

	private void printConflict(BufferedWriter bufferedWriterMerge, int indexMiddleDifferences) throws IOException {
		bufferedWriterMerge.write("<<<<<<<<<<<<<\n");
		System.out.println("<<<<<<<<<<<<<");
		
		bufferedWriterMerge.write(this.getLeftFileContent(indexMiddleDifferences));
		System.out.print(this.getLeftFileContent(indexMiddleDifferences));

		bufferedWriterMerge.write("=============\n");
		System.out.println("=============");

		bufferedWriterMerge.write(this.getRightFileContent(indexMiddleDifferences));
		System.out.print(this.getRightFileContent(indexMiddleDifferences));
		
		bufferedWriterMerge.write(">>>>>>>>>>>>>\n");
		System.out.println(">>>>>>>>>>>>>");
	}
	
	public String getLeftFileContent(int position) {
		return this.structuredDiff.get(position).get("leftFileContent").toString();
	}
	
	public String getRightFileContent(int position) {
		return this.structuredDiff.get(position).get("rightFileContent").toString();
	}
	
	public String getBothFilesContent(int position) {
		return this.structuredDiff.get(position).get("bothFilesContent").toString();
	}
}

class FirstAndSecondGroupsFounded {
	private int firstGroup;
	private int secondGroup;
	
	public FirstAndSecondGroupsFounded(int firstGroup, int secondGroup) {
		this.firstGroup = firstGroup;
		this.secondGroup = secondGroup;
	}
	
	public boolean isConsecutive() {
		return secondGroup == (firstGroup + 2);
	}
	public int getFirstGroup() {
		return firstGroup;
	}
	public void setFirstGroup(int firstGroup) {
		this.firstGroup = firstGroup;
	}
	public int getSecondGroup() {
		return secondGroup;
	}
	public void setSecondGroup(int secondGroup) {
		this.secondGroup = secondGroup;
	}
}