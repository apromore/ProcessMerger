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

package merge;

import common.IdGeneratorHelper;
import common.algos.GraphEditDistanceGreedy;
import common.algos.TwoVertices;
import common.similarity.AssingmentProblem;
import graph.Edge;
import graph.Graph;
import graph.Vertex;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import planarGraphMathing.PlanarGraphMathing.MappingRegions;
import common.VertexPair;

public class MergeModels {

    public static String find(String name, Graph g1) {
        for(Vertex v : g1.getVertices()) {
            if(name.equals(v.getLabel())) {
                return v.getID();
            }
        }
        return null;
    }

    public static boolean exist(String name, Graph g1) {
        for(Edge v : g1.getEdges()) {
            if(name.equals(v.getFromVertex())) {
                return true;
            }
        }
        return false;
    }

    public static Graph mergeModels(Graph g1, Graph g2, IdGeneratorHelper idGenerator, boolean removeEnt, String algortithm, double... param) {

        HashMap<String, String> objectresourceIDMap = new HashMap<String, String>();

        HashSet<String> labelsg1 = new HashSet<String>();
        HashSet<String> labelsg2 = new HashSet<String>();
        HashSet<String> labelsg1g2 = new HashSet<String>();
        if(!("###merged###").equals(g1.getGraphLabel())) {
            labelsg1.add(g1.getGraphLabel());
            labelsg1g2.add(g1.getGraphLabel());
        }else {
            for(Edge edge : g1.getEdges()) {
                labelsg1.addAll(edge.getLabels());
                labelsg1g2.addAll(edge.getLabels());
            }
        }
        if(!("###merged###").equals(g2.getGraphLabel())) {
            labelsg2.add(g2.getGraphLabel());
            labelsg1g2.add(g2.getGraphLabel());
        }else {
            for(Edge edge : g2.getEdges()) {
                labelsg2.addAll(edge.getLabels());
                labelsg1g2.addAll(edge.getLabels());
            }
        }

        Graph merged = new Graph();
        merged.setIdGenerator(idGenerator);
        long startTime = System.currentTimeMillis();

        merged.addVertices(g1.getVertices());
        merged.addEdges(g1.getEdges());
        merged.addVertices(g2.getVertices());
        merged.addEdges(g2.getEdges());

        LinkedList<VertexPair> mapping = new LinkedList<VertexPair>();

        if (algortithm.equals("Greedy")) {
            GraphEditDistanceGreedy gedepc = new GraphEditDistanceGreedy();
            Object weights[] = {"ledcutoff", param[0],
                    "cedcutoff", param[1],
                    "vweight", param[2],
                    "sweight", param[3],
                    "eweight", param[4]};

            gedepc.setWeight(weights);

            for (TwoVertices pair : gedepc.compute(g1, g2)) {
                Vertex v1 = g1.getVertexMap().get(pair.v1);
                Vertex v2 = g2.getVertexMap().get(pair.v2);
                if (v1.getType().equals(v2.getType())) {
                    mapping.add(new VertexPair(v1, v2, pair.weight));
                }
            }
        } else if (algortithm.equals("Hungarian")) {
            mapping = AssingmentProblem.getMappingsVetrexUsingNodeMapping(g1, g2, param[0], param[1]);
        }

        // clean mappings from mappings that conflict
        // TODO uncomment

        if (removeEnt) {
            g1.fillDominanceRelations();
            g2.fillDominanceRelations();
            removeNonDominanceMappings2(mapping);
        }

        MappingRegions mappingRegions = findMaximumCommonRegions(g1, g2, mapping);

        for (LinkedList<VertexPair> region : mappingRegions.getRegions()) {
            for (VertexPair vp : region) {
                LinkedList<Vertex> nodesToProcess = new LinkedList<Vertex>();
                for (Vertex c : vp.getRight().getChildren()) {
                    // the child is also part of the mapping
                    // remove the edge from the merged modelass
                    if (containsVertex(region, c)) {
                        nodesToProcess.add(c);
                    }
                }
                for (Vertex c : nodesToProcess) {
                    HashSet<String> labels = merged.removeEdge(vp.getRight().getID(), c.getID());

                    vp.getRight().removeChild(c.getID());
                    c.removeParent(vp.getRight().getID());

                    Vertex cLeft = getMappingPair(mapping, c);
                    Edge e = merged.containsEdge(vp.getLeft().getID(), cLeft.getID());

                    if (e != null) {
                        if(!(labels == null || labels.size() == 0)) {
                            if(e.getLabels().size() == 0) {
                                e.addLabels(labelsg1);
                            }
                            e.addLabels(labels);
                        }
                    }

                }
            }
            // add annotations for the labels
            for (VertexPair vp : region) {
                Vertex mappingRight = vp.getRight();
                vp.getLeft().addAnnotations(mappingRight.getAnnotationMap());
            }
        }

        LinkedList<Vertex> toRemove = new LinkedList<Vertex>();
        // check if some vertices must be removed
        for (Vertex v : merged.getVertices()) {
            if (v.getParents().size() == 0 && v.getChildren().size() == 0) {
                toRemove.add(v);
            }
        }

        for (Vertex v : toRemove) {
            merged.removeVertex(v.getID());
        }

        for (LinkedList<VertexPair> region : mappingRegions.getRegions()) {

            LinkedList<VertexPair> sources = findSources(region);
            LinkedList<VertexPair> sinks = findSinks(region);

            // process sources
            for (VertexPair source : sources) {
                Vertex g1Source = source.getLeft();
                Vertex g2Source = source.getRight();
                LinkedList<Vertex> g1SourcePrev = new LinkedList<Vertex>(g1Source.getParents());//removeFromList(g1Source.getParents(), mapping);
                LinkedList<Vertex> g2SourcePrev = new LinkedList<Vertex>(g2Source.getParents());//removeFromList(g2Source.getParents(), mapping);

                if (!g1Source.getType().equals(Vertex.Type.gateway)) {

                    Vertex newSource = new Vertex(Vertex.GWType.xor, idGenerator.getNextId());

                    newSource.setConfigurable(true);
                    merged.addVertex(newSource);

                    merged.connectVertices(newSource, g1Source);

                    for (Vertex v : g1SourcePrev) {
                        g1Source.removeParent(v.getID());
                        v.removeChild(g1Source.getID());
                        HashSet<String> labels = merged.removeEdge(v.getID(), g1Source.getID());
                        merged.connectVertices(v, newSource, labels);
                    }

                    for (Vertex v : g2SourcePrev) {
                        g2Source.removeParent(v.getID());
                        v.removeChild(g2Source.getID());
                        HashSet<String> labels = merged.removeEdge(v.getID(), g2Source.getID());
                        merged.connectVertices(v, newSource, labels);
                    }
                }
                // this is gateway
                else {
                    for (Vertex v : g2SourcePrev) {
                        v.removeChild(g2Source.getID());
                        if (!containsVertex(mapping, v)) {
                            HashSet<String> labels = merged.removeEdge(v.getID(), g2Source.getID());
                            merged.connectVertices(v, g1Source, labels);
                        }
                    }
                }
            }

            // process sinks
            for (VertexPair sink : sinks) {

                Vertex g1Sink = sink.getLeft();
                Vertex g2Sink = sink.getRight();

                LinkedList<Vertex> g1SourceFoll = new LinkedList<Vertex>(g1Sink.getChildren());
                LinkedList<Vertex> g2SourceFoll = new LinkedList<Vertex>(g2Sink.getChildren());

                if (!g1Sink.getType().equals(Vertex.Type.gateway)) {
                    Vertex newSink = new Vertex(Vertex.GWType.xor, idGenerator.getNextId());
                    newSink.setConfigurable(true);
                    try {
                        merged.getVertexLabel(newSink.getID());
                    } catch (Exception e) { }

                    merged.addVertex(newSink);
                    merged.connectVertices(g1Sink, newSink);

                    for (Vertex v : g1SourceFoll) {
                        g1Sink.removeChild(v.getID());
                        v.removeParent(g1Sink.getID());
                        HashSet<String> labels = merged.removeEdge(g1Sink.getID(), v.getID());
                        merged.connectVertices(newSink, v, labels);
                    }

                    for (Vertex v : g2SourceFoll) {
                        v.removeParent(g2Sink.getID());
                        HashSet<String> labels = merged.removeEdge(g2Sink.getID(), v.getID());
                        merged.connectVertices(newSink, v, labels);
                    }

                } else {
                    for (Vertex v : g2SourceFoll) {
                        v.removeParent(g2Sink.getID());
                        if (!containsVertex(mapping, v)) {
                            HashSet<String> labels = merged.removeEdge(g2Sink.getID(), v.getID());
                            merged.connectVertices(g1Sink, v, labels);
                        }
                    }
                }
            }

        }

        for (VertexPair vp : mapping) {
            for (Vertex v : vp.getLeft().getParents()) {
                // this edge is in mapping
                // save labels from the both graph
                if (containsVertex(mapping, v)) {
                    Edge e = merged.containsEdge(v.getID(), vp.getLeft().getID());
                    if (e != null) {
                        // this is a part of a mapping
                        Vertex v2 = getMappingPair(mapping, v);
                        if (v2 != null) {
                            Edge e2 = g2.containsEdge(v2.getID(), vp.getRight().getID());
                            if (e2 != null) {
                                e.addLabels(e2.getLabels());
                                // the common part should also have the labels of both graph
                            }
                        }
                    }
                }
            }
        }

        // remove mapping
        for (VertexPair vp : mapping) {
            // remove edges
            for (Vertex v : vp.getRight().getParents()) {;
                merged.removeEdge(v.getID(), vp.getRight().getID());
            }
            for (Vertex v : vp.getRight().getChildren()) {
                merged.removeEdge(vp.getRight().getID(), v.getID());
            }

            if (vp.getLeft().getType().equals(Vertex.Type.gateway) &&
                    vp.getLeft().getGWType().equals(vp.getRight().getGWType())
                    && (vp.getLeft().isAddedGW() || vp.getRight().isAddedGW())) {
                vp.getLeft().setConfigurable(true);
            }

            if (vp.getLeft().getType().equals(Vertex.Type.gateway)
                    && (vp.getLeft().isInitialGW() || vp.getRight().isInitialGW())) {

                vp.getLeft().setInitialGW();
            }

            // change gateways
            if (vp.getLeft().getType().equals(Vertex.Type.gateway) &&
                    !vp.getLeft().getGWType().equals(vp.getRight().getGWType())) {
                vp.getLeft().setGWType(Vertex.GWType.or);
                vp.getLeft().setConfigurable(true);

            }
            merged.removeVertex(vp.getRight().getID());
        }

        long mergeTime = System.currentTimeMillis();

        for (Edge e : merged.getEdges()) {
            if(e.getLabels().size() == 0) {
                e.addLabels(labelsg1g2);
            }
        }

        merged.cleanGraph();

        // labels for all edges should be added to the model
        for (Edge e : merged.getEdges()) {
//            if(e.getLabels().size() == labelsg1g2.size()) {
//               e.getLabels().clear();
//            }
            e.addLabelToModel();
        }

        long cleanTime = System.currentTimeMillis();

        merged.mergetime = mergeTime - startTime;
        merged.cleanTime = cleanTime - startTime;

        merged.name = "";
        for (String l : merged.getEdgeLabels()) {
            merged.name += l + ",";
        }
        merged.name = "###merged###"; //merged.name.substring(0, merged.name.length() - 1);
        merged.ID = "###merged###"; //String.valueOf(idGenerator.getNextId());

        return merged;
    }

