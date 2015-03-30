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

package nl.tue.tm.is.epc;

import java.util.HashSet;
import java.util.Set;

import de.bpt.hpi.graph.Edge;
import de.bpt.hpi.graph.Graph;
import de.bpt.hpi.ogdf.rpst.ExpRPST;
import de.bpt.hpi.ogdf.spqr.SPQRNodeType;
import de.bpt.hpi.ogdf.spqr.TreeNode;

public class RPSDag {
	public int nrBonds = 0;
	
	public void addProcessModel(Helper helper) throws Exception {
		Graph graph = helper.getGraph();
		ExpRPST tree = normalizeGraph(graph);

		traverse(tree);
	}

	private void traverse(ExpRPST tree) {
		Set<Integer> vertices = new HashSet<Integer>(tree.getRootNode()
				.getOriginalVertices());
		Set<Edge> edges = new HashSet<Edge>(tree.getRootNode()
				.getOriginalEdges());
		TreeNode root = tree.getRootNode();
		Graph graphp = tree.getExpandedGraph().clone();

		traverse(tree, graphp, root, edges, vertices);
	}

	private void traverse(ExpRPST tree, Graph graph, TreeNode curr,
			Set<Edge> edges, Set<Integer> vertices) {

		if (curr.getNodeType() == SPQRNodeType.Q)
			return;
		Set<Edge> ledges = new HashSet<Edge>(curr.getOriginalEdges());
		Set<Integer> lvertices = new HashSet<Integer>(
				curr.getOriginalVertices());
		for (TreeNode child : curr.getChildNodes()) {
			if (child.getNodeType() == SPQRNodeType.Q) {
				continue;
			}
			Set<Edge> cedges = new HashSet<Edge>(child.getOriginalEdges());
			Set<Integer> cvertices = new HashSet<Integer>(
					child.getOriginalVertices());

			traverse(tree, graph, child, cedges, cvertices);
			switch (child.getNodeType()) {
			case S:
				break;
			case P:
				nrBonds++; 
				break;
			case Q:
				break;
			case R:
				break;
			}
			ledges.removeAll(child.getOriginalEdges());
			lvertices.removeAll(child.getOriginalVertices());
			ledges.addAll(cedges);
			lvertices.addAll(cvertices);
		}

		edges.clear();
		edges.addAll(ledges);
		vertices.clear();
		vertices.addAll(lvertices);
	}

	private ExpRPST normalizeGraph(Graph graph) {
		Set<Integer> srcs = graph.getSourceNodes();
		Set<Integer> tgts = graph.getSinkNodes();

		srcs.retainAll(tgts);
		// remove nodes that have no input and output edges
		for (Integer v : srcs) {
			graph.removeVertex(v);
		}

		srcs = graph.getSourceNodes();
		tgts = graph.getSinkNodes();

		Integer entry = graph.addVertex("_entry_");
		Integer exit = graph.addVertex("_exit_");

		// connect all source nodes with one entry node
		for (Integer tgt : srcs)
			graph.addEdge(entry, tgt);

		// connect all sink nodes with one exit node
		for (Integer src : tgts)
			graph.addEdge(src, exit);

		return new ExpRPST(graph, entry, exit);
	}
}
