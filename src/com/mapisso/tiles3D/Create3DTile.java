package com.mapisso.tiles3D;

import java.io.File;
import java.util.List;

import com.mapisso.b3dm.v1.B3dmEngine;
import com.mapisso.b3dm.v1.B3dmWriter;
import com.mapisso.gltf.v2.GLTFEngine;
import com.mapisso.gltf.v2.GLTFWriter;
import com.mapisso.model.gltf.v2.GLTF;
import com.mapisso.model.obj.Obj;
import com.mapisso.model.tiles3D.Asset;
import com.mapisso.model.tiles3D.Content;
import com.mapisso.model.tiles3D.Height;
import com.mapisso.model.tiles3D.Node;
import com.mapisso.model.tiles3D.Properties;
import com.mapisso.model.tiles3D.Region;
import com.mapisso.model.tiles3D.Tile;
import com.mapisso.obj.ObjEngine;
import com.mapisso.obj.ObjWriter;
import com.mapisso.shape.CreateShapeFile;

public class Create3DTile {
	
	private String parentDirectory;
	private String heightAttributeName;

	private ObjEngine objEngine = null;
	private GLTFEngine gltfEngine = new GLTFEngine();
	private B3dmEngine b3dmEngine = new B3dmEngine();
	
	private ObjWriter objWriter = new ObjWriter();
	private GLTFWriter gltfWriter = new GLTFWriter();
	private B3dmWriter b3dmWriter = new B3dmWriter();
	
	private CreateShapeFile createShapeFile = new CreateShapeFile();
	
	private int z = 1;
	
	public Create3DTile(String parentDirectory, String fileFormat, String heightAttributeName) {
		this.parentDirectory = parentDirectory;
		this.heightAttributeName = heightAttributeName;
		objEngine = new ObjEngine(fileFormat);
	}

	public Tile create(Node rootNode, File fileDirectory) {

		this.parentDirectory = fileDirectory.getParent();

		Tile tile = new Tile();

		Asset asset = new Asset();
		asset.setVersion("1.0");
		asset.setTilesetVersion("1.2.3");
		asset.setGltfUpAxis("Z");
		tile.setAsset(asset);

		Region region = rootNode.getBoundingVolume();
		Properties properties = new Properties();
		properties.setHeight(new Height(region.getRegion().get(4), region.getRegion().get(5)));
		tile.setProperties(properties);

		tile.setGeometricError(rootNode.getGeometricError());

		setAllNodeAttribute(rootNode);

		rootNode.setRefine("ADD");

		Content content = new Content();
		content.setUri("0.b3dm");
		content.setBoundingVolume(region);
		rootNode.setContent(content);

		tile.setRoot(rootNode);

		return tile;

	}

	public void setAllNodeAttribute(Node n) {

		if (n.getChildren() != null) {

			Node group1 = n.getChildren().get(0);
			Node group2 = n.getChildren().get(1);

			Double geometricError1 = n.getGeometricError() / 2;
			Double geometricError2 = n.getGeometricError() / 2;

			if (group1.getChildren() == null) {
				geometricError1 = 0.0;
			}

			if (group2.getChildren() == null) {
				geometricError2 = 0.0;
			}

			group1.setGeometricError(geometricError1);
			group2.setGeometricError(geometricError2);

			group1.setRefine("ADD");
			group2.setRefine("ADD");

			Content content1 = new Content();
			content1.setUri(z + ".b3dm");
			content1.setBoundingVolume(group1.getBoundingVolume());
			group1.setContent(content1);

			File file = new File(this.parentDirectory + "/" + z + ".shp");
			createShapeFile.create(group1.getData(), file, group1.getBoundingVolume().getSourceCRS());
			
			File objFile = new File(this.parentDirectory + "/" + z  + ".obj");
			List<Obj> objList = objEngine.start(file, heightAttributeName);
			String obj = objWriter.createObjContent(objList);
			objWriter.createObjFile(obj, objFile);
			
			File gltfFile = new File(this.parentDirectory + "/" + z + ".gltf");
			GLTF gltf = gltfEngine.createGLTF(objList);
			gltfWriter.write(gltf, gltfFile);
			
			File b3dmFile = new File(this.parentDirectory + "/" + z + ".b3dm");
			byte[] b3dm = b3dmEngine.createB3dm(objList);
			b3dmWriter.write(b3dm, b3dmFile);

			Content content2 = new Content();

			z = z + 1;
			System.out.println("z:  " + z);
			content2.setUri(z + ".b3dm");
			content2.setBoundingVolume(group2.getBoundingVolume());
			group2.setContent(content2);

			file = new File(this.parentDirectory + "/" + z + ".shp");
			createShapeFile.create(group2.getData(), file, group2.getBoundingVolume().getSourceCRS());
			
			objFile = new File(this.parentDirectory + "/" + z  + ".obj");
			objList = objEngine.start(file, heightAttributeName);
			obj = objWriter.createObjContent(objList);
			objWriter.createObjFile(obj, objFile);
			
			gltfFile = new File(this.parentDirectory + "/" + z + ".gltf");
			gltf = gltfEngine.createGLTF(objList);
			gltfWriter.write(gltf, gltfFile);
			
			b3dmFile = new File(this.parentDirectory + "/" + z + ".b3dm");
			b3dm = b3dmEngine.createB3dm(objList);
			b3dmWriter.write(b3dm, b3dmFile);
			
			z = z + 1;
			System.out.println("z:  " + z);

			setAllNodeAttribute(group1);
			setAllNodeAttribute(group2);

		}

	}

}
