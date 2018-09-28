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

package kie.wb.common.graph.layout;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GraphTest {

    @Test
    public void testSimpleAcyclicGraph() {
        Graph target = new Graph(Graphs.SimpleAcyclic);
        Assert.assertTrue(target.isAcyclic());
    }

    @Test
    public void testSimpleCyclicGraph() {
        Graph target = new Graph(Graphs.SimpleCyclic);
        Assert.assertFalse(target.isAcyclic());
    }

    @Test
    public void testAcyclicGraph2() {
        Graph target = new Graph(Graphs.AcyclicGraph1);
        Assert.assertTrue(target.isAcyclic());
    }

    @Test
    public void testCyclicGraph2() {
        Graph target = new Graph(Graphs.CyclicGraph1);
        Assert.assertFalse(target.isAcyclic());
    }
}