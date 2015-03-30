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

package test;

import common.EPCModelParser;

import graph.Graph;
import merge.MergeModels;
import merge.MergingPaper;

public class TestMergeReductionRules {

	static String model_prefix = "models/test reduction rules/";
//	static String result_prefix = "models/test reduction rules/";
//	static String model_prefix = "models/allEPCs/";
	static String result_prefix = "models/test reduction rules/";

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		testReduction();
//		testModels();
		testCycle();
	}

	public static void testModels() {
		String m1 = "filter_left.epml";
		String m2 = "filter_right.epml";
		Graph.cleanGraphIDs();

		Graph g1 = EPCModelParser.readModels(model_prefix + m1, false).get(0);
		g1.removeEmptyNodes();
		g1.reorganizeIDs();

		
		Graph g2 = EPCModelParser.readModels(model_prefix + m2, false).get(0);
		g2.removeEmptyNodes();
		g2.reorganizeIDs();

		g1.addLabelsToUnNamedEdges();
		g2.addLabelsToUnNamedEdges();

//		Graph merged = new MergeModels().mergeModels(g1, g2);
		Graph merged = new MergingPaper().mergeModels(g1, g2);
		System.out.println(m1+"\t"+g1.getVertices().size()+"\t"+m2+"\t"+g2.getVertices().size()+"\t"+
				merged.getVertices().size()+"\t"+merged.getNrOfConfigGWs());

		EPCModelParser.writeModel(result_prefix +""+g1.name+"_"+g2.name+"_merged.epml", merged);	

		
	}
	
	public static void testCycle() {
		model_prefix = "models/test reduction rules/cycle/";
		result_prefix = "models/test reduction rules/cycle/";

		String m1 = "R2.epml";
		String m2 = "R1.epml";
		Graph.cleanGraphIDs();

		Graph g1 = EPCModelParser.readModels(model_prefix + m1, false).get(0);
		g1.removeEmptyNodes();
		g1.reorganizeIDs();

		
		Graph g2 = EPCModelParser.readModels(model_prefix + m2, false).get(0);
		g2.removeEmptyNodes();
		g2.reorganizeIDs();

		g1.addLabelsToUnNamedEdges();
		g2.addLabelsToUnNamedEdges();

//		Graph merged = new MergeModels().mergeModels(g1, g2);
		Graph merged = new MergingPaper().mergeModels(g1, g2);
		System.out.println(m1+"\t"+g1.getVertices().size()+"\t"+m2+"\t"+g2.getVertices().size()+"\t"+
				merged.getVertices().size()+"\t"+merged.getNrOfConfigGWs());

		EPCModelParser.writeModel(result_prefix +""+g1.name+"_"+g2.name+"_merged.epml", merged);	
	}

	
	public static void testReduction() {
		int i = 5;
//		for (int i = 1; i <= 4; i++) {
			testReduction("rule"+i+"_left.epml", "rule"+i+"_right.epml");
//		}
	}
	
	public static void testReduction(String m1, String m2) {
		
		Graph.cleanGraphIDs();

		Graph g1 = EPCModelParser.readModels(model_prefix + m1, false).get(0);
		g1.removeEmptyNodes();
		g1.reorganizeIDs();
//		EPCModelParser.writeModel(result_prefix +""+g1.name+"_changed.epml", g1);	

		
		Graph g2 = EPCModelParser.readModels(model_prefix + m2, false).get(0);
		g2.removeEmptyNodes();
		g2.reorganizeIDs();
//		EPCModelParser.writeModel(result_prefix +""+g2.name+"_changed.epml", g2);	

		g1.addLabelsToUnNamedEdges();
		g2.addLabelsToUnNamedEdges();

		Graph merged = new MergingPaper().mergeModels(g1, g2);
//		Graph merged = new MergeModels().mergeModels(g1, g2);
		System.out.println(m1+"\t"+g1.getVertices().size()+"\t"+m2+"\t"+g2.getVertices().size()+"\t"+
				merged.getVertices().size()+"\t"+merged.getNrOfConfigGWs());

		EPCModelParser.writeModel(result_prefix +""+g1.name+"_"+g2.name+"_merged.epml", merged);
	}
	
}
