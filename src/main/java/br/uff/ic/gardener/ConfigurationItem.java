package br.uff.ic.gardener;
import java.io.InputStream;
import java.net.URI;

import br.uff.ic.gardener.RevisionID;
/**
 * Configuration Item Class. Works like a Configuration Item data and metadata container.
 * 
 * @author Vitor Neves*/
public class ConfigurationItem {
	private InputStream item;
	private CIType type;
	private RevisionID revision;
	private String user;
	private URI uri;
	
	public InputStream getItemAsInputStream() {
		return item;
	}
	/*public void setItemFromInputStream(InputStream item) {
		this.item = item;
	}*/
	
	/**TODO: Fazer a conversão da forma apropriada e não meramente ilustrativa*/
	//public File getItemAsFile(){
	//	return new File(this.item.toString());
	//}
	
	/**TODO: Fazer a conversão da forma apropriada e não meramente ilustrativa*/
	//public void setItemFromFile(File item){
	//	this.item = item. 
	//}
	
	public CIType getType() {
		return type;
	}
	public void setType(CIType type) {
		this.type = type;
	}
	
	public RevisionID getRevision() {
		return revision;
	}
	
	public void setRevision(RevisionID revision) {
		this.revision = revision;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public URI getUri() {
		return uri;
	}
	public void setUri(URI _uri) {
		this.uri = _uri;
	}
	
	public ConfigurationItem(URI _uri, InputStream _item, RevisionID rev)
	{
		
		this(_uri, _item, CIType.file, rev, "");
	}
	public ConfigurationItem(URI _uri, InputStream _item, CIType _type, RevisionID _revision, String _user)
	{
		item 	= _item;
		uri 	= _uri;
		type 	= _type;
		user 	= _user;
	}

	/**
	 * Return the identificator in String form
	 * @return
	 */
	public String getStringID() {
		return uri.getPath();
	}
}
