package br.uff.ic.gardener.comm.localfake;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import java.util.Collection;

import br.uff.ic.gardener.CIType;
import br.uff.ic.gardener.RevisionCommited;
import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.comm.ComClient;
import br.uff.ic.gardener.comm.ComClientException;
import br.uff.ic.gardener.util.ANDFileFilter;
import br.uff.ic.gardener.util.DirectoryFileFilter;
import br.uff.ic.gardener.util.NameFileFilter;
import br.uff.ic.gardener.util.UtilStream;
import br.uff.ic.gardener.ConfigurationItem;

public class LocalFakeComClient implements ComClient {
	
	public static final String CONFIG_PROPERTIES = "config.properties";
	
	/**
	 * path of repository data. The repository config is a child of this
	 * directory.
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

	public static String STR_CONFIG_PATH = ".gdrservsimple";
	
	public static String getPathProperties()
	{
		return STR_CONFIG_PATH + "/" + CONFIG_PROPERTIES;
	}

	/**
	 * Create LocalAPIClient It looks up by config directory in _path specified.
	 * 
	 * @param _path
	 *            Path of repository
	 * @throws CreationAPIClientException
	 */
	public LocalFakeComClient(File _path) throws LocalFakeComClientException {
		if (!_path.isDirectory())
			throw new LocalFakeComClientException(_path.toString()
					+ "is not a directory", null);

		path = _path;

		File[] childs = _path.listFiles(new ANDFileFilter(
				new DirectoryFileFilter(), new NameFileFilter(STR_CONFIG_PATH)));

		if (childs.length < 1) 
		{
			try {
				pathConfig = new File(path.getPath(), STR_CONFIG_PATH);
				if (!pathConfig.mkdir())
					throw new LocalFakeComClientException(
							"Do not possible create config file: "
									+ pathConfig.getPath(), null);

			} catch (SecurityException e) {
				throw new LocalFakeComClientException(
						"Do not possible create config file because security questions: "
								+ pathConfig.getPath(), e);
			}
		} else {
			pathConfig = childs[0];
		}

		try {
			loadProperties();
		} catch (FileNotFoundException e) {
			throw new LocalFakeComClientException("Property file not found: ", e);
		} catch (IOException e) {
			throw new LocalFakeComClientException("Property file not found: ", e);
		}
	}
	
	/**TODO: Corrigir o Checkout*/
	@Override
	public RevisionID checkout(String strProject, RevisionID revision,  Collection<ConfigurationItem> items) throws ComClientException{
		FileInputStream input;
		if(revision.equals(RevisionID.LAST_REVISION))
		{
			revision = this.getLastRevision();
		}
		
		if(revision.getNumber() == 0 ) //< a última revisão não tem nada.
		{
			return revision;
		}
		
		try {
			input = new FileInputStream(getPathOfRevision(revision));
		} catch (FileNotFoundException e) {
			throw new ComClientException(
					"Não foi possível achar o arquivo de revisão.", "Checkout", getURIServ(), e);
		}
		ZipInputStream zos = new ZipInputStream(input);

		try {
			items.clear();
			ZipEntry entry = null;
			
			//read the log file
			{
				ByteArrayOutputStream propOut = new ByteArrayOutputStream();
				entry = zos.getNextEntry();
				if(!entry.getName().equals("config"))
				{
					zos.close();
					throw new LocalFakeComClientException("Cannot load config file in zip revision", "checkout", this.getURIServ(), null);
				}
				
				UtilStream.copy(zos, propOut);
				/*
				ByteArrayInputStream propIn = new ByteArrayInputStream(propOut.toByteArray());
				Properties prop = new Properties();
				prop.load(propIn);*/
				//não tem pq resgatar
			}

			while ((entry = zos.getNextEntry()) != null) {
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				UtilStream.copy(zos, os);

				InputStream is = new ByteArrayInputStream(os.toByteArray());
				//TODO Pedir para Vitor rever isso:
				URI name = null;
				try
				{
					name = new URI(entry.getName());
				}catch(URISyntaxException e)
				{
					try {
						name = new URI("www.uff.br");
					} catch (URISyntaxException e1) {
						throw new ComClientException("Cannot resolve uri syntax of a name",  "checkout", path.toURI(), e);
					}
				}
				items.add(new ConfigurationItem(name, is, CIType.file,revision, "nouser"));//put(entry.getName(), is);
			}
			zos.close();

		} catch (IOException e) {
			throw new ComClientException("Error at decompact revision.", "Checkout", getURIServ(), e);
		}
		
		return revision;
		
	}
	
