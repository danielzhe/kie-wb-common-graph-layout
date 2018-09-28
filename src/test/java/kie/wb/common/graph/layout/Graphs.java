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

public final class Graphs {



    /*
     * 2 crossings
     *       A           B
     *      /\\         /
     *     /  \ -------+ --
     *    / /-- +-----/    \
     *   / /     \          \
     *  D         E          F
     * */

    public static final String[][] SimpleAcyclic = {
            {"A", "B"},
            {"A", "C"},
            {"C", "B"}};

    public static final String[][] SimpleCyclic = {
            {"A", "B"},
            {"B", "C"},
            {"C", "A"}};

    public static final String[][] CyclicGraph1 = {
            {"A", "B"},
            {"A", "C"},
            {"C", "D"},
            {"D", "B"},
            {"G", "B"},
            {"G", "A"},
            {"G", "H"},
            {"H", "I"},
            {"I", "G"},
            {"G", "J"},
            {"G", "F"},
            {"F", "E"},
            {"E", "A"},
            {"A", "I"}
    };

    public static final String[][] AcyclicGraph1 = {
            {"A", "B"},
            {"A", "C"},
            {"C", "D"},
            {"D", "B"},
            {"G", "B"},
            {"G", "A"},
            {"G", "H"},
            {"H", "I"},
            {"G", "I"},
            {"G", "J"},
            {"G", "F"},
            {"F", "E"},
            {"E", "A"},
            {"A", "I"}
    };
}