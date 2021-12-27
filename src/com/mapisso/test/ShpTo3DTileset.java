package com.mapisso.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mapisso.b3dm.v1.B3dmEngine;
import com.mapisso.b3dm.v1.B3dmWriter;
import com.mapisso.gltf.v2.GLTFEngine;
import com.mapisso.gltf.v2.GLTFWriter;
import com.mapisso.model.gltf.v2.GLTF;
import com.mapisso.model.obj.Obj;
import com.mapisso.model.tiles3D.Building;
import com.mapisso.model.tiles3D.Height;
import com.mapisso.model.tiles3D.Node;
import com.mapisso.model.tiles3D.Region;
import com.mapisso.model.tiles3D.Tile;
import com.mapisso.obj.ObjEngine;
import com.mapisso.obj.ObjWriter;
import com.mapisso.rtree.RtreeNodeSplitter;
import com.mapisso.shape.CreateShapeFile;
import com.mapisso.shape.ReadShapeFile;
import com.mapisso.tiles3D.Create3DTile;
import com.mapisso.utils.RtreeUtils;
import com.vividsolutions.jts.geom.Envelope;

public class ShpTo3DTileset {
	
	private static ReadShapeFile readShapeFile = new ReadShapeFile();
	
	private static RtreeUtils rtreeUtils = new RtreeUtils();
	private static RtreeNodeSplitter quadraticNodeSplitter = new RtreeNodeSplitter();
	
	private static CreateShapeFile createShapeFile = new CreateShapeFile();
	
	private static ObjEngine objEngine = null;
	private static GLTFEngine gltfEngine = new GLTFEngine();
	private static B3dmEngine b3dmEngine = new B3dmEngine();
	
	private static ObjWriter objWriter = new ObjWriter();
	private static GLTFWriter gltfWriter = new GLTFWriter();
	private static B3dmWriter b3dmWriter = new B3dmWriter();
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		long methodStartTime = System.currentTimeMillis();
		
		File fileDirectory = new File("/Users/ziya/Desktop/SolarProject/mapisso/shp/izmir43261.shp");
		
		/**
		 * read building data into simple feature collection
		 */
		SimpleFeatureCollection featureCollection = readShapeFile.getSFC(fileDirectory);
		CoordinateReferenceSystem inputFileCRS = featureCollection.getSchema().getCoordinateReferenceSystem();
		
		List<Building> buildingList = rtreeUtils.createBuildingList(featureCollection);
		
		/**
		 * calculating minumum and maximum height value from building object list
		 */
		Number minimumHeight = rtreeUtils.calculateMinimumHeightValue(buildingList);
		Number maximumHeight = rtreeUtils.calculateMaximumHeightValue(buildingList);
		
		/**
		 * calculating bbox from building object list
		 */
		Envelope envelope = featureCollection.getBounds();
		
		Height height = new Height(minimumHeight.doubleValue(), maximumHeight.doubleValue());
		
		Region region = new Region(envelope, height, inputFileCRS);
		/**
		 * create root node for 3dtiles
		 */
		Node rootNode = new Node();

		rootNode.setEnvelope(envelope);
		rootNode.setParent(null);
		rootNode.setData(buildingList);
		rootNode.setBoundingVolume(region);
		
		/**
		 * split building object list via rtree implementation
		 */
		rootNode = quadraticNodeSplitter.split(rootNode);
		
		Create3DTile create3DTile = new Create3DTile(fileDirectory.getParent(), "shape", "height");
		
		Tile tile = create3DTile.create(rootNode, fileDirectory);
		
		File shapeFile = new File(fileDirectory.getParent() + "/0.shp");

		createShapeFile.create(rootNode.getData(), shapeFile, rootNode.getBoundingVolume().getSourceCRS());
		
		File objFile = new File(fileDirectory.getParent() + "/0.obj");
		objEngine = new ObjEngine("shape");
		List<Obj> objList = objEngine.start(shapeFile, "height");
		String obj = objWriter.createObjContent(objList);
		objWriter.createObjFile(obj, objFile);
		
		File gltfFile = new File(fileDirectory.getParent() + "/0.gltf");
		GLTF gltf = gltfEngine.createGLTF(objList);
		gltfWriter.write(gltf, gltfFile);
		
		File b3dmFile = new File(fileDirectory.getParent() + "/0.b3dm");
		byte[] b3dm = b3dmEngine.createB3dm(objList);
		b3dmWriter.write(b3dm, b3dmFile);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		
		ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
		writer.writeValue(new File(fileDirectory.getParent() + "/" + "tileset.json"), tile);
		
		long methodFinishedTime = System.currentTimeMillis();
		
		System.out.println("3D TILES ENGINE: transformation process finished. elapsed time: " + (methodFinishedTime - methodStartTime) + " ms.");
		
	}

}
