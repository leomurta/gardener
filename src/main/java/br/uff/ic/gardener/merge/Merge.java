import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Merge implements IMerge{
	public File merge(File base, File file1, File file2) {
		File fileMerge = null;
		
		try {
			File fileDiff = diff(base, file1);
			BufferedReader bufferedReaderDiff = new BufferedReader(new FileReader(fileDiff));  
			BufferedReader bufferedReaderFile2 = new BufferedReader(new FileReader(file2));
			
			//fileMerge = new File("c:\\teste\\merge\\fileMerge.txt");
			fileMerge = new File("..\\fileMerge.txt");
			BufferedWriter bufferedWriterMerge = new BufferedWriter(new FileWriter(fileMerge));
			
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
			bufferedReaderDiff.close();
			bufferedReaderFile2.close();
			bufferedWriterMerge.close();
			
		} 
		catch (IOException e) { 
			System.out.println("Erro no acesso ao arquivo !!!");
		} 
		
		
		return fileMerge;
	}
	
	private File diff(File base, File file1) {
		// TODO usar modulo diff
		return new File("c:\\teste\\merge\\unifiedFormat.txt");
	}
	
	private int findLineInFile(File file, String linha) throws IOException{
		int ind = 0; 
		int cont = 0;
		String linhaFile;
		
		BufferedReader bufferedReaderFile = new BufferedReader(new FileReader(file));
		while(((linhaFile = bufferedReaderFile.readLine()) != null)) {
			cont++;
			if (linha.equals(linhaFile)) {
				ind = cont;
			}
		}
		
		return ind;
	}
}
