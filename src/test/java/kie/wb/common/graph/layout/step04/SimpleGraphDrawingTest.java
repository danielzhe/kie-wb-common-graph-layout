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
import java.util.Arrays;
import java.util.HashSet;

import kie.wb.common.graph.layout.Edge;
import kie.wb.common.graph.layout.Graph;
import kie.wb.common.graph.layout.Graphs;
import kie.wb.common.graph.layout.Vertex;
import org.junit.Test;

import static org.junit.Assert.*;

@SuppressWarnings("Duplicates")
public class SimpleGraphDrawingTest {

    @Test
    public void testRemoveToVirtualVertex() {
        //             Input: 01----VIRTUAL----02
        //    Input Vertices: [01,V,02]
        //          Expected: 01----02
        // Expected Vertices: [01,02]
        Vertex virtual = new Vertex("V", true);
        Vertex real_01 = new Vertex("01");
        Vertex real_02 = new Vertex("02");

        Edge edge1 = new Edge("01", "V");
        Edge edge2 = new Edge("V", "02");

        ArrayList<Edge> edges = new ArrayList<>();
        edges.add(edge1);
        edges.add(edge2);

        HashSet<Vertex> vertices = new HashSet<>();
        vertices.add(virtual);
        vertices.add(real_01);
        vertices.add(real_02);

        SimpleGraphDrawing drawing = new SimpleGraphDrawing();
        drawing.removeVirtualVertex(edge1, edges, vertices);

        assertTrue(vertices.contains(real_01));
        assertTrue(vertices.contains(real_02));
        assertFalse(vertices.contains(virtual));
        assertFalse(edges.contains(edge1));
        assertFalse(edges.contains(edge2));
        assertEquals(1, edges.size());

        Edge newEdge = edges.get(0);
        assertTrue(newEdge.isLinkedWith("01"));
        assertTrue(newEdge.isLinkedWith("02"));
    }

    @Test
    public void testRemoveFromVirtualVertex() {
        Vertex virtual = new Vertex("V", true);
        Vertex real_01 = new Vertex("01");
        Vertex real_02 = new Vertex("02");

        Edge edge1 = new Edge("01", "V");
        Edge edge2 = new Edge("V", "02");

        ArrayList<Edge> edges = new ArrayList<>();
        edges.add(edge1);
        edges.add(edge2);

        HashSet<Vertex> vertices = new HashSet<>();
        vertices.add(virtual);
        vertices.add(real_01);
        vertices.add(real_02);

        SimpleGraphDrawing drawing = new SimpleGraphDrawing();
        drawing.removeVirtualVertex(edge2, edges, vertices);

        assertTrue(vertices.contains(real_01));
        assertTrue(vertices.contains(real_02));
        assertFalse(vertices.contains(virtual));
        assertFalse(edges.contains(edge1));
        assertFalse(edges.contains(edge2));
        assertEquals(1, edges.size());

        Edge newEdge = edges.get(0);
        assertTrue(newEdge.isLinkedWith("01"));
        assertTrue(newEdge.isLinkedWith("02"));
    }

    @Test
    public void removeEdgeBetweenVirtualVertices() {

        // Concrete Vertices: C1,C2
        // Virtual: V1, V2, V3
        // Input: C1---->V1---->V2---->V3---->C2

        Vertex c1 = new Vertex("C1");
        Vertex c2 = new Vertex("C2");
        Vertex v1 = new Vertex("V1", true);
        Vertex v2 = new Vertex("V2", true);
        Vertex v3 = new Vertex("V3", true);
        HashSet<Vertex> inputVertex = new HashSet<>();
        inputVertex.add(c1);
        inputVertex.add(c2);
        inputVertex.add(v1);
        inputVertex.add(v2);
        inputVertex.add(v3);

        ArrayList<Edge> edges = new ArrayList<>();
        edges.add(new Edge("C1", "V1"));
        edges.add(new Edge("V1", "V2"));

        Edge betweenVirtualVertices = new Edge("V2", "V3");
        edges.add(betweenVirtualVertices);

        edges.add(new Edge("V3", "C2"));

        SimpleGraphDrawing drawing = new SimpleGraphDrawing();
        drawing.removeVirtualVertex(betweenVirtualVertices, edges, inputVertex);

        assertTrue(inputVertex.contains(c1));
        assertTrue(inputVertex.contains(c2));
        assertTrue(inputVertex.contains(v1));
        assertFalse(inputVertex.contains(v2));
        assertFalse(inputVertex.contains(v3));
        assertFalse(edges.contains(betweenVirtualVertices));
        assertEquals(2, edges.size());
        assertEquals(edges.get(0).getFrom(), "C1");
        assertEquals(edges.get(0).getTo(), "V1");
        assertEquals(edges.get(1).getFrom(), "V1");
        assertEquals(edges.get(1).getTo(), "C2");

        // Input: C1---->V1---->V2---->V3---->C2
        // Expected: C1---->V1---->C2

    }

    @Test
    public void removeAllVirtualVertices() {
        // Input: A---->V1---->V2---->V3---->V4--->B--->C--->V5--->V6--->D--->V7--->V8--->E
        Vertex[] vertices = new Vertex[]{
                new Vertex("A"),
                new Vertex("V1", true),
                new Vertex("V2", true),
                new Vertex("V3", true),
                new Vertex("V4", true),
                new Vertex("B"),
                new Vertex("C"),
                new Vertex("V5", true),
                new Vertex("V6", true),
                new Vertex("D"),
                new Vertex("V7", true),
                new Vertex("V8", true),
                new Vertex("E")
        };

        ArrayList<Edge> edges = connect(vertices);

        // Expected: A--->B--->C--->D--->E
        SimpleGraphDrawing drawing = new SimpleGraphDrawing();
        HashSet<Vertex> array = new HashSet<>(Arrays.asList(vertices));
        drawing.removeVirtualVertices(edges, array);

        assertEquals(4, edges.size());
        assertTrue(edges.contains(new Edge("A", "B")));
        assertTrue(edges.contains(new Edge("B", "C")));
        assertTrue(edges.contains(new Edge("C", "D")));
        assertTrue(edges.contains(new Edge("D", "E")));

        assertEquals(5, array.size());
        assertArrayEquals(new String[]{"A", "B", "C", "D", "E"},
                          array.stream().map(v -> v.getId()).sorted().toArray());
    }


    private ArrayList<Edge> connect(Vertex... vertices) {
        ArrayList<Edge> edges = new ArrayList<>();
        for (int i = 0; i < vertices.length; i++) {

            if (i + 1 < vertices.length) {
                Edge e = new Edge(vertices[i].getId(), vertices[i + 1].getId());
                edges.add(e);
            }
        }
        return edges;
    }
}