    private static LinkedList<VertexPair> findSources(LinkedList<VertexPair> mapping){
        LinkedList<VertexPair> sources = new LinkedList<VertexPair>();
        for (VertexPair vp : mapping) {
            boolean added = false;
            for (Vertex v : vp.getLeft().getParents()) {
                // the mapping does not contain
                if (!containsVertex(mapping, v)) {
                    sources.add(vp);
                    added = true;
                    break;
                }
            }
            if (!added) {
                for (Vertex v : vp.getRight().getParents()) {
                    // the mapping does not contain
                    if (!containsVertex(mapping, v)) {
                        sources.add(vp);
                        break;
                    }
                }
            }
        }
        return sources;
    }

    private static LinkedList<VertexPair> findSinks(LinkedList<VertexPair> mapping){
        LinkedList<VertexPair> sinks = new LinkedList<VertexPair>();
        for (VertexPair vp : mapping) {
            boolean added = false;
            for (Vertex v : vp.getLeft().getChildren()) {
                // the mapping does not contain
                if (!containsVertex(mapping, v)) {
                    sinks.add(vp);
                    added = true;
                    break;
                }
            }
            if (!added) {
                for (Vertex v : vp.getRight().getChildren()) {
                    // the mapping does not contain
                    if (!containsVertex(mapping, v)) {
                        sinks.add(vp);
                        break;
                    }
                }
            }
        }
        return sinks;
    }