	public String getPathOfRevision(RevisionID r) {
		return pathConfig.getPath() + File.separatorChar + r.toString()
				+ ".zip";
	}
	
	@Override
	public RevisionID commit(String strProject, String strMessage, String strUser, Collection<ConfigurationItem> items) throws ComClientException 
	{
		// create a ZipOutputStream to zip the data to

		ZipOutputStream zos;		
		/**TODO: Corrigir o Revision ID*/
		RevisionID newRevision = RevisionID.generateNewRevision(this.getLastRevision().getNumber());
		//RevisionID newRevision;
		try {
			zos = new ZipOutputStream(new FileOutputStream(getPathOfRevision(newRevision)));

			//write the log file
			{
				ByteArrayOutputStream propOut = new ByteArrayOutputStream();
				Properties prop = new Properties();
				prop.setProperty("user", strUser );
				prop.setProperty("message", strMessage);
				prop.setProperty("date", Long.toString((new Date()).getTime()));
				prop.store(propOut, "config of revision");
				
				zipInputStream("config", new ByteArrayInputStream(propOut.toByteArray()),zos);
			}
			// assuming that there is a directory named inFolder (If there
			// isn't create one) in the same directory as the one the code runs
			// from,
			// call the zipDir method
			for (ConfigurationItem ci : items) {
				zipInputStream(ci.getStringID(),ci.getItemAsInputStream(), zos);
			}

			properties.setProperty("LastRevision", RevisionID.generateNewRevision(getLastRevision().getNumber()).toString());
			
			File config = new File(String.format("%s%s%s", pathConfig.getPath(), File.separatorChar, CONFIG_PROPERTIES));
			
			properties.store(new FileOutputStream(config), "");
			// close the stream
			zos.close();

		} catch (FileNotFoundException e) {
			throw new ComClientException("Zip file not found", "Checkout", getURIServ(), e);
		} catch (IOException e) {
			throw new ComClientException("Zip process has problems", "Checkout", getURIServ(), e);
		}
		
		return newRevision;
	}
	
	private RevisionID getLastRevision() {
		return RevisionID.fromString(properties.getProperty("LastRevision"));
	}
	
	private void loadProperties() throws FileNotFoundException, IOException {
		File[] files = pathConfig.listFiles(new NameFileFilter(CONFIG_PROPERTIES));

		if (properties == null) {
			// init properties
			properties = new Properties();
			properties.setProperty("LastRevision",
					RevisionID.ZERO_REVISION.toString());
		}

		File config = null;
		if (files.length == 0) {
			config = new File(String.format("%s%s%s", pathConfig.getPath(), File.separatorChar, CONFIG_PROPERTIES));

			// store
			properties.store(new FileOutputStream(config), "");

			// try load again
			files = pathConfig.listFiles(new NameFileFilter(CONFIG_PROPERTIES));
		}

		if (files.length == 0)
			throw new FileNotFoundException("The file " + CONFIG_PROPERTIES
					+ " does not found");

		config = files[0];

		properties.load(new FileInputStream(config));
	}

	/**
	 * Compress a input stream "item" with key "id"
	 * 
	 * @param id
	 *            The id of data
	 * @param item
	 *            The data
	 * @param zos
	 *            the zip output
	 * @throws IOException
	 *             zipping has a problem
	 */
	public static void zipInputStream(String id, InputStream item, ZipOutputStream zos)
			throws IOException {

		int i = 0;
		for(; i < id.length(); i++)
		{
			if(id.charAt(i)== '/')
			{
				//faznada
			}else
			{
				break;
			}
		}
		String newId = id.substring(i);
		
		// create a new zip entry
		ZipEntry anEntry = new ZipEntry(newId);

		// place the zip entry in the ZipOutputStream object
		zos.putNextEntry(anEntry);
		// now write the content of the file to the ZipOutputStream
		UtilStream.copy(item, zos);

		// close the Stream
		item.close();
	}

