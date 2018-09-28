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

package kie.wb.common.graph.layout.step01;

import kie.wb.common.graph.layout.Graph;
import kie.wb.common.graph.layout.GraphTest;
import kie.wb.common.graph.layout.Graphs;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CycleBreakerTest {

    @Test
    public void testAcyclicGraphs() {
        Graph graph = new Graph(Graphs.SimpleAcyclic);

        CycleBreaker breaker = new CycleBreaker(graph);
        Graph result = breaker.breakCycle();

        Assert.assertTrue(result.isAcyclic());
    }

    @Test
    public void testSimpleCyclicGraph() {
        Graph graph = new Graph(Graphs.SimpleCyclic);
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("C", "D");
        graph.addEdge("D", "A");

        Assert.assertFalse(graph.isAcyclic());

        CycleBreaker breaker = new CycleBreaker(graph);
        Graph result = breaker.breakCycle();

        Assert.assertTrue(result.isAcyclic());
    }

    @Test
    public void testCyclicGraph1() {
        Graph graph = new Graph(Graphs.CyclicGraph1);

        CycleBreaker breaker = new CycleBreaker(graph);
        Graph result = breaker.breakCycle();

        Assert.assertTrue(result.isAcyclic());
    }
}