    @SuppressWarnings("unused")
    private void removeNonDominanceMappings(LinkedList<VertexPair> mapping) {

        LinkedList<VertexPair> removeList = new LinkedList<VertexPair>();
        int i = 0;

        for (VertexPair vp : mapping) {
            i++;
            // the mapping is already in removed list
            if (removeList.contains(vp)) {
                continue;
            }

            for (int j = i; j < mapping.size(); j++) {
                VertexPair vp1 = mapping.get(j);
                if (vp.getLeft().getID().equals(vp1.getLeft().getID()) ||
                        vp.getRight().getID().equals(vp1.getRight().getID())) {
                    continue;
                }
                boolean dominanceInG1 = containsInDownwardsPath(vp.getLeft(), vp1.getLeft());
                boolean dominanceInG2 = containsInDownwardsPath(vp.getRight(), vp1.getRight());

                // dominance rule is broken
                if (dominanceInG1 && !dominanceInG2 || !dominanceInG1 && dominanceInG2) {
                    // remove 2 pairs from the pairs list and start with the new pair
                    removeList.add(vp);
                    removeList.add(vp1);
                    break;
                }
            }
        }

        // remove conflicting mappings
        for (VertexPair vp : removeList) {
            mapping.remove(vp);
        }
    }

    @SuppressWarnings("unused")
    private void removeNonDominanceMappings1(LinkedList<VertexPair> mapping) {

        LinkedList<VertexPair> removeList = new LinkedList<VertexPair>();
        int i = 0;

        for (VertexPair vp : mapping) {
            i++;
            // the mapping is already in removed list
            if (removeList.contains(vp)) {
                continue;
            }

            // TODO - if there exists path where A dominances B, then this dominances B
            // even when this is a cycle
            for (int j = i; j < mapping.size(); j++) {
                VertexPair vp1 = mapping.get(j);
                if (vp.getLeft().getID().equals(vp1.getLeft().getID()) ||
                        vp.getRight().getID().equals(vp1.getRight().getID())) {
                    continue;
                }

                // dominance rule is broken
                if (vp.getLeft().dominance.contains(vp1.getLeft().getID())
                        && vp1.getRight().dominance.contains(vp.getRight().getID())
                        || vp1.getLeft().dominance.contains(vp.getLeft().getID())
                        && vp.getRight().dominance.contains(vp1.getRight().getID())) {
                    // remove 2 pairs from the pairs list and start with the new pair
                    removeList.add(vp);
                    removeList.add(vp1);
                    break;
                }
            }
        }

        // remove conflicting mappings
        for (VertexPair vp : removeList) {
            mapping.remove(vp);
        }
    }

