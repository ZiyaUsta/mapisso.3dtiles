package com.mapisso.shape;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.MultiPolygon;

public class ReadShapeFile {

	public SimpleFeatureCollection getSFC(File file) {

		SimpleFeatureCollection features = null;

		try {
			
			Map<String, Serializable> connectionParameters = new HashMap<>();
			connectionParameters.put("url", file.toURI().toURL());
			DataStore dataStore = DataStoreFinder.getDataStore(connectionParameters);

			String typeName = dataStore.getTypeNames()[0];
			String[] featureTypes = dataStore.getTypeNames();

			if (featureTypes.length != 1) {
				throw new NullPointerException("feature type list is null");
			}

			SimpleFeatureSource source = dataStore.getFeatureSource(typeName);
			features = source.getFeatures();

			dataStore.dispose();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return features;
	}

	public SimpleFeatureType getTypeNames(String filePath) throws IOException {

		File file = new File(filePath);

		Map<String, Serializable> connectionParameters = new HashMap<>();
		connectionParameters.put("url", file.toURI().toURL());

		DataStore dataStore = DataStoreFinder.getDataStore(connectionParameters);

		SimpleFeatureType type = dataStore.getSchema(dataStore.getTypeNames()[0]);
		
		dataStore.dispose();

		return type;

	}

	public List<MultiPolygon> getMultiPolygonList(SimpleFeatureCollection collection) {

		List<MultiPolygon> polygonList = new ArrayList<MultiPolygon>();

		SimpleFeatureIterator iterator = (SimpleFeatureIterator) collection.features();

		while (iterator.hasNext()) {
			SimpleFeature feature = iterator.next();
			MultiPolygon geom = (MultiPolygon) feature.getDefaultGeometry();
			polygonList.add(geom);

		}

		return polygonList;
	}

}