	/**
	 * Method to compress a directory in a ZipOutputStream. The Source code in:
	 * http://www.devx.com/tips/Tip/14049
	 * 
	 * @param dir2zip
	 *            the directory path
	 * @param zos
	 *            the ZipOutputStream
	 */
	@SuppressWarnings("unused")
	private void zipDir(String dir2zip, ZipOutputStream zos) {
		try {
			// create a new File object based on the directory we have to zip
			File zipDir = new File(dir2zip);
			// get a listing of the directory content
			String[] dirList = zipDir.list();

			// loop through dirList, and zip the files
			for (int i = 0; i < dirList.length; i++) {
				File f = new File(zipDir, dirList[i]);
				if (f.isDirectory()) {
					// if the File object is a directory, call this
					// function again to add its content recursively
					String filePath = f.getPath();
					zipDir(filePath, zos);
					// loop again
					continue;
				}
				// if we reached here, the File object f was not a directory
				// create a FileInputStream on top of f
				FileInputStream fis = new FileInputStream(f);
				// create a new zip entry
				ZipEntry anEntry = new ZipEntry(f.getPath());

				// place the zip entry in the ZipOutputStream object
				zos.putNextEntry(anEntry);

				UtilStream.copy(fis, zos);
				// close the Stream
				fis.close();
			}
		} catch (Exception e) {
			// handle exception
		}

	}

	@Override
	public URI getURIServ() {
		return path.toURI();
	}

	@Override
	public void close() {		
	}

	@Override
	public void init(String strProject) {
		//fazNada pq ignora conceito de projeto
	}
	
	/**
	 * generateLog of the server
	 * @param list list of revisions loggeds
	 * @param revision the first revision. if equal to null, initiate at Revision 1.
	 * @param lastRevision the last revision. if equal null, initiate at last Revision
	 * @throws LocalFakeComClientException 
	 */
	@Override
	public
	void generateLog(Collection<RevisionCommited> coll,
			RevisionID firstRevision, RevisionID lastRevision) throws ComClientException
	{
		if(firstRevision == null)
			firstRevision = new RevisionID(1);
		
		if(lastRevision == null)
			lastRevision = RevisionID.LAST_REVISION;
		
		if(firstRevision.compareTo(lastRevision)>= 0)
			throw new LocalFakeComClientException("The fist revision should be smaller than last revision", null);
		
		///lê os itens
		for(long i = firstRevision.getNumber(); i<=lastRevision.getNumber(); i++)
		{
			RevisionID r = new RevisionID(i);
			
			InputStream input=  null;
			ZipInputStream zos = null;
			
			try {
				input = new FileInputStream(getPathOfRevision(r));
				zos = new ZipInputStream(input);
			} catch (FileNotFoundException e) {
				throw new ComClientException(
						"Não foi possível achar o arquivo de revisão.", "generateLog", getURIServ(), e);
			}
			
			ByteArrayOutputStream propOut = new ByteArrayOutputStream();
			ZipEntry entry = null;
			Properties prop = new Properties();
			try {
				entry = zos.getNextEntry();
			
				if(!entry.getName().equals("config"))
				{
					zos.close();
					throw new LocalFakeComClientException("Cannot load config file in zip revision", "checkout", this.getURIServ(), null);
				}
				UtilStream.copy(zos, propOut);
				prop.load(new ByteArrayInputStream(propOut.toByteArray()));
				zos.close();
			} catch (IOException e) {
				try {
					zos.close();
				} catch (IOException e1) {
				}
				throw new LocalFakeComClientException("Error in get config in the zip of revision",e);
			}
			
			String strUser = null;
			String strMessage = null;
			String strDate = null;
			strUser = prop.getProperty("user" );
			strMessage = prop.getProperty("message");
			strDate = prop.getProperty("date");
			RevisionCommited rc = new RevisionCommited(new RevisionID(i), strUser, strMessage, new Date(Long.parseLong(strDate)));
			coll.add(rc);
		}		
	}
	
	

	@Override
	public RevisionID getLastRevision(String strProject) 
	{
		return getLastRevision();
	}
}