    // implementation of Marlon new dominance mapping relation
    private static void removeNonDominanceMappings2(LinkedList<VertexPair> mapping) {

        LinkedList<VertexPair> removeList = new LinkedList<VertexPair>();
        int i = 0;

        for (VertexPair vp : mapping) {
            i++;
            // the mapping is already in removed list
            if (removeList.contains(vp)) {
                continue;
            }

            for (int j = i; j < mapping.size(); j++) {

                VertexPair vp1 = mapping.get(j);

                // the mapping is already in removed list
                if (removeList.contains(vp1)) {
                    continue;
                }

                // same starting or ending point of models
                if (vp.getLeft().getID().equals(vp1.getLeft().getID()) ||
                        vp.getRight().getID().equals(vp1.getRight().getID())) {
                    continue;
                }

                // dominance rule is broken
                if ((vp.getLeft().dominance.contains(vp1.getLeft().getID())
                        && vp1.getRight().dominance.contains(vp.getRight().getID())
                        && !(vp1.getLeft().dominance.contains(vp.getLeft().getID())
                        || vp.getRight().dominance.contains(vp1.getRight().getID())))
                        || (vp1.getLeft().dominance.contains(vp.getLeft().getID())
                        && vp.getRight().dominance.contains(vp1.getRight().getID())
                        && !(vp.getLeft().dominance.contains(vp1.getLeft().getID())
                        || vp1.getRight().dominance.contains(vp.getRight().getID())))) {
                    // remove 2 pairs from the pairs list and start with the new pair
                    removeList.add(vp);
                    removeList.add(vp1);
                    break;
                }
            }
        }

        // remove conflicting mappings
        for (VertexPair vp : removeList) {
            mapping.remove(vp);
        }
    }

