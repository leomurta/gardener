package br.uff.ic.gardener.workspace;

public class WorkspaceConfigParserException extends Exception {
	
	private String strToken = "";
	
	WorkspaceConfigParserException(String message, String token,  Exception parent)
	{
		strToken = token; 		
	}

	public void setStrToken(String strToken) {
		this.strToken = strToken;
	}

	public String getStrToken() {
		return strToken;
	}
}
