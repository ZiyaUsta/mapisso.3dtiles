package com.mapisso.test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.mapisso.b3dm.v1.B3dmEngine;
import com.mapisso.model.obj.Obj;
import com.mapisso.obj.ObjEngine;

public class ShpToB3DMTest {
	
	private static B3dmEngine b3dmWriter = new B3dmEngine();

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		File fileDirectory = new File("/Users/mapisso/Desktop/SolarProject/mapisso/shp/binagrup.shp");

		ObjEngine objEngine = new ObjEngine("shape");
		List<Obj> objList = objEngine.start(fileDirectory, "height");
		
		byte[] b3dm = b3dmWriter.createB3dm(objList);
		
		String targetFile = "/Users/mapisso/Desktop/SolarProject/mapisso/mapisso.b3dm";

		// Create a new file and override when already exists:
		try (OutputStream output = openFile(targetFile)) {
			output.write(b3dm);
		}

	}
	
	private static BufferedOutputStream openFile(String fileName) throws IOException {
		return openFile(fileName, false);
	}

	public static BufferedOutputStream openFile(String fileName, boolean append) throws IOException {
		// Don't forget to add buffering to have better performance!
		return new BufferedOutputStream(new FileOutputStream(fileName, append));
	}

}