    private boolean containsInDownwardsPath(Vertex v1, Vertex v2) {

        LinkedList<Vertex> toProcess = new LinkedList<Vertex>();
        toProcess.addAll(v1.getChildren());

        while (toProcess.size() > 0) {
            Vertex process = toProcess.removeFirst();
            if (process.getID().equals(v2.getID())) {
                return true;
            }
            toProcess.addAll(process.getChildren());
        }
        return false;
    }

    private static void mergeConnectors(MappingRegions mappingRegions, Graph merged, LinkedList<VertexPair> mapping) {
        for (LinkedList<VertexPair> region : mappingRegions.getRegions()) {
            for (VertexPair vp : region) {
                if (vp.getLeft().getType().equals(Vertex.Type.gateway)) {
                    boolean makeConf = false;
                    LinkedList<Vertex> toProcess = new LinkedList<Vertex>();
                    for (Vertex p : vp.getRight().getParents()) {
                        for (VertexPair zz : region) {
                            if (zz.getLeft().getID().equals(p.getID()) || zz.getRight().getID().equals(p.getID())) {
                                break;
                            }
                        }
                        if (!containsVertex(region, p)) {
                            toProcess.add(p);
                        }
                    }

                    for (Vertex p : toProcess) {
                        makeConf = true;
                        HashSet<String> l = merged.removeEdge(p.getID(), vp.getRight().getID());
                        p.removeChild(vp.getRight().getID());
                        vp.getRight().removeParent(p.getID());
                        merged.connectVertices(p, vp.getLeft(), l);
                    }
                    toProcess = new LinkedList<Vertex>();

                    for (Vertex p : vp.getRight().getChildren()) {
                        for (VertexPair zz : region) {
                            if (zz.getLeft().getID().equals(p.getID()) || zz.getRight().getID().equals(p.getID())) {
                                break;
                            }
                        }
                        if (!containsVertex(region, p)) {
                            toProcess.add(p);
                        }
                    }

                    for (Vertex p : toProcess) {
                        makeConf = true;
                        HashSet<String> l = merged.removeEdge(vp.getRight().getID(), p.getID());
                        p.removeParent(vp.getRight().getID());
                        vp.getRight().removeChild(p.getID());
                        merged.connectVertices(vp.getLeft(), p, l);
                    }
                    if (makeConf) {
                        vp.getLeft().setConfigurable(true);
                    }
                    if (!vp.getLeft().getGWType().equals(vp.getRight().getGWType())) {
                        vp.getLeft().setGWType(Vertex.GWType.or);
                    }
                }
            }
        }
    }


