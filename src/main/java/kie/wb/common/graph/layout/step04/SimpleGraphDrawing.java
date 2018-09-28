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

package kie.wb.common.graph.layout.step04;

import java.util.ArrayList;

import kie.wb.common.graph.layout.Edge;
import kie.wb.common.graph.layout.Graph;
import kie.wb.common.graph.layout.Layer;
import kie.wb.common.graph.layout.Vertex;
import kie.wb.common.graph.layout.step03.VertexOrdering;

/**
 * Calculate position for each vertex in a graph using the simplest approach.
 * 1. Vertices are horizontal distributed inside its layer, using the same space between each one
 * 2. All layers are vertical centered
 * 3. The space between layers is the same
 */
public class SimpleGraphDrawing {

    /*
     * Pre:
     * 1. De-reverse reversed layers
     * 2. Remove dummy vertices and reconnect each side
     */

   /* public Draw draw(ArrayList<Layer> layers, ArrayList<Edge> edges){
        return null;
    }*/

   /* public final class Draw{

        private final ArrayList<VertexPosition> vertices;
        private final ArrayList<Edge> edges;

        public Draw(){
            this.edges = new ArrayList<>();
            this.vertices = new ArrayList<>();
        }

        public void addVertex(final String id, final int x, final int y){
            this.vertices.add(new VertexPosition(id, x, y));
        }

        public void addEdge(final Edge edge){
            this.edges.add(edge);
        }

        public ArrayList<Edge> getEdges() {
            return edges;
        }

        public ArrayList<VertexPosition> getVertices() {
            return vertices;
        }
    }*/

    public final class VertexPosition{

        private int x;
        private int y;
        private final Vertex vertex;

        public VertexPosition(final Vertex vertex){
            this.vertex = vertex;
        }

        public VertexPosition(final Vertex vertex, final int x, final int y){
            this(vertex);
            this.x = x;
            this.y = y;
        }

        public Vertex getVertex(){
            return this.vertex;
        }

        public int getX(){
            return this.x;
        }

        public void setX(int x){
            this.x = x;
        }

        public int getY(){
            return this.y;
        }

        public void setY(int y){
            this.y = y;
        }
    }
}