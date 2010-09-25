package br.uff.ic.gardener.workspace;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.TreeMap;

import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.TransationException;
import br.uff.ic.gardener.client.APIClient;
import br.uff.ic.gardener.util.UtilStream;

public class Workspace {

	/**
	 * Path of the workspace
	 */
	private File path = null;

	/**
	 * reference to the client aplication
	 */
	private APIClient client = null;

	/**
	 * get APIClient of application
	 * 
	 */
	private APIClient getClient() {
		return client;
	}

	/**
	 * The rurrent revisionID of workspace
	 */
	private RevisionID currentRevision = null;

	public RevisionID getCurrentRevision() {
		return currentRevision;
	}

	public void setCurrentRevision(RevisionID revision) {
		currentRevision = revision;
	}

	/**
	 * Constructor. it needs a path to look up for the .gdr file.
	 * 
	 * @param pathOfWorkspace
	 */
	public Workspace(File pathOfWorkspace, APIClient _client) {
		path = pathOfWorkspace;

		if (!path.isDirectory())
			throw new WorkspaceError(pathOfWorkspace, "the path:("
					+ pathOfWorkspace.toString()
					+ ") is not a valid directory.", null);

		client = _client;

		if (client == null)
			throw new WorkspaceError(pathOfWorkspace,
					"the APIClient is not valid (is null)", null);

		path = pathOfWorkspace;
	}

	private class NotInitDotFileFilter implements FileFilter {

		@Override
		public boolean accept(File arg0) {
			String temp = arg0.getName();
			return temp.length() == 0 || temp.charAt(0) != '.';
		}

	}

	/**
	 * Commita as alterações Para isto ele pega os arquivos e diretórios do
	 * diretório corrente, ignorando os .diretórios
	 * 
	 * @throws WorkspaceException
	 */
	public void commit() throws WorkspaceException {
		File files[] = path.listFiles(new NotInitDotFileFilter());

		Map<String, InputStream> map = new TreeMap<String, InputStream>();

		try {
			for (File f : files) {
				InputStream is;

				is = new FileInputStream(f);

				java.io.ByteArrayOutputStream outBuffer = new ByteArrayOutputStream();

				try {
					UtilStream.copy(is, outBuffer);

				} catch (IOException e) {
					throw new WorkspaceException(f,
							"Erro ao gravar arquivo no buffer em memória", e);
				}

				InputStream inputStream = new ByteArrayInputStream(
						outBuffer.toByteArray());
				map.put(f.getName(), inputStream);
			}
		} catch (FileNotFoundException e) {
			throw new WorkspaceException(path, "Arquivo não encontrado", e);
		}

		try {
			getClient().commit(map);
		} catch (TransationException e) {
			throw new WorkspaceException(this.path,
					"não foi possível enviar as alterações para o servidor.", e);
		}
	}

	/**
	 * Implements the completely checkout, generate the workspace It will get
	 * the flow data of serv and will build the Workspace.
	 * 
	 * @throws WorkspaceException
	 */
	public void checkout(RevisionID revision) throws WorkspaceException {
		Map<String, InputStream> map = new TreeMap<String, InputStream>();
		try {
			getClient().checkout(map, revision);
		} catch (TransationException e) {
			throw new WorkspaceException(this.path,
					"não foi possível resgatar as alterações do o servidor.", e);
		}

		// erase content
		for (File f : path.listFiles(new NotInitDotFileFilter())) {
			f.delete();
		}

		for (Map.Entry<String, InputStream> e : map.entrySet()) {
			File f = new File(path, e.getKey());
			try {
				f.createNewFile();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				throw new WorkspaceException(f,
						"Do not create file in repository", e2);
			}

			try {

				OutputStream out = new FileOutputStream(f);
				UtilStream.copy(e.getValue(), out);

			} catch (IOException e1) {
				throw new WorkspaceException(f,
						"Do not copy data from repository", e1);
			}

		}

	}

	/**
	 * Close the things
	 */
	public void close() {
		// fecha o que venha a ser necessário fechar
	}
}