    private static VertexPair findNextVertexToProcess(LinkedList<VertexPair> mapping, LinkedList<VertexPair> visited) {
        for (VertexPair vp : mapping) {
            VertexPair process = containsMapping(visited, vp.getLeft(), vp.getRight());
            if (process == null) {
                return vp;
            }
        }
        return null;
    }

    private static VertexPair containsMapping(LinkedList<VertexPair> mapping, Vertex left, Vertex right) {
        for (VertexPair vp : mapping) {
            if (vp.getLeft().getID().equals(left.getID()) &&
                    vp.getRight().getID().equals(right.getID())) {
                return vp;
            }
        }
        return null;
    }

    @SuppressWarnings("unused")
    private static boolean containsMapping(LinkedList<VertexPair> mapping, VertexPair v) {
        for (VertexPair vp : mapping) {
            if (vp.getLeft().getID().equals(v.getLeft().getID()) &&
                    vp.getRight().getID().equals(v.getRight().getID())) {
                return true;
            }
        }
        return false;
    }

    public static MappingRegions findMaximumCommonRegions(Graph g1, Graph g2, LinkedList<VertexPair> mapping) {
        MappingRegions map = new MappingRegions();
        LinkedList<VertexPair> visited = new LinkedList<VertexPair>();

        while (true) {
            VertexPair c = findNextVertexToProcess(mapping, visited);
            if (c == null) {
                break;
            }
            LinkedList<VertexPair> toVisit = new LinkedList<VertexPair>();
            LinkedList<VertexPair> mapRegion = new LinkedList<VertexPair>();

            toVisit.add(c);
            while (toVisit.size() > 0) {
                c = toVisit.removeFirst();
                mapRegion.add(c);

                visited.add(c);
                for (Vertex pLeft : c.getLeft().getParents()) {
                    for (Vertex pRight : c.getRight().getParents()) {
                        VertexPair pairMap = containsMapping(mapping, pLeft, pRight);
                        VertexPair containsMap = containsMapping(visited, pLeft, pRight);
                        VertexPair containsMap1 = containsMapping(toVisit, pLeft, pRight);
                        if (pairMap != null && containsMap == null && containsMap1 == null) {
                            toVisit.add(pairMap);
                        }
                    }
                }

                for (Vertex pLeft : c.getLeft().getChildren()) {
                    for (Vertex pRight : c.getRight().getChildren()) {
                        VertexPair pairMap = containsMapping(mapping, pLeft, pRight);
                        VertexPair containsMap = containsMapping(visited, pLeft, pRight);
                        VertexPair containsMap1 = containsMapping(toVisit, pLeft, pRight);
                        if (pairMap != null && containsMap == null && containsMap1 == null) {
                            toVisit.add(pairMap);
                        }
                    }
                }

            }
            if (mapRegion.size() > 0) {
                map.addRegion(mapRegion);
            }
        }

        return map;
    }

    public static boolean containsVertex(LinkedList<VertexPair> mapping, Vertex v) {
        for (VertexPair vp : mapping) {
            if (vp.getLeft().getID().equals(v.getID()) || vp.getRight().getID().equals(v.getID())) {
                return true;
            }
        }
        return false;
    }

    public static Vertex getMappingPair(LinkedList<VertexPair> mapping, Vertex v) {
        for (VertexPair vp : mapping) {
            if (vp.getLeft().getID().equals(v.getID())) {
                return vp.getRight();
            } else if (vp.getRight().getID().equals(v.getID())) {
                return vp.getLeft();
            }
        }
        return null;
    }
}