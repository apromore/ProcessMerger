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

package common.algos;

import com.google.common.collect.TreeMultimap;
import com.google.common.collect.TreeMultiset;
import graph.Graph;
import graph.Vertex;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import common.Settings;
import common.similarity.AssingmentProblem;
import common.similarity.NodeSimilarity;

/**
 * Class that implements the algorithm to compute the edit distance between two
 * SimpleGraph instances. Use the algorithm by calling the constructor with the two
 * SimpleGraph instances between which you want to compute the edit distance. Then call
 * compute(), which will return the edit distance.
 */
public class GraphEditDistanceGreedy extends DistanceAlgoAbstr implements DistanceAlgo{

    public int nrSubstitudedVertices = 0;
    boolean deterministic = true;

    private Set<TwoVertices> times(List<Vertex> a, List<Vertex> b){
        Set<TwoVertices> result = new HashSet<TwoVertices>();
        for (Vertex ea: a){
            for (Vertex eb: b){
                double similarity = NodeSimilarity.findNodeSimilarity(ea, eb);
                if (ea.getType().equals(Vertex.Type.gateway) && eb.getType().equals(Vertex.Type.gateway)
                        && similarity >= Settings.MERGE_CONTEXT_THRESHOLD) {
                    result.add(new TwoVertices(ea.getID(), eb.getID(), 1 - similarity));
//                  System.out.println(">ADDING PAI r" + ea +" <> "+ eb + " ; sim "+ similarity);
                } else if ((ea.getType().equals(Vertex.Type.event) && eb.getType().equals(Vertex.Type.event)
                        || ea.getType().equals(Vertex.Type.function) && eb.getType().equals(Vertex.Type.function)) &&
                        AssingmentProblem.canMap(ea, eb) && similarity >= Settings.MERGE_THRESHOLD){
                    result.add(new TwoVertices(ea.getID(), eb.getID(), 1 - similarity));
//                  System.out.println(">ADDING PAI r" + ea +" <> "+ eb + " ; sim "+ similarity);
                }
            }
        }
        return result;
    }

