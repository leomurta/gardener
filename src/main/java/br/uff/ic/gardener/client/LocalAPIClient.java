package br.uff.ic.gardener.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.TransationException;
import br.uff.ic.gardener.client.CreationAPIClientException;
import br.uff.ic.gardener.util.ANDFileFilter;
import br.uff.ic.gardener.util.DirectoryFileFilter;
import br.uff.ic.gardener.util.NameFileFilter;
import br.uff.ic.gardener.util.UtilStream;


/**
 * It does a simple repository based in zip files per revision.
 * @author Marcos
 *
 */
class LocalAPIClient implements APIClient {

	private static final String CONFIG_PROPERTIES = "config.properties";

	/**
	 * path of repository data. The repository config is a child of this directory.
	 */
	File path;
	
	/**
	 * path of config repository.
	 */
	File pathConfig;
	
	/**
	 * more information in: http://www.exampledepot.com/egs/java.util/Props.html 
	 */
	Properties properties = null;
	
	//TODO Ver formato STATIC
	private static String strConfigPath = ".gdrservsimple";
	/**
	 * Create LocalAPIClient
	 * It looks up by config directory in _path specified.
	 * @param _path Path of repository
	 * @throws CreationAPIClientException
	 */
	public LocalAPIClient(File _path) throws CreationAPIClientException
	{	
		if(!_path.isDirectory())
			throw new CreationAPIClientException(_path.toString() + "is not a directory", null);
		
		path = _path;
		
		File[] childs = _path.listFiles(new ANDFileFilter(new DirectoryFileFilter(), new NameFileFilter(strConfigPath)));
		
		if(childs.length < 1)
		{
			try
			{
				pathConfig = new File(path.getPath()+"/"+strConfigPath);
				if(!pathConfig.mkdir())
					throw new CreationAPIClientException("Do not possible create config file: "+ pathConfig.getPath(), null);
					
			}catch (SecurityException e) {
				throw new CreationAPIClientException("Do not possible create config file because security questions: "+ pathConfig.getPath(), e);
			}
		}else
		{
			pathConfig = childs[0];
		}
			
		try {
			loadProperties();
		} catch (FileNotFoundException e) {
			throw new CreationAPIClientException("Property file not found: ", e);
		} catch (IOException e) {
			throw new CreationAPIClientException("Property file not found: ", e);
		}
	}
	
	@Override
	public void checkout(Map<String, InputStream> items, RevisionID revision)
			throws TransationException {
		
		FileInputStream input;
		try {
			input = new FileInputStream(getPathOfRevision(revision));
		} catch (FileNotFoundException e) {
			throw new TransationException("Não foi possível achar o arquivo de revisão.", e);
		}
		ZipInputStream zos = new ZipInputStream(input);	
		
		
		try {
			items.clear();
			ZipEntry entry = null;
			
			while ((entry = zos.getNextEntry()) != null)
			{
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				UtilStream.copy(zos, os);
				
				InputStream is = new ByteArrayInputStream(os.toByteArray());
				items.put(entry.getName(), is);
			}
			zos.close();
		
		} catch (IOException e) {
			throw new TransationException("Error at decompact revision.", e);
		}
		
	}
	
	public String getPathOfRevision(RevisionID r)
	{
		return pathConfig.getPath()
		+	File.separatorChar
		+	r.toString()
		+ 	".zip";
	}


