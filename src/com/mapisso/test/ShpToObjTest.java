package com.mapisso.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.mapisso.model.obj.Obj;
import com.mapisso.obj.ObjEngine;
import com.mapisso.obj.ObjWriter;

public class ShpToObjTest {
	
	private static ObjWriter objWriter = new ObjWriter();

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		File fileDirectory = new File("/Users/ziya/Documents/test/bina1.shp");

		ObjEngine objEngine = new ObjEngine("shape");

		List<Obj> objList = objEngine.start(fileDirectory, "height");
		
		String result = objWriter.createObjContent(objList);

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter("/Users/ziya/Documents/Applications/shadowMapping/bina.obj"));
			writer.write(result);

		} catch (IOException e) {
		} finally {
			try {
				if (writer != null)
					writer.close();
			} catch (IOException e) {
			}
		}

	}

}
