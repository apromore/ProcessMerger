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
import common.Settings;

import graph.Graph;
import merge.MergeModels;

public class MergeProcessModels {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length == 2) {
			testSAWA(args[0], args[1]);
		} else if (args.length == 4 && "-mt".equals(args[0])) {
			double newMergeThreshold = Double.parseDouble(args[1]);
			if (newMergeThreshold >=0 && newMergeThreshold <= 1) {
				Settings.MERGE_THRESHOLD = Double.parseDouble(args[1]);
			} else {
				System.out.println("Merge threshold not in valid range, leaving it to default 0.5 value");
			}
			testSAWA(args[2], args[3]);
		} else {
			System.out.println("USAGE: \n" +
					"ProcessMerger [-mt mt_value] model1 model2\n" +
					"-mt - custom merge threshold - if similarity for nodes is > threshold, then 2 nodes are merged\n" +
					"If -mt and -ct are not specified, the default value of 0.5 is used for both.");
		}
	}

	public static void testSAWA(String m1, String m2) {

		Graph g1 = EPCModelParser.readModels(m1, false).get(0);
		g1.reorganizeIDs();
		
		Graph g2 = EPCModelParser.readModels(m2, false).get(0);
		g2.reorganizeIDs();
		
		g1.addLabelsToUnNamedEdges();
		g2.addLabelsToUnNamedEdges();


		Graph merged = new MergeModels().mergeModels(g1, g2);
		
		EPCModelParser.writeModel(m1.substring(0, m1.length()-5) + "_"+m2.substring(0, m2.length()-5) + ".epml", merged);	
	}

}