    public Set<TwoVertices> compute(Graph sg1, Graph sg2){
        init(sg1,sg2);

        //INIT
        BestMapping mapping = new BestMapping();
        Set<TwoVertices> openCouples = times(sg1.getVertices(), sg2.getVertices());
        double shortestEditDistance = Double.MAX_VALUE;
        Random randomized = new Random(123456789);
        int stepn = 0;
        //STEP
        boolean doStep = true;
        while (doStep){
            doStep = false;
            stepn++;
            Vector<TwoVertices> bestCandidates = new Vector<TwoVertices>();
            double newShortestEditDistance = shortestEditDistance;
//          long s1 = System.currentTimeMillis();
//          System.out.println("step : "+stepn + " ; "+ openCouples.size());
            for (TwoVertices couple: openCouples){
//              System.out.println(">> PROCESSING COUPLE : " + sg1.getVertexMap().get(couple.v1)+ " <> "+ sg2.getVertexMap().get(couple.v2));

//              long t1 = System.currentTimeMillis();
                double newEditDistance = this.editDistance(mapping, couple);
//              System.out.println("\t edit distance : " + newEditDistance + " ; shortest " + shortestEditDistance);

//              long t2 = System.currentTimeMillis();
//              System.out.println((t2-t1)+ " ms: openpair "+sg1.getLabel(couple.v1) + " "+ sg2.getLabel(couple.v2) + " "+newEditDistance+ " "+mapping.size());
                if (newEditDistance < newShortestEditDistance){
                    bestCandidates = new Vector<TwoVertices>();
                    bestCandidates.add(couple);
//                  System.out.println("\t>> ADD COUPLE to bestcandidates(clean bestcand) : " + sg1.getVertexMap().get(couple.v1)+ " <> "+ sg2.getVertexMap().get(couple.v2));
                    newShortestEditDistance = newEditDistance;
                }else if (newEditDistance == newShortestEditDistance){
                    bestCandidates.add(couple);
//                  System.out.println("\t>> ADD COUPLE to bestcandidates : " + sg1.getVertexMap().get(couple.v1)+ " <> "+ sg2.getVertexMap().get(couple.v2));

                }
            }

            if (bestCandidates.size() > 0) {
                TwoVertices couple;

                // Case 1: Only one candidate pair
                if (bestCandidates.size() == 1) {
                    couple = bestCandidates.firstElement();
                } else {
                    //  CASE 2: Lexicographical order is enough
                    TreeMultimap<String, TwoVertices> tmap = TreeMultimap.create();
                    for (TwoVertices pair: bestCandidates) {
                        String label1 = sg1.getVertexLabel(pair.v1);
                        String label2 = sg2.getVertexLabel(pair.v2);
                        if (label1 != null && label2 != null && label1.compareTo(label2) > 0) {
                            String tmp = label1;
                            label1 = label2;
                            label2 = tmp;
                        }
                        tmap.put(label1+label2, pair);
                    }
                    String firstkey = tmap.keySet().first();

                    if (tmap.get(firstkey).size() == 1) {
                        couple = tmap.get(firstkey).first();
                    } else if (tmap.get(firstkey).size() > 1) {
                        Set<TwoVertices> set = tmap.get(firstkey);
                        TreeMultimap<String, TwoVertices> tmapp = TreeMultimap.create();

                        String label1;
                        String tmpLabel;
                        TreeMultiset<String> mset = TreeMultiset.create();
                        for (TwoVertices pair: set) {
                            label1 = sg1.getVertexLabel(pair.v1);
                            mset.clear();
                            for (Vertex n: sg1.getPreset(pair.v1)) {
                                tmpLabel = sg1.getVertexLabel(n.getID());
                                if (tmpLabel != null) {
                                    mset.add(tmpLabel);
                                }
                            }
                            label1 += mset.toString();
                            mset.clear();
                            for (Vertex n: sg1.getPostset(pair.v1)) {
                                tmpLabel = sg1.getVertexLabel(n.getID());
                                if (tmpLabel != null) {
                                    mset.add(tmpLabel);
                                }
                            }
                            label1 += mset.toString();

                            String label2 = sg2.getVertexLabel(pair.v2);
                            mset.clear();
                            for (Vertex n: sg2.getPreset(pair.v2)) {
                                tmpLabel = sg2.getVertexLabel(n.getID());
                                if (tmpLabel != null) {
                                    mset.add(tmpLabel);
                                }
                            }
                            label2 += mset.toString();
                            mset.clear();
                            for (Vertex n: sg2.getPostset(pair.v2)) {
                                tmpLabel = sg2.getVertexLabel(n.getID());
                                if (tmpLabel != null) {
                                    mset.add(tmpLabel);
                                }
                            }
                            label2 += mset.toString();

                            if (label1.compareTo(label2) > 0) {
                                String tmp = label1;
                                label1 = label2;
                                label2 = tmp;
                            }
                            tmapp.put(label1+label2, pair);
                        }
                        String contextkey = tmapp.keySet().first();
                        // CASE 3: Composite labels (concatenation of labels of nodes surrounding the target vertex)
                        if (tmapp.get(contextkey).size() == 1) {
                            couple = tmapp.get(contextkey).first();
                        } else {
                            // CASE 4: Non deterministic choice (Choose a random candidate)
//                            System.out.println("CASE 4 oops ...");
                            deterministic = false;
                            couple = bestCandidates.get(randomized.nextInt(bestCandidates.size()));
                        }
                    } else {
                        // CASE 5: Non deterministic choice (Choose a random candidate)
//                        System.out.println("CASE 5 oops ...");
                        deterministic = false;
                        couple = bestCandidates.get(randomized.nextInt(bestCandidates.size()));
                    }
                }

                Set<TwoVertices> newOpenCouples = new HashSet<TwoVertices>();
                for (TwoVertices p : openCouples) {
                    if (!p.v1.equals(couple.v1) && !p.v2.equals(couple.v2)) {
                        newOpenCouples.add(p);
                    }
                }
                openCouples = newOpenCouples;

                mapping.addPair(couple);
//              System.out.println("<><>MAPPING ADD COUPLE : " + sg1.getVertexMap().get(couple.v1)+ " <> "+ sg2.getVertexMap().get(couple.v2));
                shortestEditDistance = newShortestEditDistance;
                doStep = true;
            }
//          long s2 = System.currentTimeMillis();
//          System.out.println("step took time : "+(s2-s1));
        }

//      for (TwoVertices pair : mapping.getMapping()) {
//          Vertex v1 = sg1.getVertexMap().get(pair.v1);
//          Vertex v2 = sg2.getVertexMap().get(pair.v2);
//          System.out.println(v1 + " <> "+ v2 + " " + (1-pair.weight));
//      }
//      
//      System.out.println("substitutedV "+ mapping.getMapping().size() + " ; substituedEdges "+ mapping.nrMappedEdges + " similarity "+ (1-shortestEditDistance));

//      System.out.println("shortest ed : "+shortestEditDistance);
        //Return the smallest edit distance
        return mapping.mapping;
    }

