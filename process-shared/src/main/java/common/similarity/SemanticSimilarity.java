/*
 * Copyright © 2009-2014 The Apromore Initiative.
 *
 * This file is part of “Apromore”.
 *
 * “Apromore” is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * “Apromore” is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.
 * If not, see <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */

package common.similarity;

import java.util.LinkedList;

import common.Settings;
import common.VertexPair;

import graph.Vertex;

public class SemanticSimilarity {

	public static double getSemanticSimilarity(Vertex v1, Vertex v2) {
		
//		System.out.println("Semantic similarity for ("+ v1.getID()+") (" + v2.getID()+")");
		LinkedList<Vertex> v1NonGWParents = v1.getAllNonGWParents();
		LinkedList<Vertex> v2NonGWParents = v2.getAllNonGWParents();
		LinkedList<Vertex> v1NonGWChildren = v1.getAllNonGWChildren();
		LinkedList<Vertex> v2NonGWChildren = v2.getAllNonGWChildren();
//		System.out.println("v1NonGWParents "+ v1NonGWParents.size() +" v2NonGWParents " + v2NonGWParents.size() 
//				+ "v1NonGWChildren "+ v1NonGWChildren.size() +" v2NonGWChildren " + v2NonGWChildren.size());
		
		
		LinkedList<VertexPair> parentMappings = AssingmentProblem.getMappingsVetrex(v1NonGWParents, v2NonGWParents, Settings.MERGE_THRESHOLD, Settings.getEnglishStemmer(), 0);
		LinkedList<VertexPair> childMappings = AssingmentProblem.getMappingsVetrex(v1NonGWChildren, v2NonGWChildren, Settings.MERGE_THRESHOLD, Settings.getEnglishStemmer(), 0);

//		for (VertexPair c : parentMappings) {
//			System.out.println(">>>p "+c.getLeft().getLabel()+" ("+c.getLeft().getID()+") "+ " <> "+ c.getRight().getLabel()+" ("+c.getRight().getID()+") ");
//		}
//		for (VertexPair c : childMappings) {
//			System.out.println(">>>c "+c.getLeft().getLabel()+" ("+c.getLeft().getID()+") "+ " <> "+ c.getRight().getLabel()+" ("+c.getRight().getID()+") ");
//		}
		return (double)(parentMappings.size() + childMappings.size()) 
				/ (double)(Math.max(v1NonGWParents.size(), v2NonGWParents.size()) + Math.max(v1NonGWChildren.size(), v2NonGWChildren.size()));
	}
}
