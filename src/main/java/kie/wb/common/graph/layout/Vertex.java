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

import java.util.Objects;

public final class Vertex implements Comparable<Vertex>,
                                     Cloneable {

    private final String id;
    private int median;
    private boolean isVirtual;
    private boolean isStartVertex;
    private int x;
    private int y;

    public Vertex(final String id) {
        this(id, false);
    }

    public Vertex(final String id,
                  final boolean isVirtual){
        this.id = id;
        this.isVirtual = isVirtual;
    }

    public boolean isVirtual() {
        return this.isVirtual;
    }

    public void setVirtual(final boolean virtual) {
        this.isVirtual = virtual;
    }

    public int getMedian() {
        return median;
    }

    public void setMedian(final int median) {
        this.median = median;
    }


    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public Vertex clone() {
        final Vertex clone = new Vertex(this.id);
        clone.setMedian(this.median);
        clone.setVirtual(this.isVirtual);
        clone.setX(this.x);
        clone.setY(this.y);
        return clone;
    }

    @Override
    public int compareTo(final Vertex other) {
        if (this.equals(other)) {
            return 0;
        } else if (this.getMedian() < other.getMedian()) {
            return -1;
        } else if (this.getMedian() > other.getMedian()) {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vertex vertex = (Vertex) o;
        return median == vertex.median &&
                Objects.equals(id, vertex.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, median);
    }

    public String getId() {
        return this.id;
    }

    public boolean isStartVertex() {
        return isStartVertex;
    }

    public void setStartVertex(boolean startVertex) {
        isStartVertex = startVertex;
    }
}