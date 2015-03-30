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
import digest.Digest;

import graph.Graph;
import merge.MergingPaper;

public class TestDigestCommercialInsurance {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		testCommercialInsurance("MC_claim_initiation.epml", "PC_claim_initiation.epml");
		testCommercialInsurance("MC_claim_lodgement.epml", "PC_claim_lodgement.epml");
		testCommercialInsurance("MC_invoice_received.epml", "PC_invoice_received.epml");
	}

	public static void testCommercialInsurance(String m1, String m2) {
		String model_prefix = "models/commercial insurance/";
		String result_prefix = "models/digest/";

		Graph g1 = EPCModelParser.readModels(model_prefix + m1, false).get(0);
		g1.removeEmptyNodes();
		g1.reorganizeIDs();
		
		Graph g2 = EPCModelParser.readModels(model_prefix + m2, false).get(0);
		g2.removeEmptyNodes();
		g2.reorganizeIDs();

		g1.addLabelsToUnNamedEdges();
		g2.addLabelsToUnNamedEdges();
		
		Graph merged = new MergingPaper().mergeModels(g1, g2);

		Graph digested = Digest.digest(merged, 2);
		
		EPCModelParser.writeModel(result_prefix +""+g1.name+"_"+g2.name+"_digest.epml", digested);	
	}
}
