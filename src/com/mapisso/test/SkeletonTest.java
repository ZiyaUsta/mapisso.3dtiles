package com.mapisso.test;

import java.io.File;
import java.io.IOException;

import javax.vecmath.Point3d;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.twak.camp.Corner;
import org.twak.camp.Edge;
import org.twak.camp.Machine;
import org.twak.camp.OffsetSkeleton;
import org.twak.camp.Output.Face;
import org.twak.camp.Skeleton;
import org.twak.utils.collections.Loop;
import org.twak.utils.collections.LoopL;
import org.twak.camp.debug.CampSkeleton;
import com.mapisso.roof.Vertex;
import com.mapisso.shape.ReadShapeFile;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;


public class SkeletonTest {
	
	
	
	public static void main(String[] args) throws IOException {
	
	/*	
			    
		Corner c1 = new Corner (3703356.599156659, 3089911.1050016643, 4159531.5520224804);
		Corner c2 = new Corner (3703355.960648143, 3089918.57537716, 4159526.6044662003);
		Corner c3 = new Corner (3703349.2061727387, 3089913.487896065, 4159536.3318608357);
		Corner c4 = new Corner (3703348.4578283667, 3089920.976261688, 4159531.4681656314);
		*/
				
				Machine speed = new Machine();
		
				
				// Bu kısım döngüye alınacak hard coded yazdım test için 
				
				// Koordinatlardan corner oluşturma
				Corner c1 = new Corner (3703356.599156659, 3089911.1050016643, 0);
				Corner c2 = new Corner (3703355.960648143, 3089918.57537716, 0);
				Corner c3 = new Corner (3703349.2061727387, 3089913.487896065,0);
				Corner c4 = new Corner (3703348.4578283667, 3089920.976261688, 0);

				// Cornerlardan Edge
				Edge e1 = new Edge(c2,c1);
				Edge e2 = new Edge(c3,c2);
				Edge e3 = new Edge(c4,c3);
				Edge e4 = new Edge(c1,c4);

				// edgelere speed setliyoruz.
				e1.machine = speed;
				e2.machine = speed;
				e3.machine = speed;
				e4.machine = speed;

				
				
				// Herbir poligon için bir loop oluşturup edgeleri ona ekliyoruz.
				Loop<Edge> loop1 = new Loop<Edge>();
				loop1.append(e1);
				loop1.append(e2);
				loop1.append(e3);
				loop1.append(e4);

				Skeleton skel = new Skeleton (loop1.singleton(), true);
				skel.skeleton();
			
				// Önemli not input poligonların koordinat dizileri saat yönünün tersine olmalı yoksa düzgün çalışmıyor !!!
				
				// Oluşan herbir yüzeyin yani poligonların koordinatlarını döngü ile ekrana yazdırıyorum.
				
				for ( Face face : skel.output.faces.values() ) {
					
					System.out.println( "face:" );
					for ( Loop<Point3d> lp3 : face.points )
						for ( Point3d pt : lp3 )
							System.out.println( pt );
				}
				
				// shapefiledaki tek bir poligon için çalışsın diye index i bir attırdım while döngüsü durdun diye
				
	
				
				// Bu noktadan sonra yapılması gereken bu koordinat dizilerinden earcut4j ile indisler üretip bunları obj ve gltf modellerimize eklemek.
				
				/* kullandığım kütüphane için pom dosyasına :
				
				<dependency>
	    		<groupId>com.github.twak</groupId>
	    		<artifactId>campskeleton</artifactId>
	    		<version>master-SNAPSHOT</version>
				</dependency>
				
				bölümünü eklersin
*/
				
			    }
			
		}
		
		
	
	

