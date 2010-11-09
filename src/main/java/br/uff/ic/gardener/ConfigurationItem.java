package br.uff.ic.gardener;
import java.io.InputStream;
import java.net.URI;

import br.uff.ic.gardener.RevisionID;
/**
 * Configuration Item Class. Works like a Configuration Item data and metadata container.
 * 
 * @author Vitor Neves*/
public class ConfigurationItem {

	/**InputStream source of CI data.
	*/
	private InputStream item;
	
	/**Type of CI
	  */
	private CIType type;
	
	/**Revision source of IC
	  */
	private RevisionID revision;
	
	/**The user owner of IC
	  */
	private String user;
	
	/**Locator/Identificator of IC
	  */
	private URI uri;
	
	/**Generate and return de source IC data
	  */
	public InputStream getItemAsInputStream() {
		return item;
	}

	/**Return the IC type.
	  */
	public CIType getType() {
		return type;
	}
	
	/**Return revision of IC
	  */
	public RevisionID getRevision() {
		return revision;
	}
	
	/**Return user Owner of IC
	  */
	public String getUser() {
		return user;
	}

	/** Return ID of IC
	  */
	public URI getUri() {
		return uri;
	}


	/**Constructor
	  *@param _uri Identificator/Locator of IC
	  *@param _item source of data
	  *@param _type the Type of IC
	  *@param _revision the revision source of IC
	  *@param _user the user IC owner 
	  */
	public ConfigurationItem(URI _uri, InputStream _item, CIType _type, RevisionID _revision, String _user)
	{
		item 	= _item;
		uri 	= _uri;
		revision = _revision;
		type 	= _type;
		user 	= _user;                
	}
	
	/**Constructor. This constructor infer the IC type by InputStream Type
	  *@param _uri Identificator/Locator of IC
	  *@param _item source of data
	  *@param _type the Type of IC
	  *@param _revision the revision source of IC
	  *@param _user the user IC owner 
	  */
	public ConfigurationItem(URI _uri, InputStream _item, RevisionID _revision)
	{
		item 	= _item;
		uri 	= _uri;
		revision= _revision;
		type 	= null;
		user 	= null;
	}
	/**
	 * Return the identificator in String form
	 * @return
	 */
	public String getStringID() {
		return uri.getPath();
	}
	
	@Override
	public String toString()
	{
		return String.format("(%s, %s)", uri.getPath(), revision.toString());
	}
}
