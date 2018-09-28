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

package kie.wb.common.graph.layout.step02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import kie.wb.common.graph.layout.Graph;

/**
 * Assign each vertex in a graph to a layers, using the longest path algorithm.
 */
public class LongestPathVertexLayerer {

    private final Vertex[] vertices;
    private final int[] vertexHeight;
    private final Graph graph;
    private final ArrayList<Layer> layers;

    public LongestPathVertexLayerer(Graph graph) {

        String[] vertices = graph.getVertices();

        this.layers = new ArrayList<>();
        this.graph = graph;
        this.vertices = new Vertex[vertices.length];
        this.vertexHeight = new int[vertices.length];

        for (int i = 0; i < vertices.length; i++) {
            String v = vertices[i];
            this.vertices[i] = new Vertex(v, i);
            this.vertexHeight[i] = -1;
        }
    }

    public void execute() {
        for (Vertex vertex :
                this.vertices) {
            visit(vertex);
        }
    }

    private int visit(Vertex vertex) {
        int height = this.vertexHeight[vertex.id];
        if (height >= 0) {
            return height;
        }

        int maxHeight = 1;

        String[] verticesFromHere = graph.getVerticesFrom(vertex.getLabel());
        for (String nextVertex :
                verticesFromHere) {
            if(!nextVertex.equals(vertex.getLabel())){
                Optional<Vertex> next = Arrays.stream(this.vertices)
                        .filter(f -> f.getLabel().equals(nextVertex))
                        .findFirst();
                int targetHeight = visit(next.get());
                maxHeight = Math.max(maxHeight, targetHeight+1);
            }
        }

        addToLayer(vertex, maxHeight);
        return maxHeight;
    }

    private void addToLayer(Vertex vertex, int height) {
        for(int i = this.layers.size(); i <height; i++){
            layers.add(0, new Layer());
        }

        int level = layers.size() - height;
        Layer layer = layers.get(level);
        layer.setLevel(height);
        vertex.setLayer(layer);
        vertexHeight[vertex.id] = height;
    }

    public Vertex[] getVertices(){
        return this.vertices;
    }

    private class Layer{

        private int level;

        public void setLevel(int level) {
            this.level = level;
        }

        public int getLevel() {
            return level;
        }
    }

    private class Vertex {

        private final String label;
        private int id;
        private Layer layer;

        private Vertex(String label, int id) {
            this.label = label;
            this.id = id;
        }

        public int getId() {
            return this.id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLabel() {
            return this.label;
        }

        public void setLayer(Layer layer) {

            this.layer = layer;
        }

        public Layer getLayer() {
            return this.layer;
        }
    }
}