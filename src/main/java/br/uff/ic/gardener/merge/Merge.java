package br.uff.ic.gardener.merge;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


public class Merge implements IMerge{
	
	public File merge(File base, File file1, File file2, File file3) {
		File fileMerge = new File("..\\fileMerge.txt");
		try {
			OutputStream outputStream = merge(new FileInputStream(base), new FileInputStream(file1), new FileInputStream(file2));
			
			FileOutputStream fileOutputStream = new FileOutputStream(fileMerge);
			fileOutputStream.write((outputStream.toString()).getBytes());
			fileOutputStream.close();
			
		} 
		catch (IOException e) { 
			System.out.println("Erro no acesso ao arquivo !!!");
		}
		
		return fileMerge;
	}

	public OutputStream merge(InputStream base, InputStream file1, InputStream file2) {
		OutputStream outputStream = new ByteArrayOutputStream();
		try {
			File fileDiff = diff(base, file1);
			BufferedReader bufferedReaderDiff = new BufferedReader(new FileReader(fileDiff));  
			InputStreamReader inputStreamReaderFile2 = new InputStreamReader(file2);
			BufferedReader bufferedReaderFile2 = new BufferedReader(inputStreamReaderFile2);
			
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
			BufferedWriter bufferedWriterMerge = new BufferedWriter(outputStreamWriter);
			
			String linhaDiff, linhaFile2 = null;
			int startFromFile, countFromFile, startToFile, countToFile;
			int indSinalMenos, indPrimVirgula, indSinalMais, indSegVirgula, indSegArroba;
			int pontFile2 = 1;
			
			// ler cabecalho
			bufferedReaderDiff.readLine();
			bufferedReaderDiff.readLine();
			
			// leitura do resultado do diff
			while((linhaDiff = bufferedReaderDiff.readLine()) != null) {  
			    System.out.println("Linha: " + linhaDiff); 
			    if (linhaDiff.startsWith("@@")) {
			    	indSinalMenos = linhaDiff.indexOf('-');
			    	indPrimVirgula = linhaDiff.indexOf(',');
			    	indSinalMais = linhaDiff.indexOf('+');
			    	indSegVirgula = linhaDiff.indexOf(',', indPrimVirgula + 1);
			    	indSegArroba = linhaDiff.indexOf("@@", 3);
			    	
			    	startFromFile = Integer.parseInt(linhaDiff.substring(indSinalMenos+1, indPrimVirgula));
			    	countFromFile = Integer.parseInt(linhaDiff.substring(indPrimVirgula+1, indSinalMais - 1));
			    	startToFile = Integer.parseInt(linhaDiff.substring(indSinalMais+1, indSegVirgula));
			    	countToFile = Integer.parseInt(linhaDiff.substring(indSegVirgula+1, indSegArroba - 1));
			    }
			    
			    if (linhaDiff.startsWith("-")) {
			    	linhaDiff = linhaDiff.substring(1, linhaDiff.length());
			    	int indLineFile2 = findLineInFile(file2, linhaDiff);
			    	
			    	if (indLineFile2 > 0) {
				    	// leitura do File2 para montar resultado do merge
				    	while(((linhaFile2 = bufferedReaderFile2.readLine()) != null) && (pontFile2 < indLineFile2)) {
				    		bufferedWriterMerge.write(linhaFile2);
				    		bufferedWriterMerge.newLine();
				    		pontFile2++;
				    	}	
				    	pontFile2++;
			    	}
			    }
			    
			    if (linhaDiff.startsWith(" ")) {
			    	linhaDiff = linhaDiff.substring(1, linhaDiff.length());
			    	int indLineFile2 = findLineInFile(file2, linhaDiff);
			    	if (indLineFile2 > 0) {
			    		// leitura do File2 para montar resultado do merge
				    	while(((linhaFile2 = bufferedReaderFile2.readLine()) != null) && (pontFile2 < indLineFile2)) {
				    		bufferedWriterMerge.write(linhaFile2);
				    		bufferedWriterMerge.newLine();
				    		pontFile2++;
				    	}
				    	if (pontFile2 == indLineFile2) {
				    		bufferedWriterMerge.write(linhaFile2);
				    		bufferedWriterMerge.newLine();
				    		pontFile2++;
				    	}
			    	}
			    }

			    if (linhaDiff.startsWith("+")) {
			    	linhaDiff = linhaDiff.substring(1, linhaDiff.length());
		    		bufferedWriterMerge.write(linhaDiff);
		    		bufferedWriterMerge.newLine();
		    	}

			}
			
			// termina de escrever o que sobrou de file2
	    	while(((linhaFile2 = bufferedReaderFile2.readLine()) != null)) {
	    		bufferedWriterMerge.write(linhaFile2);
	    		bufferedWriterMerge.newLine();
	    	}
	    	//bufferedWriterMerge.flush();
	    	
			bufferedReaderDiff.close();
			bufferedReaderFile2.close();
			bufferedWriterMerge.close();
			
		} 
		catch (IOException e) { 
			System.out.println("Erro no acesso ao arquivo !!!");
		}
		
		return outputStream;
		
	}
	
	
	private File diff(InputStream base, InputStream file1) {
		// TODO usar modulo diff
		return new File("..\\unifiedFormat.txt");
	}
	
	private int findLineInFile(InputStream file, String linha) throws IOException{
		int ind = 0; 
		int cont = 0;
		String linhaFile;
		
		InputStreamReader inputStreamReaderFile = new InputStreamReader(file);
		BufferedReader bufferedReaderFile = new BufferedReader(inputStreamReaderFile);
		while(((linhaFile = bufferedReaderFile.readLine()) != null)) {
			cont++;
			if (linha.equals(linhaFile)) {
				ind = cont;
			}
		}
		
		return ind;
	}
}
