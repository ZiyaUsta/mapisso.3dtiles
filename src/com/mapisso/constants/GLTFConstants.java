package com.mapisso.constants;

public class GLTFConstants {

	/*
	 * 5120 (BYTE) 1 5121 (UNSIGNED_BYTE) 1 5122 (SHORT) 2 5123 (UNSIGNED_SHORT) 2
	 * 5125 (UNSIGNED_INT) 4 5126 (FLOAT) 4 size of element as a byte size.
	 */
	public static Integer[] COMPONENT_TYPE = new Integer[] { 5120, 5121, 5122, 5123, 5125, 5126 };

	/*
	 * "SCALAR" 1 "VEC2" 2 "VEC3" 3 "VEC4" 4 "MAT2" 4 "MAT3" 9 "MAT4" 16 number of
	 * components
	 */
	public static String[] NUMBER_OF_COMPONENTS = new String[] { "SCALAR", "VEC2", "VEC3", "VEC4", "MAT2", "MAT3",
			"MAT4" };

	/*
	 * 34962 Array buffer 34963 Element Array Buffer
	 */
	public static Integer[] TARGET_OF_BUFFER = new Integer[] { 34962, 34963 };

	/*
	 * GLTF Version
	 */
	public static String GLTF_VERSION = "2.0";

	/*
	 * GLTF File Generator Name
	 */
	public static String GLTF_GENERATOR = "Halo Solar Platform";
	
	/*
	 * GLB CONSTANTS
	 */
	public static final int GLTF_MAGIC = 0x46546C67;
	
	public static final int JSON_TYPE = 0x4E4F534A;
	
	public static final int BIN_TYPE = 0x004E4942;
	
	public static final int GLTF_VERSION_GLB = 2;

}
