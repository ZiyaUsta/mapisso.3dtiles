package com.mapisso.b3dm.v1;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class B3dmWriter {
	
	public void write(byte[] b3dm, File fileLocation) {
		
		// Create a new file and override when already exists:
		try (OutputStream output = openFile(fileLocation.getPath())) {
			output.write(b3dm);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static BufferedOutputStream openFile(String fileName) throws IOException {
		return openFile(fileName, false);
	}

	private static BufferedOutputStream openFile(String fileName, boolean append) throws IOException {
		// Don't forget to add buffering to have better performance!
		return new BufferedOutputStream(new FileOutputStream(fileName, append));
	}

}
