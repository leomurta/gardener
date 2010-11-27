package br.uff.ic.gardener.merge;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

public class MergeContentStructured {
	
	private ArrayList<HashMap<String, StringBuilder>> structuredDiff;
	private BufferedWriter bufferedWriterMerge;
	
	private Boolean hasConflict;
	
	public MergeContentStructured(File destiny) throws MergeException {
		super();
		try {
			this.bufferedWriterMerge = new BufferedWriter(new FileWriter(destiny));
		} catch (IOException e) {
			throw new FileNotFoundException("Destiny not found.");
		}
		this.structuredDiff = new ArrayList<HashMap<String, StringBuilder>>();;
		this.structuredDiff.add(new HashMap<String, StringBuilder>()); //skipping position 0
	}
	
	//REMOVE LATER
	public ArrayList<HashMap<String, StringBuilder>> getDifferences() {
		return this.structuredDiff;
	}

	public void addBothSides(String diffLine, int position) {
		HashMap<String, StringBuilder> structuredDiffElement = null;
		boolean newPosition = false;
		
		if (position < this.structuredDiff.size()) {
			structuredDiffElement = structuredDiff.get(position);
		} else { //create a new position
			if ((position == 2) && (this.structuredDiff.size() == 1)) {
				structuredDiffElement = new HashMap<String, StringBuilder>();
				structuredDiffElement.put("leftFileContent", null);
				structuredDiffElement.put("rightFileContent", null);
				structuredDiffElement.put("bothFilesContent", null);
				this.structuredDiff.add(structuredDiffElement); //position 1 didn't exist yet. skipping position 1
			}
			structuredDiffElement = new HashMap<String, StringBuilder>();
			structuredDiffElement.put("leftFileContent", null);
			structuredDiffElement.put("rightFileContent", null);
			structuredDiffElement.put("bothFilesContent", new StringBuilder());
			newPosition = true;
		}

		structuredDiffElement.put("bothFilesContent", structuredDiffElement.get("bothFilesContent").append(diffLine + "\n"));
		
		if (newPosition) {
			structuredDiff.add(structuredDiffElement);
		} else {
			structuredDiff.set(position, structuredDiffElement);
		}
	}
	
