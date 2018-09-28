/*
 * Copyright 2018 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kie.wb.common.graph.layout.step03;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import kie.wb.common.graph.layout.Edge;
import kie.wb.common.graph.layout.Graph;

//  Gansner et al 1993
public class VertexOrdering {

    private final Graph graph;
    private ArrayList<Layer> layers;
    private Object[][] nestedBestRanks;

    /**
     * Maximum number of iterations to perform.
     */
    private final int MaxIterations = 24;

    public VertexOrdering(Graph graph, ArrayList<Layer> layers) {
        this.graph = graph;
        this.layers = layers;
    }

    public ArrayList<Layer> process() {

        ArrayList<Edge> edges = new ArrayList<>(Arrays.asList(this.graph.getEdges()));
        ArrayList<Layer> virtualized = createVirtual(edges);
        ArrayList<Layer> best = clone(virtualized);

        nestedBestRanks = new Object[virtualized.size()][];
        // Starts with the current order
        for (int i = 0; i < nestedBestRanks.length; i++) {
            Layer layer = best.get(i);
            nestedBestRanks[i] = new Object[layer.getVertices().size()];
            for (int j = 0; j < layer.getVertices().size(); j++) {
                nestedBestRanks[i][j] = layer.getVertices().get(j);
            }
        }

        for (int i = 0; i < MaxIterations; i++) {
            median(virtualized, edges, i);
            // TODO: the transpose function. Can it work only with median?
            //transpose(layers, edges);
            if (this.crossing(best, edges) > this.crossing(virtualized, edges)) {
                best = clone(virtualized);
            }
        }

        return best;
    }

    public static Object[] flat(ArrayList<Edge> edges, Layer north, Layer south) {

        ArrayList<String> southPos = new ArrayList(south.getVertices().size());
        for (int i = 0; i < south.getVertices().size(); i++) {
            southPos.add(south.getVertices().get(i).getId());
        }

        Object[] southEntries = north.getVertices().stream().flatMap(v -> {
            List<Edge> connectedEdges = edges.stream()
                    .filter(e -> (e.getTo().equals(v.getId()) || e.getFrom().equals(v.getId())))
                    .collect(Collectors.toList());

            return connectedEdges.stream().map(e -> {
                if (southPos.contains(e.getTo())) {
                    return southPos.indexOf(e.getTo());
                }
                return southPos.indexOf(e.getFrom());
            }).sorted();
        }).toArray();

        return southEntries;
    }

    public static int crossing(ArrayList<Edge> edges, Layer north, Layer south) {

        Object[] southEntries = flat(edges, north, south);

        int firstIndex = 1;
        while (firstIndex < south.getVertices().size()) {
            firstIndex <<= 1;
        }
        int treeSize = 2 * firstIndex - 1;
        firstIndex -= 1;
        int[] tree = new int[treeSize];

        int cc = 0;

        for (Object entry :
                southEntries) {
            int index = ((Integer) entry) + firstIndex;
            tree[index] += 1;//entry.weight;
            int weightSum = 0;
            while (index > 0) {
                if (index % 2 != 0) {
                    weightSum += tree[index + 1];
                }
                index = (index - 1) >> 1;
                tree[index] += 1; //entry.weight;
            }
            cc += 1 * weightSum;//entry.weight * weightSum;
        }

        return cc;
    }

    public static int crossing(ArrayList<Layer> layers, ArrayList<Edge> edges) {
        int crossingCount = 0;
        for (int i = 1; i < layers.size(); i++) {
            crossingCount += crossing(edges, layers.get(i - 1), layers.get(i));
        }
        return crossingCount;
    }

    private static int positionOf(Edge edge, Layer layer) {
        Optional<Vertex> vertex = layer.getVertices().stream()
                .filter(v -> v.getId().equals(edge.getFrom()) || v.getId().equals(edge.getTo()))
                .findFirst();

        return layer.getVertices().indexOf(vertex.get());
    }

   /* private void transpose(ArrayList<Layer> layers, ArrayList<Edge> edges) {
        boolean improved = true;
        while(improved){
            improved = false;
            for (Layer l:
            ) {

            }
        }
    }*/

    public static void median(ArrayList<Layer> layers, ArrayList<Edge> edges, int i) {

        if ((i % 2 == 0)) {
            for (int j = layers.size() - 1; j >= 1; j--) {
                Layer currentLayer = layers.get(j);
                for (Vertex vertex :
                        currentLayer.getVertices()) {
                    //median value of vertices in rank r-1 connected to v
                    int median = calculateMedianOfVerticesConnectedTo(vertex.getId(), layers.get(j - 1), edges);
                    vertex.setMedian(median);
                    /*if (medians.containsKey(vertex)) {
                        medians.remove(vertex);
                    }
                    medians.put(vertex, median);*/
                }

                // sort the vertices inside layer based on the new order
                currentLayer.getVertices().sort(Vertex::compareTo);
            }
        } else {
            for (int j = 0; j < layers.size() - 1; j++) {
                Layer currentLayer = layers.get(j);

                for (Vertex vertex :
                        layers.get(j).getVertices()) {
                    int median = calculateMedianOfVerticesConnectedTo(vertex.getId(), layers.get(j + 1), edges);
                    vertex.setMedian(median);
                }

                currentLayer.getVertices().sort(Vertex::compareTo);
            }
        }
    }

    private static int calculateMedianOfVerticesConnectedTo(String vertex, Layer layer, ArrayList<Edge> edges) {
        ArrayList<Integer> connectedVerticesIndex = new ArrayList<>();
        ArrayList<Vertex> vertices = layer.getVertices();
        for (int i = 0; i < vertices.size(); i++) {
            Vertex vertexInLayer = vertices.get(i);

            boolean hasConnection = edges.stream()
                    .anyMatch(e -> (e.getFrom().equals(vertexInLayer.getId()) || e.getTo().equals(vertexInLayer.getId()))
                            && (e.getFrom().equals(vertex) || e.getTo().equals(vertex)));

            if (hasConnection) {
                connectedVerticesIndex.add(i);
            }
        }

        double median;
        final int size = connectedVerticesIndex.size();

        if(size == 0){
            return layer.getVertices().indexOf(vertex);
        }
        if (size == 1) {
            return connectedVerticesIndex.get(0);
        }

        if (size % 2 == 0) {
            median = ((double) connectedVerticesIndex.get(size / 2) + (double) connectedVerticesIndex.get(size / 2 - 1)) / 2;
        } else {
            median = (double) connectedVerticesIndex.get(size / 2); // ??????
        }

        return (int) median;
    }

    private ArrayList<Layer> clone(ArrayList<Layer> input) {
        ArrayList<Layer> clone = new ArrayList<>(input.size());
        for (Layer value :
                input) {
            clone.add(value.clone());
        }
        return clone;
    }

    public ArrayList<Layer> createVirtual(ArrayList<Edge> edges) {
        int virtualIndex = 0;
        for (int i = 0; i < layers.size() - 1; i++) {
            Layer currentLayer = layers.get(i);
            Layer nextLayer = layers.get(i + 1);
            for (Vertex vertex :
                    currentLayer.vertices) {

                List<Edge> outgoing = edges.stream()
                        .filter(e -> e.getFrom().equals(vertex.getId()))
                        .filter(e -> Math.abs(getLayerNumber(e.getTo(), layers) - getLayerNumber(vertex.getId(), layers)) > 1)
                        .collect(Collectors.toList());

                List<Edge> incoming = edges.stream()
                        .filter(e -> e.getTo().equals(vertex.getId()))
                        .filter(e -> Math.abs(getLayerNumber(e.getFrom(), layers) - getLayerNumber(vertex.getId(), layers)) > 1)
                        .collect(Collectors.toList());

                for (Edge edge :
                        outgoing) {
                    Vertex virtualVertex = new Vertex("V" + virtualIndex++);
                    nextLayer.vertices.add(virtualVertex);
                    edges.remove(edge);
                    Edge v1 = new Edge(edge.getFrom(), virtualVertex.getId());
                    Edge v2 = new Edge(virtualVertex.getId(), edge.getTo());
                    edges.add(v1);
                    edges.add(v2);
                }

                for (Edge edge :
                        incoming) {
                    Vertex virtualVertex = new Vertex("V" + virtualIndex++);
                    nextLayer.vertices.add(virtualVertex);
                    edges.remove(edge);
                    Edge v1 = new Edge(virtualVertex.getId(), edge.getTo());
                    Edge v2 = new Edge(edge.getFrom(), virtualVertex.getId());
                    edges.add(v1);
                    edges.add(v2);
                }
            }
        }

        return layers; // This doesn't make much sense since we're changing the input collection
    }

    private int getLayerNumber(String vertex, ArrayList<Layer> layers) {
        Optional<Layer> layer = layers
                .stream()
                .filter(l -> l.getVertices().stream().anyMatch(v -> v.getId().equals(vertex)))
                .findFirst();

        return layer.get().level;
    }

    public static class Layer {

        private final int level;
        private final ArrayList<Vertex> vertices;

        public Layer(int level) {
            this.level = level;
            this.vertices = new ArrayList<>();
        }

        public void addNewVertex(String vertexId) {
            this.vertices.add(new Vertex(vertexId));
        }

        public ArrayList<Vertex> getVertices() {
            return this.vertices;
        }

        public Layer clone() {
            final Layer clone = new Layer(this.level);
            for (Vertex v :
                    this.vertices) {
                clone.getVertices().add(v.clone());
            }
            return clone;
        }
    }

    public static class Vertex implements Comparable,
                                          Cloneable {

        private final String id;
        private int median;

        public Vertex(String id) {
            this.id = id;
        }

        public int getMedian() {
            return median;
        }

        public void setMedian(int median) {
            this.median = median;
        }

        @Override
        public Vertex clone() {
            final Vertex clone = new Vertex(this.id);
            clone.setMedian(this.median);
            return clone;
        }

        @Override
        public int compareTo(Object other) {
            if (other instanceof Vertex) {
                if (median < ((Vertex) other).getMedian()) {
                    return -1;
                } else if (median > ((Vertex) other).getMedian()) {
                    return 1;
                }
//                else {
//                    if (reverse) {
//                        return -1;
//                    } else {
//                        return 1;
//                    }
//                }
//            } else {

//            }
            }

            return 0;
        }

        public String getId() {
            return this.id;
        }
    }
}