	@Override
	public RevisionID commit(Map<String, InputStream> items)
			throws TransationException {
		//create a ZipOutputStream to zip the data to 
		
	    ZipOutputStream zos;
	    RevisionID newRevision = RevisionID.generateNewRevision(this.getLastRevision().getNumber());
		try {
			zos = new ZipOutputStream(new FileOutputStream(
						pathConfig.getPath()
					+	File.separatorChar
					+	newRevision.toString()
					+ 	".zip")
			);
		
	    
		    //assuming that there is a directory named inFolder (If there 
		    //isn't create one) in the same directory as the one the code runs from, 
		    //call the zipDir method 
		    
			for(Map.Entry<String, InputStream> entry: items.entrySet())
			{
				zipInputStream(entry.getKey(), entry.getValue(), zos);
			}
			
			//close the stream 
		    zos.close(); 
	    
		} catch (FileNotFoundException e) {
			throw new TransationException("Zip file not found", e);
		} catch (IOException e) {
			throw new TransationException("Zip process has problems", e);
		}
		
		//atualiza o properties
		properties.setProperty("LastRevision", newRevision.toString());
		try {
			saveProperties();
		} catch (InternalAPIClientException e) {
			throw new TransationException(e.toString(), e);
		}
		return newRevision;
	}

	
	private void saveProperties() throws InternalAPIClientException {
		File[] files = pathConfig.listFiles(new NameFileFilter(CONFIG_PROPERTIES));
		
		if(files.length > 0)
		{
			FileOutputStream out;
			try {
				out = new FileOutputStream(files[0]);
			} catch (FileNotFoundException e) {
			
				throw new InternalAPIClientException("Não foi possível encontrar o arquivo de properties "+ CONFIG_PROPERTIES, e);
			}
			try {
				properties.store(out,"");
			} catch (IOException e) {
				throw new InternalAPIClientException("Erro ao salvar o arquivo de properties: "+ CONFIG_PROPERTIES, e);
			}
		}
	}

	@Override
	public RevisionID getLastRevision(){
		return RevisionID.fromString(properties.getProperty("LastRevision"));
	}
	
	
	private void loadProperties() throws FileNotFoundException, IOException
	{
		File[] files = pathConfig.listFiles(new NameFileFilter(CONFIG_PROPERTIES));
		
		
		if(properties == null)
		{
			//init properties
			properties = new Properties();
			properties.setProperty("LastRevision", RevisionID.ZERO_REVISION.toString());
		}
		
		File config = null;
		if(files.length == 0)
		{
			config = new File(pathConfig.getPath() + File.pathSeparatorChar + CONFIG_PROPERTIES);
		
			//store			
			properties.store(new FileOutputStream(config), "");
			
			//try load again
			files = pathConfig.listFiles(new NameFileFilter(CONFIG_PROPERTIES));
		}
		
		if(files.length==0)
			throw new FileNotFoundException("O arquivo " + CONFIG_PROPERTIES + " não foi encontrado");
		
		config = files[0];
		
		properties.load(new FileInputStream(config));
	}

	/**
	 * Compress a input stream "item" with key "id"
	 * @param id The id of data
	 * @param item The data
	 * @param zos the zip output
	 * @throws IOException zipping has a problem
	 */
	private void zipInputStream(String id, InputStream item, ZipOutputStream zos) throws IOException
	{
		//create a new zip entry 
        ZipEntry anEntry = new ZipEntry(id); 
        
  
        //place the zip entry in the ZipOutputStream object 
        zos.putNextEntry(anEntry); 
        //now write the content of the file to the ZipOutputStream 
        UtilStream.copy(item, zos);
        
       //close the Stream 
       item.close();  		
	}
	
	/**
	 * Method to compress a directory in a ZipOutputStream.
	 * The Source code in: http://www.devx.com/tips/Tip/14049
	 * @param dir2zip the directory path
	 * @param zos the ZipOutputStream
	 */
	@SuppressWarnings("unused")
	private void zipDir(String dir2zip, ZipOutputStream zos) 
	{ 
	    try 
	   { 
	        //create a new File object based on the directory we have to zip
	    	File zipDir = new File(dir2zip); 
	        //get a listing of the directory content 
	        String[] dirList = zipDir.list(); 
	
	        //loop through dirList, and zip the files 
	        for(int i=0; i<dirList.length; i++) 
	        { 
	            File f = new File(zipDir, dirList[i]); 
	        if(f.isDirectory()) 
	        { 
	                //if the File object is a directory, call this 
	                //function again to add its content recursively 
	            String filePath = f.getPath(); 
	            zipDir(filePath, zos); 
	                //loop again 
	            continue; 
	        } 
            //if we reached here, the File object f was not a directory 
            //create a FileInputStream on top of f            
	        FileInputStream fis = new FileInputStream(f);	          
	        //create a new zip entry 
	        ZipEntry anEntry = new ZipEntry(f.getPath()); 
            
	        //place the zip entry in the ZipOutputStream object 
	        zos.putNextEntry(anEntry); 

	        UtilStream.copy(fis, zos);
           //close the Stream 
           fis.close(); 
	        } 
	   } 
		catch(Exception e) 
		{ 
		    //handle exception 
		} 

	}
}
