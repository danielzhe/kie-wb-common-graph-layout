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

import kie.wb.common.graph.layout.Edge;
import kie.wb.common.graph.layout.Graph;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class VertexOrderingTest {

    @Test
    public void testSimpleReorder() {
        Graph graph = new Graph();
        graph.addEdge("A", "D");
        graph.addEdge("B", "C");

        ArrayList<VertexOrdering.Layer> layers = new ArrayList<>();
        VertexOrdering.Layer layer01 = new VertexOrdering.Layer(1);
        layer01.addNewVertex("A");
        layer01.addNewVertex("B");
        layers.add(layer01);

        VertexOrdering.Layer layer02 = new VertexOrdering.Layer(2);
        layer02.addNewVertex("C");
        layer02.addNewVertex("D");
        layers.add(layer02);

        VertexOrdering ordering = new VertexOrdering(graph, layers);
        ArrayList<VertexOrdering.Layer> orderedLayers = ordering.process();

        assertArrayEquals(new Object[]{"A", "B"}, orderedLayers.get(0).getVertices().stream().map(v -> v.getId()).toArray());
        assertArrayEquals(new Object[]{"D", "C"}, orderedLayers.get(1).getVertices().stream().map(v -> v.getId()).toArray());
    }

    @Test
    public void testReorder() {
        Graph graph = new Graph();
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("F", "B");
        graph.addEdge("C", "E");
        graph.addEdge("G", "C");
        graph.addEdge("C", "H");
        graph.addEdge("D", "F");

        ArrayList<VertexOrdering.Layer> layers = new ArrayList<>();
        VertexOrdering.Layer layer01 = new VertexOrdering.Layer(1);
        layer01.addNewVertex("A");
        layers.add(layer01);

        VertexOrdering.Layer layer02 = new VertexOrdering.Layer(2);
        layer02.addNewVertex("B");
        layer02.addNewVertex("C");
        layer02.addNewVertex("D");
        layers.add(layer02);

        VertexOrdering.Layer layer03 = new VertexOrdering.Layer(3);
        layer03.addNewVertex("E");
        layer03.addNewVertex("F");
        layer03.addNewVertex("G");
        layer03.addNewVertex("H");
        layers.add(layer03);

        VertexOrdering ordering = new VertexOrdering(graph, layers);
        ArrayList<VertexOrdering.Layer> orderedLayers = ordering.process();

        assertArrayEquals(new Object[]{"A"}, orderedLayers.get(0).getVertices().stream().map(v -> v.getId()).toArray());
        assertArrayEquals(new Object[]{"D", "B", "C"}, orderedLayers.get(1).getVertices().stream().map(v -> v.getId()).toArray());
        assertArrayEquals(new Object[]{"F", "E", "G", "H"}, orderedLayers.get(2).getVertices().stream().map(v -> v.getId()).toArray());
    }
   /* @Test
    public void createVirtual() {
        Graph graph = new Graph();
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("A", "D");
        graph.addEdge("B", "E");
        graph.addEdge("C", "F");
        graph.addEdge("D", "G");
        graph.addEdge("A", "H");

        ArrayList<VertexOrdering.Layer> layers = new ArrayList<>();
        VertexOrdering.Layer layer1 = new VertexOrdering.Layer(1);
        layer1.addNewVertex("A");
        layers.add(layer1);

        VertexOrdering.Layer layer2 = new VertexOrdering.Layer(2);
        layer2.addNewVertex("B");
        layer2.addNewVertex("C");
        layer2.addNewVertex("D");
        layers.add(layer2);

        VertexOrdering.Layer layer3 = new VertexOrdering.Layer(3);
        layer3.addNewVertex("E");
        layer3.addNewVertex("F");
        layer3.addNewVertex("G");
        layer3.addNewVertex("H");
        layers.add(layer3);

        VertexOrdering ordering = new VertexOrdering(graph, layers);

        ArrayList<Edge> edges = new ArrayList<Edge>(Arrays.asList(graph.getEdges()));
        ArrayList<VertexOrdering.Layer> virtual = ordering.createVirtual(edges);

        virtual.size();
    }

    @Test
    public void calculateMedianTest(){

        ArrayList<VertexOrdering.Layer> layers = new ArrayList<>();
        ArrayList<Edge> edges = new ArrayList<>();
        edges.add(new Edge("A", "F"));
        edges.add(new Edge("B", "E"));
        edges.add(new Edge("C", "D"));

        VertexOrdering.Layer layer00 = new VertexOrdering.Layer(0);
        layer00.addNewVertex("A");
        layer00.addNewVertex("B");
        layer00.addNewVertex("C");
        VertexOrdering.Layer layer01 = new VertexOrdering.Layer(1);
        layer01.addNewVertex("D");
        layer01.addNewVertex("E");
        layer01.addNewVertex("F");
        layers.add(layer00);
        layers.add(layer01);
        int i = 0;

        VertexOrdering.median(layers,edges,i);

        ArrayList<VertexOrdering.Vertex> expectedOrder = new ArrayList<>();
        expectedOrder.add(new VertexOrdering.Vertex("F"));
        expectedOrder.add(new VertexOrdering.Vertex("E"));
        expectedOrder.add(new VertexOrdering.Vertex("D"));



        Assert.assertTrue(true);

    }

    @Test
    public void testFlat(){
        ArrayList<VertexOrdering.Layer> layers = new ArrayList<>();
        VertexOrdering.Layer top = new VertexOrdering.Layer(0);
        top.addNewVertex("A");
        top.addNewVertex("B");
        top.addNewVertex("C");
        layers.add(top);

        VertexOrdering.Layer bottom = new VertexOrdering.Layer(1);
        bottom.addNewVertex("D");
        bottom.addNewVertex("E");
        bottom.addNewVertex("F");
        layers.add(bottom);

        ArrayList<Edge> edges = new ArrayList<>();
        edges.add(new Edge("A", "D"));
        edges.add(new Edge("A","E"));
        edges.add(new Edge("A","F"));

        Object[] expected = VertexOrdering.flat(edges, top, bottom);

        Assert.assertTrue(true);
    }*/

    //crossing

    @Test
    public void testSimpleCrossing() {
        ArrayList<VertexOrdering.Layer> layers = new ArrayList<>();
        VertexOrdering.Layer top = new VertexOrdering.Layer(0);
        top.addNewVertex("A");
        top.addNewVertex("B");
        layers.add(top);

        VertexOrdering.Layer bottom = new VertexOrdering.Layer(1);
        bottom.addNewVertex("C");
        bottom.addNewVertex("D");
        layers.add(bottom);

        ArrayList<Edge> edges = new ArrayList<>();
        edges.add(new Edge("A", "D"));
        edges.add(new Edge("B", "C"));

        int result = VertexOrdering.crossing(edges, top, bottom);

        assertEquals(1, result);
    }

    @Test
    public void testSimpleNoCrossing() {
        ArrayList<VertexOrdering.Layer> layers = new ArrayList<>();
        VertexOrdering.Layer top = new VertexOrdering.Layer(0);
        top.addNewVertex("A");
        top.addNewVertex("B");
        layers.add(top);

        VertexOrdering.Layer bottom = new VertexOrdering.Layer(1);
        bottom.addNewVertex("C");
        bottom.addNewVertex("D");
        layers.add(bottom);

        ArrayList<Edge> edges = new ArrayList<>();
        edges.add(new Edge("A", "C"));
        edges.add(new Edge("B", "D"));

        int result = VertexOrdering.crossing(edges, top, bottom);

        assertEquals(0, result);
    }

    @Test
    public void test1Crossing() {

        /*
         * 1 crossing
         * A   B   C
         *   \   /
         *     X
         *   /   \
         * D   E   F
         */
        ArrayList<VertexOrdering.Layer> layers = new ArrayList<>();
        VertexOrdering.Layer top = new VertexOrdering.Layer(0);
        top.addNewVertex("A");
        top.addNewVertex("B");
        top.addNewVertex("C");
        layers.add(top);

        VertexOrdering.Layer bottom = new VertexOrdering.Layer(1);
        bottom.addNewVertex("D");
        bottom.addNewVertex("E");
        bottom.addNewVertex("F");
        layers.add(bottom);

        ArrayList<Edge> edges = new ArrayList<>();
        edges.add(new Edge("A", "F"));
        edges.add(new Edge("D", "C"));

        int result = VertexOrdering.crossing(edges, top, bottom);

        assertEquals(1, result);
    }

    @Test
    public void test2CrossingsUnevenLayers() {

        /*
         * 2 crossings
         *       A           B
         *      /\\         /
         *     /  \ -------+ --
         *    / /-- +-----/    \
         *   / /     \          \
         *  D         E          F
         * */
        ArrayList<VertexOrdering.Layer> layers = new ArrayList<>();
        VertexOrdering.Layer top = new VertexOrdering.Layer(0);
        top.addNewVertex("A");
        top.addNewVertex("B");
        //top.addNewVertex("C");
        layers.add(top);

        VertexOrdering.Layer bottom = new VertexOrdering.Layer(1);
        bottom.addNewVertex("D");
        bottom.addNewVertex("E");
        bottom.addNewVertex("F");
        layers.add(bottom);

        ArrayList<Edge> edges = new ArrayList<>();
        edges.add(new Edge("A", "D"));
        edges.add(new Edge("E", "A"));
        edges.add(new Edge("A", "F"));
        edges.add(new Edge("D", "B"));

        int result = VertexOrdering.crossing(edges, top, bottom);

        assertEquals(2, result);
    }

    @Test
    public void test2Crossings() {

        /*
         * 2 crossings
         * A   B   C
         *  \  | /
         *   x x
         *  / \|
         * D   E   F
         * */
        ArrayList<VertexOrdering.Layer> layers = new ArrayList<>();
        VertexOrdering.Layer top = new VertexOrdering.Layer(0);
        top.addNewVertex("A");
        top.addNewVertex("B");
        top.addNewVertex("C");
        layers.add(top);

        VertexOrdering.Layer bottom = new VertexOrdering.Layer(1);
        bottom.addNewVertex("D");
        bottom.addNewVertex("E");
        bottom.addNewVertex("F");
        layers.add(bottom);

        ArrayList<Edge> edges = new ArrayList<>();
        edges.add(new Edge("A", "E"));
        edges.add(new Edge("B", "E"));
        edges.add(new Edge("C", "D"));

        int result = VertexOrdering.crossing(edges, top, bottom);

        assertEquals(2, result);
    }

    @Test
    public void test2Crossing8Vertex() {

        /*
         * 2 crossing
         *  A       B       C        D
         *  |\--\   |        \---\   |
         *  | \  \--+-------\     \  |
         *  |  \----+-----\  \     \ |
         *  |       |      \  \---\ \|
         *  E       F       G        H
         */
        ArrayList<VertexOrdering.Layer> layers = new ArrayList<>();
        VertexOrdering.Layer top = new VertexOrdering.Layer(0);
        top.addNewVertex("A");
        top.addNewVertex("B");
        top.addNewVertex("C");
        top.addNewVertex("D");
        layers.add(top);

        VertexOrdering.Layer bottom = new VertexOrdering.Layer(1);
        bottom.addNewVertex("E");
        bottom.addNewVertex("F");
        bottom.addNewVertex("G");
        bottom.addNewVertex("H");
        layers.add(bottom);

        ArrayList<Edge> edges = new ArrayList<>();
        edges.add(new Edge("A", "E"));
        edges.add(new Edge("A", "G"));
        edges.add(new Edge("A", "H"));
        edges.add(new Edge("B", "F"));
        edges.add(new Edge("C", "H"));
        edges.add(new Edge("D", "H"));

        int result = VertexOrdering.crossing(edges, top, bottom);

        assertEquals(2, result);
    }

    @Test
    public void test3CrossingsInMiddle() {

        /*
         * 3 crossings
         * A   B   C
         *   \ |  /
         *     X
         *   / | \
         * D   E   F
         */
        ArrayList<VertexOrdering.Layer> layers = new ArrayList<>();
        VertexOrdering.Layer top = new VertexOrdering.Layer(0);
        top.addNewVertex("A");
        top.addNewVertex("B");
        top.addNewVertex("C");
        layers.add(top);

        VertexOrdering.Layer bottom = new VertexOrdering.Layer(1);
        bottom.addNewVertex("D");
        bottom.addNewVertex("E");
        bottom.addNewVertex("F");
        layers.add(bottom);

        ArrayList<Edge> edges = new ArrayList<>();
        edges.add(new Edge("A", "F"));
        edges.add(new Edge("B", "E"));
        edges.add(new Edge("C", "D"));

        int result = VertexOrdering.crossing(edges, top, bottom);

        assertEquals(3, result);
    }

    @Test
    public void testK33GraphCrossing() {

        /*
         * k33 - every vertex from layer 1 connected to every vertex in layer 2
         */
        ArrayList<VertexOrdering.Layer> layers = new ArrayList<>();
        VertexOrdering.Layer top = new VertexOrdering.Layer(0);
        top.addNewVertex("A");
        top.addNewVertex("B");
        top.addNewVertex("C");
        layers.add(top);

        VertexOrdering.Layer bottom = new VertexOrdering.Layer(1);
        bottom.addNewVertex("D");
        bottom.addNewVertex("E");
        bottom.addNewVertex("F");
        layers.add(bottom);

        ArrayList<Edge> edges = new ArrayList<>();
        edges.add(new Edge("A", "D"));
        edges.add(new Edge("A", "E"));
        edges.add(new Edge("A", "F"));

        edges.add(new Edge("B", "D"));
        edges.add(new Edge("B", "E"));
        edges.add(new Edge("B", "F"));

        edges.add(new Edge("C", "D"));
        edges.add(new Edge("C", "E"));
        edges.add(new Edge("C", "F"));

        int result = VertexOrdering.crossing(edges, top, bottom);

        assertEquals(9, result);
    }

    @Test
    public void test5VertexUnevenLayersNoCrossing() {

        /*
         * 3 crossings
         * A    B
         * |   /|\
         * |  / | \
         * | /  |  \
         * D    E   F
         */
        ArrayList<VertexOrdering.Layer> layers = new ArrayList<>();
        VertexOrdering.Layer top = new VertexOrdering.Layer(0);
        top.addNewVertex("A");
        top.addNewVertex("B");
        //top.addNewVertex("C");
        layers.add(top);

        VertexOrdering.Layer bottom = new VertexOrdering.Layer(1);
        bottom.addNewVertex("D");
        bottom.addNewVertex("E");
        bottom.addNewVertex("F");
        layers.add(bottom);

        ArrayList<Edge> edges = new ArrayList<>();
        edges.add(new Edge("A", "D"));
        edges.add(new Edge("B", "D"));
        edges.add(new Edge("E", "B"));
        edges.add(new Edge("F", "B"));

        int result = VertexOrdering.crossing(edges, top, bottom);

        assertEquals(0, result);
    }
}