	public void addDifferences(String diffLine, int position) {
		HashMap<String, StringBuilder> structuredDiffElement = null;
		boolean newPosition = false;
		
		if (position < this.structuredDiff.size()) {
			structuredDiffElement = structuredDiff.get(position);
		} else { //create a new position
			if (diffLine == null) { //flag indicating this is the last element
				structuredDiffElement = new HashMap<String, StringBuilder>();
				structuredDiffElement.put("leftFileContent", null);
				structuredDiffElement.put("rightFileContent", null);
				structuredDiffElement.put("bothFilesContent", null);
				this.structuredDiff.add(structuredDiffElement); //forcing always terminate with differences. It will be easier processing later
				return;
			}
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
	
		if (newPosition) {
			structuredDiff.add(structuredDiffElement);
		} else {
			structuredDiff.set(position, structuredDiffElement);
		}
	}

	public Boolean merge(Matcher matcher) throws MergeException {
		/* 
		 * our regular expression always return equalities at even positions and differences at odd positions
		 */

		try {
			int firstArrayPosition = 1;
			//if diff file started with differences so...
			if (this.getLeftFileContent(firstArrayPosition) != null && this.getRightFileContent(firstArrayPosition) != null) {
				this.writeResult(this.doMerge(matcher, firstArrayPosition));
			}

			boolean allMatcherProcessed = false;
			FirstAndSecondGroupsFounded groupsFounded = null;
			int firstGroupPosition = 2;
			
			while (!allMatcherProcessed) {
				groupsFounded = this.findFirstTwoGroupsMatched(matcher, firstGroupPosition);
				
				if (groupsFounded.isConsecutive()) {
					//print the group
					this.writeResult(this.getBothFilesContent(firstGroupPosition));
					
					//print the result of merging the differences between this group and the second
					this.writeResult(this.doMerge(matcher, firstGroupPosition + 1));
				} else if (groupsFounded.hasBothGroups()) { //base file coudn't help
					//print the result of diff2 until the next group
					for (int arrayPosition = groupsFounded.getFirstGroup(); arrayPosition < groupsFounded.getSecondGroup(); arrayPosition++) {
						this.writeResult(this.doSimpleMerge(arrayPosition));
					}
				} else if (groupsFounded.hasOnlyFirstGroup()) { //base file coudn't help
					//print the result of diff2 until the end
					int firstPosition = groupsFounded.getFirstGroup();
					int lastPosition = this.structuredDiff.size() - 1;
					this.doSimpleMergeWithDiff2Result(firstPosition, lastPosition);
				} else if (groupsFounded.hasNoMoreGroups()) { //all groups and were processed
					//pode só ter diferencas
					allMatcherProcessed = true;
				}
				firstGroupPosition = groupsFounded.getSecondGroup();
			}

			int lastArrayPosition = this.structuredDiff.size() - 1;
			//if diff file ended with differences so...
			if (this.getLeftFileContent(lastArrayPosition) != null && this.getRightFileContent(lastArrayPosition) != null) {
				this.writeResult(this.doMerge(matcher, lastArrayPosition));				
			}
			
			this.bufferedWriterMerge.flush();
			return this.hasConflict;
		} catch (IOException e) {
			throw new MergeException("Error writing the destiny file", e);
		} finally {
			try {
				this.bufferedWriterMerge.close();
			} catch (IOException e) {
				throw new MergeException("Error writing the destiny file", e);
			}
		}
	}

	private void doSimpleMergeWithDiff2Result(int firstPosition, int lastPosition) throws IOException {
		for (int arrayPosition = firstPosition; arrayPosition < lastPosition; arrayPosition++) {
			this.writeResult(this.doSimpleMerge(arrayPosition));
		}
	}

	private String doMerge(Matcher matcher, int arrayPosition) {
		StringBuilder ret = new StringBuilder();

		String baseContent = matcher.group(arrayPosition);

		if (baseContent.equals(this.getLeftFileContent(arrayPosition))) {
			ret.append(this.getRightFileContent(arrayPosition));
		} else if (baseContent.equals(this.getRightFileContent(arrayPosition))) {
			ret.append(this.getLeftFileContent(arrayPosition));
		} else {
			//conflict
			ret.append(this.printConflict(arrayPosition));
		}
		return ret.toString();
	}
	
	private String doSimpleMerge(int arrayPosition) {
		StringBuilder ret = new StringBuilder();

		if (arrayPosition % 2 == 0) { //even positions contains content present in both files 
			ret.append(this.getBothFilesContent(arrayPosition));
		} else { //odd positions contains the differences
			ret.append(this.printConflict(arrayPosition));
		}
		return ret.toString();
	}
	
	private String printConflict(int arrayPosition) {
		StringBuilder ret = new StringBuilder();
		
		ret.append("<<<<<<<<<<<<<\n");
		ret.append(this.getLeftFileContent(arrayPosition));
		ret.append("=============\n");
		ret.append(this.getRightFileContent(arrayPosition));
		ret.append(">>>>>>>>>>>>>\n");
		
		if (this.hasConflict == null) {
			this.hasConflict = new Boolean(true);
		}
		
		return ret.toString();
	}
	
	public Boolean mergeWithoutBaseContent() throws MergeException {
		//print the result of diff2 until the end
		
		try {
			int firstPosition = 1;
			int lastPosition = this.structuredDiff.size() - 1;
			
			if (this.getLeftFileContent(firstPosition) != null && this.getRightFileContent(firstPosition) != null) {
				this.writeResult(this.doSimpleMerge(firstPosition));
			}
			firstPosition = 2;

			this.doSimpleMergeWithDiff2Result(firstPosition, lastPosition);
			
			if (this.getLeftFileContent(lastPosition) != null && this.getRightFileContent(lastPosition) != null) {
				this.writeResult(this.doSimpleMerge(lastPosition));
			}
			
			this.bufferedWriterMerge.flush();
			return this.hasConflict;
		} catch (IOException e) {
			throw new MergeException("Error writing the destiny file", e);
		} finally {
			try {
				this.bufferedWriterMerge.close();
			} catch (IOException e) {
				throw new MergeException("Error writing the destiny file", e);
			}
		}
	}
	
	public String getLeftFileContent(int position) {
		StringBuilder leftContent = this.structuredDiff.get(position).get("leftFileContent");
		return leftContent != null ? leftContent.toString() : null;  
	}
	
	public String getRightFileContent(int position) {
		StringBuilder rightContent = this.structuredDiff.get(position).get("rightFileContent");
		return rightContent != null ? rightContent.toString() : null;
	}
	
	public String getBothFilesContent(int position) {
		StringBuilder bothContent = this.structuredDiff.get(position).get("bothFilesContent");
		return bothContent != null ? bothContent.toString() : null;
	}
	
	private FirstAndSecondGroupsFounded findFirstTwoGroupsMatched(Matcher matcher, int firstCandidate) {
		int first = this.findFirstGroupMatched(matcher, firstCandidate);
		int second = this.findFirstGroupMatched(matcher, first > 0 ? first + 2 : -1);
		return new FirstAndSecondGroupsFounded(first, second);
	}
	
	private int findFirstGroupMatched(Matcher matcher, int firstCandidate) {
		if (firstCandidate < 0 || firstCandidate > matcher.groupCount()) {
			return -1; //group doesn't exist
		}
		//looking for the group
		while (matcher.group(firstCandidate) == null && firstCandidate + 2 <= matcher.groupCount()) {
			firstCandidate += 2;
		}
		//groupCount has finished and I didn't find a group
		if (matcher.group(firstCandidate) == null) {
			return -1;
		}
		return firstCandidate;
	}
	
	private void writeResult(String result) throws IOException {
		this.bufferedWriterMerge.write(result);
		System.out.print(result);
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

	public boolean hasBothGroups() {
		return (firstGroup > 0) && (secondGroup > 0);
	}

	public boolean hasOnlyFirstGroup() {
		return (firstGroup > 0) && (secondGroup == -1);
	}
	
	public boolean hasNoMoreGroups() {
		return (firstGroup == -1) && (secondGroup == -1);
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