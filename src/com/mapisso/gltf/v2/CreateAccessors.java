package com.mapisso.gltf.v2;

import java.util.ArrayList;
import java.util.List;

import com.mapisso.constants.GLTFConstants;
import com.mapisso.model.gltf.v2.Accessor;
import com.mapisso.model.obj.Obj;
import com.mapisso.model.obj.Triangle;

public class CreateAccessors {
	
	public List<Accessor> create(List<Obj> objList) {
		
		List<Accessor> accessorList = new ArrayList<Accessor>();

		Integer coordinateSize = 0;

		Integer positionByteOfset = 0;
		Integer indicesByteOfset = 0;
		Integer bacthidsByteOfset = 0;
		
		int k = 0;

		for (int i = 0; i < objList.size(); i++) {

			Accessor accessorPositions = new Accessor();
			Accessor accessorNormals = new Accessor();
			Accessor accessorIndices = new Accessor();
			Accessor accessorBatchIds = new Accessor();
			
			Obj tempObj = objList.get(i);
			
			List<Triangle> triangleList = tempObj.getTriangles();
			
			String namePositions = tempObj.getO() + "-Mesh_positions";
			
			Number[] minCoordArray = tempObj.getMinCoord();
			Number[] minNumber = new Number[] { minCoordArray[0], minCoordArray[1], minCoordArray[2] };
			Number[] maxCoordArray = tempObj.getMaxCoord();
			Number[] maxNumber = new Number[] { maxCoordArray[0], maxCoordArray[1], maxCoordArray[2] };
			
			coordinateSize = coordinateSize + tempObj.getV().size() * 3;
			
			accessorPositions.setName(namePositions);
			accessorPositions.setComponentType(GLTFConstants.COMPONENT_TYPE[5]);
			accessorPositions.setCount(triangleList.size() * 3);
			accessorPositions.setMin(minNumber);
			accessorPositions.setMax(maxNumber);
			accessorPositions.setType(GLTFConstants.NUMBER_OF_COMPONENTS[2]);
			accessorPositions.setBufferView(k);
			accessorPositions.setByteOffset(positionByteOfset);
			
			/*if (i != 0) {
				positionByteOfset = positionByteOfset + coordinateSizeList.get(i - 1) * 4;
				accessorPositions.setByteOffset(positionByteOfset);
			} else {
				accessorPositions.setByteOffset(0);
			}*/
			
			String nameNormal = tempObj.getO() + "-Mesh_normals";
			Number[] minNormalNumber = tempObj.getMinNormal();
			Number[] maxNormalNumber = tempObj.getMaxNormal();
			
			accessorNormals.setName(nameNormal);
			accessorNormals.setComponentType(GLTFConstants.COMPONENT_TYPE[5]);
			accessorNormals.setCount(triangleList.size() * 3);
			accessorNormals.setMin(new Number[] {minNormalNumber[0], minNormalNumber[1], minNormalNumber[2]});
			accessorNormals.setMax(new Number[] {maxNormalNumber[0], maxNormalNumber[1], maxNormalNumber[2]});
			accessorNormals.setType(GLTFConstants.NUMBER_OF_COMPONENTS[2]);
			accessorNormals.setBufferView(k + 1);
			accessorNormals.setByteOffset(positionByteOfset);
			
			Integer indicesSize = triangleList.size() * 3;
			
			/*if (i != 0) {
				batchIdsByteOfset = batchIdsByteOfset + (triangleSizeList.get(i - 1) * 2);
				accessorBatchIds.setByteOffset((coordinateSizeList.get(i - 1) * 4 + indicesSize));
			} else {

				accessorBatchIds.setByteOffset(0);
			}*/

			String nameIndices = tempObj.getO() + "-Mesh_indices";
			
			Number[] minNumberInd = new Number[] { tempObj.getMinIndice() };
			Number[] maxNumberInd = new Number[] { indicesSize -1 };
			
			accessorIndices.setName(nameIndices);
			accessorIndices.setComponentType(GLTFConstants.COMPONENT_TYPE[3]);
			accessorIndices.setCount(indicesSize);
			accessorIndices.setMin(minNumberInd);
			accessorIndices.setMax(maxNumberInd);
			accessorIndices.setType(GLTFConstants.NUMBER_OF_COMPONENTS[0]);
			accessorIndices.setBufferView(k + 3);
			accessorIndices.setByteOffset(indicesByteOfset);
			
			/*if (i != 0) {
				indicesByteOfset = indicesByteOfset + (triangleSizeList.get(i - 1) * 2);
				accessorIndices.setByteOffset(indicesByteOfset);
			} else {
				accessorIndices.setByteOffset(0);
			}*/
			
			String nameBatchids = tempObj.getO() + "-Mesh_batchids";
			Integer batchIdsSize = triangleList.size() * 3;
			
			Number[] minBatchId = new Number[] {i};
			Number[] maxBatchId = new Number[] {i};
			
			accessorBatchIds.setName(nameBatchids);
			accessorBatchIds.setComponentType(GLTFConstants.COMPONENT_TYPE[3]);
			accessorBatchIds.setCount(batchIdsSize);
			accessorBatchIds.setMin(minBatchId);
			accessorBatchIds.setMax(maxBatchId);
			accessorBatchIds.setType(GLTFConstants.NUMBER_OF_COMPONENTS[0]);
			accessorBatchIds.setBufferView(k + 2);
			accessorBatchIds.setByteOffset(0);
			accessorBatchIds.setByteOffset(bacthidsByteOfset); 
			
			accessorList.add(accessorPositions);
			accessorList.add(accessorNormals);
			accessorList.add(accessorBatchIds);
			accessorList.add(accessorIndices);
			
			k = k + 4;
		}
		
		return accessorList;

	}
	
}
