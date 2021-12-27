package com.mapisso.obj;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mapisso.model.obj.Obj;
import com.vividsolutions.jts.geom.Coordinate;

public class ObjReader {

	public List<Obj> read(String fileLocation) throws IOException {

		File file = new File(fileLocation);

		Map<String, String> vMap = new LinkedHashMap<String, String>();
		Map<String, String> fMap = new LinkedHashMap<String, String>();
		List<String> objNameList = new ArrayList<String>();

		if (file.exists()) {

			List<String> textLines = Files.readAllLines(java.nio.file.Paths.get(fileLocation), StandardCharsets.UTF_8);

			String oValue = "";

			for (int i = 0; i < textLines.size(); i++) {

				String line = textLines.get(i);

				if (line.startsWith("o")) {

					oValue = line.substring(2, line.length());
					objNameList.add(oValue);

				}

				if (line.startsWith("v")) {

					if (vMap.get(oValue) != null) {
						vMap.put(oValue, vMap.get(oValue) + "," + line.substring(2));
					} else {
						vMap.put(oValue, line.substring(2));
					}

				}

				if (line.startsWith("f")) {

					if (fMap.get(oValue) != null) {
						fMap.put(oValue, fMap.get(oValue) + "," + line.substring(2));
					} else {
						fMap.put(oValue, line.substring(2));
					}

				}
			}

		}
		
		List<Obj> objList = new ArrayList<Obj>();
		
		for (int i = 0; i < objNameList.size(); i++) {
			
			Obj obj = new Obj();
			
			String objName = objNameList.get(i);
			List<List<Integer>> fList = createFList(fMap.get(objNameList.get(i)));
			List<Coordinate> tempObjCoordList = createObjCoordList(vMap.get(objNameList.get(i)));
			
			obj.setO(objName);
			obj.setF(fList);
			obj.setV(tempObjCoordList);
			
			objList.add(obj);
			
		}

		
		return objList;

	}
	
	public static List<Coordinate> createObjCoordList(String vCoordString){
		
		String[] splitted = vCoordString.split(",");
		
		List<Coordinate> tempObjCoordList = new ArrayList<Coordinate>();
		for (int i = 0; i < splitted.length; i++) {
			
			String[] coordArray = splitted[i].split("\\s+");
			
			String y = coordArray[0];
			String x = coordArray[1];
			String z = coordArray[2];
			
			Coordinate coord = new Coordinate(Double.valueOf(x),Double.valueOf(y), Double.valueOf(z));
			tempObjCoordList.add(coord);
			
		}
		
		return tempObjCoordList;
	}

	public static List<List<Integer>> createFList(String fString) {

		String[] splitted = fString.split(",");

		List<List<Integer>> tempFList = new ArrayList<List<Integer>>();

		for (int i = 0; i < splitted.length; i++) {

			String[] coordArray = splitted[i].split("\\s+");

			List<Integer> faceList = new ArrayList<Integer>();

			for (int j = 0; j < coordArray.length; j++) {

				faceList.add(Integer.valueOf(coordArray[j]));

			}

			tempFList.add(faceList);

		}

		return tempFList;

	}

}
