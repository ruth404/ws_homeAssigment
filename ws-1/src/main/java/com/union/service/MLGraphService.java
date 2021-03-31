package main.java.com.union.service;

import java.io.StringReader;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.nio.ImportException;
import org.jgrapht.nio.dot.DOTImporter;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;
import java.util.*;
import org.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class MLGraphService {
    	private static Logger logger= LoggerFactory.getLogger(MLGraphService.class);
		private static Set<String> potentialML = new HashSet<String>();

		// CFG input params
		private static Graph<String, DefaultEdge> dGraph;
		private String e1;
		private String e2;
		private String h;

		
		public MLGraphService(String input) throws Exception {
			inputParser(input);
		}
		
		/**
		 * This method is used to parse the input information
		 * and to set all the class params
		 * @param input an input string in json format
		 * @return void.
		 */
		private void inputParser(String input) throws Exception {
			JSONObject obj = new JSONObject(input);
			String dgraphInputStr = "";
			
			e1 = obj.getString("e1");
			e2 = obj.getString("e2");
			h = obj.getString("h");
			dgraphInputStr = obj.getString("graph");
			
			logger.info("Input params: e1: " + e1 + ", e2: " + e2 + ", h: " + h + ", graph: " + dgraphInputStr);

			// Create new directed graph object
			dGraph = new SimpleDirectedGraph<>(DefaultEdge.class);
			DOTImporter<String, DefaultEdge> dotImporter = new DOTImporter<>();
			dotImporter.setVertexFactory(label -> label);
			dotImporter.importGraph(dGraph, new StringReader(dgraphInputStr));
			
			// input validation 
			if (!dGraph.containsVertex(e1)) { 
				logger.error("Invalid Input Exceptione: e1 is not in the graph");
				throw new Exception("Invalid Input Exceptione: e1 is not in the graph");
			}
			else if (!dGraph.containsVertex(e2)) {
				logger.error("Invalid Input Exceptione: e2 is not in the graph");
				throw new Exception("Invalid Input Exceptione: e2 is not in the graph");
			}
			else if(!dGraph.containsVertex(h)) {
				logger.error("Invalid Input Exceptione: h is not in the graph");
				throw new Exception("Invalid Input Exceptione: h is not in the graph");				
			}
		}


		/**
		 * This method is used to calculate the intersection of all
		 * possible paths between two vertexes in the graph.
		 * This is a recursive function based on DFS algorithm
		 * @param e1	- an entry node in the graph
		 * @param h		- a node in the graph. The path end point.
		 * @param path	- a set of all the nodes in the current path
		 * @return void.
		 */
		public static void allPathIntersectionDFS(String e1, String h, Set<String> path) throws Exception {
			Iterator<DefaultEdge> dGraphIt = dGraph.edgesOf(e1).iterator();
			String targetEdge;
			
			// adding current entry node to the path set
			path.add(e1);

			while(dGraphIt.hasNext()){
				targetEdge = dGraph.getEdgeTarget(dGraphIt.next());
				
		    	 if(h.equals(targetEdge)) {		 	// path complited 
		    		 potentialML.retainAll(path);	// intersect the current path with the paths that already calculated
		    	 }
		    	 else if(!path.contains(targetEdge)) { // avoiding circles 
		    		 allPathIntersectionDFS(targetEdge, h, path);
		    	 }
		     }
		     path.remove(e1);
		}
	    
	    public Set<String> getGraphMustLead() throws Exception {
	    	Set<String> path = new HashSet<String>();	// new empty path set
	    	
			// Initializes graphs potential mast lead nodes (with all the graph nodes)
			potentialML.addAll(dGraph.vertexSet());
	    	
			allPathIntersectionDFS(e1, h, path);
			
			if(potentialML.size() == dGraph.vertexSet().size()) { // there is no path between e1 and h
				logger.info("There is no path between e1 and h in the givven graph");
				potentialML.clear();
			}
			
			logger.info("Must leads of h: " + potentialML);
			return potentialML;
	    }
	    
			 
	}