    public double computeGED(Graph sg1, Graph sg2){
        return computeGED(sg1, sg2, false);
    }

    public double computeGED(Graph sg1, Graph sg2, boolean print) {
        init(sg1,sg2);

        //INIT
        BestMapping mapping = new BestMapping();
        Set<TwoVertices> openCouples = times(sg1.getVertices(), sg2.getVertices());
        double shortestEditDistance = Double.MAX_VALUE;
        Random randomized = new Random();
        int stepn = 0;
        //STEP
        boolean doStep = true;
        while (doStep){
            doStep = false;
            stepn++;
            Vector<TwoVertices> bestCandidates = new Vector<TwoVertices>();
            double newShortestEditDistance = shortestEditDistance;
//          long s1 = System.currentTimeMillis();
//          System.out.println("step : "+stepn + " ; "+ openCouples.size());
            for (TwoVertices couple: openCouples){
//              System.out.println(">> PROCESSING COUPLE : " + sg1.getVertexMap().get(couple.v1)+ " <> "+ sg2.getVertexMap().get(couple.v2));

//              long t1 = System.currentTimeMillis();
                double newEditDistance = this.editDistance(mapping, couple);

//              System.out.println("\t edit distance : " + newEditDistance + " ; shortest " + shortestEditDistance);

//              long t2 = System.currentTimeMillis();
//              System.out.println((t2-t1)+ " ms: openpair "+sg1.getLabel(couple.v1) + " "+ sg2.getLabel(couple.v2) + " "+newEditDistance+ " "+mapping.size());
                if (newEditDistance < newShortestEditDistance){
                    bestCandidates = new Vector<TwoVertices>();
                    bestCandidates.add(couple);
//                  System.out.println("\t>> ADD COUPLE to bestcandidates(clean bestcand) : " + sg1.getVertexMap().get(couple.v1)+ " <> "+ sg2.getVertexMap().get(couple.v2));
                    newShortestEditDistance = newEditDistance;
                }else if (newEditDistance == newShortestEditDistance){
                    bestCandidates.add(couple);
//                  System.out.println("\t>> ADD COUPLE to bestcandidates : " + sg1.getVertexMap().get(couple.v1)+ " <> "+ sg2.getVertexMap().get(couple.v2));

                }
            }

            if (bestCandidates.size() > 0){
                TwoVertices couple;

                // Case 1: Only one candidate pair
                if (bestCandidates.size() == 1) {
                    couple = bestCandidates.firstElement();
                } else {
                    //  CASE 2: Lexicographical order is enough
                    TreeMultimap<String, TwoVertices> tmap = TreeMultimap.create();
                    for (TwoVertices pair: bestCandidates) {
                        String label1 = sg1.getVertexLabel(pair.v1);
                        String label2 = sg2.getVertexLabel(pair.v2);
                        if (label1 != null && label2 != null && label1.compareTo(label2) > 0) {
                            String tmp = label1;
                            label1 = label2;
                            label2 = tmp;
                        }
                        tmap.put(label1+label2, pair);
                    }
                    String firstkey = tmap.keySet().first();

                    if (tmap.get(firstkey).size() == 1) {
                        couple = tmap.get(firstkey).first();
                    } else if (tmap.get(firstkey).size() > 1) {
                        Set<TwoVertices> set = tmap.get(firstkey);
                        TreeMultimap<String, TwoVertices> tmapp = TreeMultimap.create();

                        String label1;
                        String tmpLabel;
                        TreeMultiset<String> mset = TreeMultiset.create();
                        for (TwoVertices pair: set) {
                            label1 = sg1.getVertexLabel(pair.v1);
                            mset.clear();
                            for (Vertex n: sg1.getPreset(pair.v1)) {
                                tmpLabel = sg1.getVertexLabel(n.getID());
                                if (tmpLabel != null) {
                                    mset.add(tmpLabel);
                                }
                            }
                            label1 += mset.toString();
                            mset.clear();
                            for (Vertex n: sg1.getPostset(pair.v1)) {
                                tmpLabel = sg1.getVertexLabel(n.getID());
                                if (tmpLabel != null) {
                                    mset.add(tmpLabel);
                                }
                            }
                            label1 += mset.toString();

                            String label2 = sg2.getVertexLabel(pair.v2);
                            mset.clear();
                            for (Vertex n: sg2.getPreset(pair.v2)) {
                                tmpLabel = sg2.getVertexLabel(n.getID());
                                if (tmpLabel != null) {
                                    mset.add(tmpLabel);
                                }
                            }
                            label2 += mset.toString();
                            mset.clear();
                            for (Vertex n: sg2.getPostset(pair.v2)) {
                                tmpLabel = sg2.getVertexLabel(n.getID());
                                if (tmpLabel != null) {
                                    mset.add(tmpLabel);
                                }
                            }
                            label2 += mset.toString();

                            if (label1.compareTo(label2) > 0) {
                                String tmp = label1;
                                label1 = label2;
                                label2 = tmp;
                            }
                            tmapp.put(label1+label2, pair);
                        }
                        String contextkey = tmapp.keySet().first();
                        // CASE 3: Composite labels (concatenation of labels of nodes surrounding the target vertex)
                        if (tmapp.get(contextkey).size() == 1) {
                            couple = tmapp.get(contextkey).first();
                        } else {
                            // CASE 4: Non deterministic choice (Choose a random candidate)
                            deterministic = false;
                            couple = bestCandidates.get(randomized.nextInt(bestCandidates.size()));
                        }
                    } else {
                        // CASE 5: Non deterministic choice (Choose a random candidate)
                        System.out.println("oops ...");
                        deterministic = false;
                        couple = bestCandidates.get(randomized.nextInt(bestCandidates.size()));
                    }
                }

                Set<TwoVertices> newOpenCouples = new HashSet<TwoVertices>();
                for (TwoVertices p: openCouples){
//                  System.out.println("\t\t\t<><>openCouples : " + sg1.getVertexMap().get(p.v1)+ " <> "+ sg2.getVertexMap().get(p.v2));
                    if (!p.v1.equals(couple.v1) && !p.v2.equals(couple.v2)){
                        newOpenCouples.add(p);
//                      System.out.println("\t\t\t\tADDING TO NEW MAPPP!!!");
                    }
                }
                openCouples = newOpenCouples;

                mapping.addPair(couple);
//              System.out.println("<><>MAPPING ADD COUPLE : " + sg1.getVertexMap().get(couple.v1)+ " <> "+ sg2.getVertexMap().get(couple.v2));
                shortestEditDistance = newShortestEditDistance;
                doStep = true;
            }
//          long s2 = System.currentTimeMillis();
//          System.out.println("step took time : "+(s2-s1));
        }

        nrSubstitudedVertices = mapping.size();

        if (print) {
            for (TwoVertices pair : mapping.getMapping()) {
                Vertex v1 = sg1.getVertexMap().get(pair.v1);
                Vertex v2 = sg2.getVertexMap().get(pair.v2);

                System.out.println(v1 + " <> "+ v2 + " " + NodeSimilarity.findNodeSimilarity(v1, v2));

            }
        }
//      System.out.println("substitutedV "+ mapping.getMapping().size() + " ; substituedEdges "+ mapping.nrMappedEdges);
//      System.out.println("shortest ed : "+shortestEditDistance);
        //Return the smallest edit distance
        return shortestEditDistance;
    }

}