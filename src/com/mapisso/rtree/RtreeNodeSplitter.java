package com.mapisso.rtree;

import java.util.ArrayList;
import java.util.List;

import com.mapisso.constants.RtreeConstants;
import com.mapisso.model.tiles3D.Building;
import com.mapisso.model.tiles3D.Height;
import com.mapisso.model.tiles3D.Node;
import com.mapisso.model.tiles3D.Region;
import com.mapisso.utils.RtreeUtils;
import com.vividsolutions.jts.geom.Envelope;

public class RtreeNodeSplitter implements NodeSplitter {
	
	private Envelope bBoxAllData = null;
	private RtreeUtils rtreeUtils = new RtreeUtils();
	
	public Node split(Node n) {

		if (n.getData().size() <= RtreeConstants.RTREE_MAX_VALUE) {
			System.out.println("Data size is smaller then RTREE MAX VALUE parameter value: " + RtreeConstants.RTREE_MAX_VALUE);
		} else {

			Node group1 = new Node();
			Node group2 = new Node();
			
			bBoxAllData = n.getEnvelope();

			List<Building> list = n.getData();

			List<Node> groupedBuildings = findFarAwayBuildings(list, group1, group2);
			
			group1 = groupedBuildings.get(0);
			group2 = groupedBuildings.get(1);

			group1.setParent(n);
			group2.setParent(n);

			distributeLeaves(n, group1, group2);
			
			Double geometricErrorRootNode = rtreeUtils.calculateGeometricError(n);
			n.setGeometricError(geometricErrorRootNode);

			Envelope group1Envelope = computeMBR(group1);
			Envelope group2Envelope = computeMBR(group2);
			
			Double minHeightGroup1 = rtreeUtils.calculateMinimumHeightValue(group1.getData()).doubleValue();
			Double maxHeightGroup1 = rtreeUtils.calculateMaximumHeightValue(group1.getData()).doubleValue();
			
			Double minHeightGroup2 = rtreeUtils.calculateMinimumHeightValue(group2.getData()).doubleValue();
			Double maxHeightGroup2 = rtreeUtils.calculateMinimumHeightValue(group2.getData()).doubleValue();
			
			Region region1 = new Region(group1Envelope, new Height(minHeightGroup1, maxHeightGroup1), n.getBoundingVolume().getSourceCRS());
			Region region2 = new Region(group2Envelope, new Height(minHeightGroup2, maxHeightGroup2), n.getBoundingVolume().getSourceCRS());
			
			group1.setBoundingVolume(region1);
			group2.setBoundingVolume(region2);
			
			ArrayList<Node> childrenList = new ArrayList<Node>();
			childrenList.add(group1);
			childrenList.add(group2);

			n.setChildren(childrenList);

			split(group1);
			split(group2);

		}
		
		return n;

	}

	public Envelope computeMBR(Node n) {

		Envelope bBox = null;

		if (n.getData().isEmpty())
			return null;

		bBox = n.getData().get(0).getEnvelope();

		for (int i = 1; i < n.getData().size(); i++) {

			bBox = merge(bBox, n.getData().get(i).getEnvelope());
		}
		
		return bBox;

	}

	public Envelope merge(Envelope inputEnvelope, Envelope willBeMergedEnvelope) {
		
		Double minX = Math.min(inputEnvelope.getMinX(), willBeMergedEnvelope.getMinX());
		Double maxX = Math.max(inputEnvelope.getMaxX(), willBeMergedEnvelope.getMaxX());
		Double minY = Math.min(inputEnvelope.getMinY(), willBeMergedEnvelope.getMinY());
		Double maxY = Math.max(inputEnvelope.getMaxY(), willBeMergedEnvelope.getMaxY());

		return new Envelope(minX, maxX, minY, maxY);
	}

