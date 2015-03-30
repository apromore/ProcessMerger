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

public class Arc{
	
	private String id;
	private Node source;
	private Node target;
	
	public Arc(){
	}
	
	public Arc(String id, Node source, Node target){
		this.id = id;
		this.source = source;
		this.target = target;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public Node getSource() {
		return source;
	}
	public void setSource(Node source) {
		this.source = source;
	}
	public Node getTarget() {
		return target;
	}
	
	public void setTarget(Node target) {
		this.target = target;
	}
	
	public String toString(){
		return "Arc(("+id+") , " + source.toString() + "," + target.toString() + ")";
	}
	
	public boolean equals(Object arg0) {
		if (arg0 instanceof Arc){
			return id.equals(((Arc)arg0).getId());
		}else{
			return false;
		}
	}
	
	public int hashCode() {
		return Integer.parseInt(id);
	}
	
}
