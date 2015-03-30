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

import java.util.Collection;
import java.util.Map;

import de.bpt.hpi.graph.Graph;

public interface Helper {
	Graph getGraph();	
	public void serializeToFile(Collection<Integer> vertices, String code);
	String serialize(Collection<Integer> vertices, String hashcode, int index);
	String serializeInContext(Collection<Integer> vertices, Collection<Integer> context, String hashcode, int inde);
	boolean isGateway(Integer id);
	String getGraphName();
	// max combinations in one region
	public int getMaxCombinationsInRegion();
	public void setMaxCombinationsInRegion(int maxCombinationsInRegion);
	// total number of combinations
	public void addTotalNumberOfCombinations(int nr);
	public int getTotalNumberOfCombinations();
	// number of regions
	public void addTotalNrOfRegions();
	public int getTotalNrOfRegions();
	void serializeToFile(String string,
			Map<Integer, Collection<Integer>> idOriginalVerticesMap,
			String code);
}
