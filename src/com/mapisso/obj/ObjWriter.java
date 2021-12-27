package com.mapisso.obj;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.mapisso.model.obj.Obj;
import com.mapisso.utils.ObjUtils;

public class ObjWriter {
	
	private ObjUtils objUtils = new ObjUtils();
	
	public String createObjContent(List<Obj> objList) {
		
		String obj  = "";
		
		for (int i = 0; i < objList.size(); i++) {
			
			Obj tempObj = objList.get(i);
			String o = "o " + tempObj.getO() + "\n";
			String v = objUtils.transformToV(tempObj.getV()) + "\n";
			String f = objUtils.transformToF(tempObj.getF()) + "\n";
			
			obj = obj + o + v + f;
			
		}
		
		return obj;

	}
	
	public void createObjFile(String objContent, File fileLocation) {
		
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(fileLocation.getPath()));
			writer.write(objContent);

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
