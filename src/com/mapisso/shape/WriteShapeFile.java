package com.mapisso.shape;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class WriteShapeFile {
	
public void write(File outputFile, SimpleFeatureCollection inputCollection, SimpleFeatureType TYPE, CoordinateReferenceSystem inputCRS) throws Exception {
		
//	System.out.println("outputFile: " + outputFile.toString());
		ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();

		Map<String, Serializable> params = new HashMap<String, Serializable>();
		params.put("url", outputFile.toURI().toURL());
		params.put("create spatial index", Boolean.TRUE);

		ShapefileDataStore newDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
		newDataStore.createSchema(TYPE);

		/*
		 * You can comment out this line if you are using the createFeatureType method
		 * (at end of class file) rather than DataUtilities.createType
		 */
		
		CoordinateReferenceSystem crs = inputCRS;
		
		newDataStore.forceSchemaCRS(crs);

		Transaction transaction = new DefaultTransaction("create");

		String typeName = newDataStore.getTypeNames()[0];
		SimpleFeatureSource featureSource = newDataStore.getFeatureSource(typeName);

		if (featureSource instanceof SimpleFeatureStore) {
			SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;

			featureStore.setTransaction(transaction);
			try {
				featureStore.addFeatures(inputCollection);
				transaction.commit();

			} catch (Exception problem) {
				problem.printStackTrace();
				transaction.rollback();

			} finally {
				transaction.close();
				newDataStore.dispose();
			}
			//System.exit(0); // success!
		} else {
			System.out.println(typeName + " does not support read/write access");
			//System.exit(1);
		}
	}

}
