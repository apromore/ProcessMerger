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


import common.Settings;

import graph.Graph;
import graph.Vertex;
import graph.Vertex.Type;

public class NodeSimilarity {

	public static double findNodeSimilarity(Vertex n, Vertex m) {
		// functions or events -
		// compare the labels of these nodes 
		// tokenize, stem and find the similarity score
		if ((n.getType().equals(Type.function) && m.getType().equals(Type.function)
		 || n.getType().equals(Type.event) && m.getType().equals(Type.event)) 
		 && AssingmentProblem.canMap(n, m)) {
			return LabelEditDistance.edTokensWithStemming(m.getLabel(), 
					n.getLabel(), Settings.STRING_DELIMETER,
					Settings.getEnglishStemmer(), true);
			
		} 
		// gateways
		else if (n.getType().equals(Type.gateway) && m.getType().equals(Type.gateway)) {
			// splits can not be merged with joins
			if (Graph.isSplit(n) && Graph.isJoin(m)
					|| Graph.isSplit(m) && Graph.isJoin(n)) {
				return 0;
			}
			double sim =  SemanticSimilarity.getSemanticSimilarity(n, m);
//			System.out.println(">Similarity : "+sim);
			return sim;
		}
		return 0;
	}
}