	public List<Node> findFarAwayBuildings(List<Building> buildingCollection, Node g1, Node g2) {

		List<Node> nodeList = new ArrayList<Node>();

		Double minDistanceUpperleft = 0.0;
		Double maxDistanceUpperLeft = 0.0;

		Double minDistanceUpperRight = 0.0;
		Double maxDistanceUpperRight = 0.0;
		
		Envelope buildingLeftMinumum = null;
		Envelope buildingLeftMaximum = null;

		Envelope buildingRightMinumum = null;
		Envelope buildingRightMaximum = null;
		
		for (int i = 0; i < buildingCollection.size(); i++) {
			
			Envelope tempBuildingEnvelope = buildingCollection.get(i).getEnvelope();
			
			Double tempDistanceUpperLeft = calculateDistanceUpperLeft(bBoxAllData, tempBuildingEnvelope);
			Double tempDistanceUpperRight = calculateDistanceUpperRight(bBoxAllData, tempBuildingEnvelope);
			
			if (i == 1) {
				minDistanceUpperleft = maxDistanceUpperLeft = tempDistanceUpperLeft;
				buildingLeftMinumum = buildingLeftMaximum = tempBuildingEnvelope;
			} else {

				if (tempDistanceUpperLeft < minDistanceUpperleft) {
					minDistanceUpperleft = tempDistanceUpperLeft;
					buildingLeftMinumum = tempBuildingEnvelope;
				}

				if (tempDistanceUpperLeft > maxDistanceUpperLeft) {
					maxDistanceUpperLeft = tempDistanceUpperLeft;
					buildingLeftMaximum = tempBuildingEnvelope;
				}
			}

			if (i == 1) {
				minDistanceUpperRight = maxDistanceUpperRight = tempDistanceUpperRight;
				buildingRightMinumum = buildingRightMaximum = tempBuildingEnvelope;
			} else {
				if (tempDistanceUpperRight < minDistanceUpperRight) {
					minDistanceUpperRight = tempDistanceUpperRight;
					buildingRightMinumum = tempBuildingEnvelope;
				}

				if (tempDistanceUpperRight > maxDistanceUpperRight) {
					maxDistanceUpperRight = tempDistanceUpperRight;
					buildingRightMaximum = tempBuildingEnvelope;
				}

			}
			
		}
		
		if (minDistanceUpperRight < minDistanceUpperleft) {
			g1.setEnvelope(buildingRightMinumum);
			g2.setEnvelope(buildingRightMaximum);
		} else {
			g1.setEnvelope(buildingLeftMinumum);
			g2.setEnvelope(buildingLeftMaximum);
		}

		nodeList.add(g1);
		nodeList.add(g2);

		return nodeList;
	}

	public double calculateDistanceUpperLeft(Envelope inputEnvelope, Envelope buildingEnvelope) {

		return ((inputEnvelope.getMaxX() - buildingEnvelope.getMaxX()) + (buildingEnvelope.getMinY() - inputEnvelope.getMinY()))
				/ 2;

	}

	public double calculateDistanceUpperRight(Envelope inputEnvelope, Envelope buildingEnvelope) {

		return ((inputEnvelope.getMaxX() - buildingEnvelope.getMaxX()) + (buildingEnvelope.getMaxY() - inputEnvelope.getMaxY()))
				/ 2;

	}

	private void distributeLeaves(Node n, Node g1, Node g2) {

		List<Building> g1Data = new ArrayList<Building>();
		List<Building> g2Data = new ArrayList<Building>();

		while (!n.getData().isEmpty() && n.getData().size() != 1) {
			// Pick next
			Double difmax = Double.MIN_VALUE;
			
			int nmax_index = -1;
			
			List<Building> buildingCollection = n.getData();
			
			for (int i = 0; i < buildingCollection.size(); i++) {
				
				Building building = buildingCollection.get(i);
				
				Envelope tempBuildingEnvelope = building.getEnvelope();
				
				double d1 = calculateExpansionValue(tempBuildingEnvelope, g1.getEnvelope());
				double d2 = calculateExpansionValue(tempBuildingEnvelope, g2.getEnvelope());
				
				double difference = Math.abs(d1 - d2);

				if (difference > difmax) {
					difmax = difference;
					nmax_index = i;
				}
				
				i += 1;
				
			}
			
			assert (nmax_index != -1);

			// Distribute Entry
			// System.out.println("n get data size: " + n.getData().size());
			Building nMax = n.getData().remove(nmax_index);
			// System.out.println("n data size: " + n.getData().size());

			// ... to the one with the least expansion
			double overlap1 = calculateExpansionValue(g1.getEnvelope(), nMax.getEnvelope());
			double overlap2 = calculateExpansionValue(g2.getEnvelope(), nMax.getEnvelope());

			if (overlap1 >= overlap2) {
				g1Data.add(nMax);
			} else if (overlap2 > overlap1) {
				g2Data.add(nMax);
			}
		}

		g1.setData(g1Data);
		g2.setData(g2Data);

	}

	/**
	 * Returns the amount that other will need to be expanded to fit this.
	 */
	public double calculateExpansionValue(Envelope nodeEnvelope, Envelope buildingEnvelope) {
		
		double expansionValue = 0.0d;
		
		//minx miny, maxx miny, maxx maxy, minx maxy, minx miny

		if (buildingEnvelope.getMinY()< nodeEnvelope.getMinY())
			expansionValue += nodeEnvelope.getMinY() - buildingEnvelope.getMinY();
		if (buildingEnvelope.getMaxY() > nodeEnvelope.getMaxY())
			expansionValue += buildingEnvelope.getMaxY() - nodeEnvelope.getMaxY();

		if (buildingEnvelope.getMinX() < nodeEnvelope.getMinX())
			expansionValue += nodeEnvelope.getMinX() - buildingEnvelope.getMinX();
		if (buildingEnvelope.getMaxX() > nodeEnvelope.getMaxX())
			expansionValue += buildingEnvelope.getMaxX() - nodeEnvelope.getMaxX();

		return expansionValue;
	}
	
}