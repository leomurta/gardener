package br.uff.ic.gardener.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class UtilStream {

	public static final int DEFAULT_BUFFER_SIZE = 2156;

	private static byte[] BUFFER = new byte[DEFAULT_BUFFER_SIZE];

	public static void copy(InputStream input, OutputStream output)
			throws IOException {
		copy(input, output, BUFFER);
	}

	public static void copy(InputStream input, OutputStream output,
			int sizeBuffer) throws IOException {
		byte[] buffer = null;
		if (sizeBuffer != DEFAULT_BUFFER_SIZE)
			buffer = new byte[sizeBuffer];
		else
			buffer = BUFFER;

		copy(input, output, buffer);
	}

	public static void copy(InputStream input, OutputStream output,
			byte[] buffer) throws IOException {
		int byteRead = 0;
		while ((byteRead = input.read(buffer)) > 0) {
			output.write(buffer, 0, byteRead);
		}
	}
}
