/*
 *  Licensed to GraphHopper and Peter Karich under one or more contributor
 *  license agreements. See the NOTICE file distributed with this work for 
 *  additional information regarding copyright ownership.
 * 
 *  GraphHopper licenses this file to you under the Apache License, 
 *  Version 2.0 (the "License"); you may not use this file except in 
 *  compliance with the License. You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.graphhopper.routing;

import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.storage.Graph;
import com.graphhopper.util.EdgeIterator;

/**
 * This class creates a Path from a DijkstraOneToMany node
 * <p/>
 * @author Peter Karich
 */
public class PathNative extends Path
{
    private final int[] parentNodes;
    private final int[] parentEdges;

    public PathNative( Graph g, FlagEncoder encoder, int[] parentNodes, int[] parentEdges )
    {
        super(g, encoder);
        this.parentNodes = parentNodes;
        this.parentEdges = parentEdges;
    }

    /**
     * Extracts path from two shortest-path-tree
     */
    @Override
    public Path extract()
    {
        if (endNode < 0)
            return this;

        while (true)
        {
            int edgeId = parentEdges[endNode];
            if (!EdgeIterator.Edge.isValid(edgeId))
                break;

            processEdge(edgeId, endNode);
            endNode = parentNodes[endNode];
        }
        reverseOrder();
        return setFound(true);
    }
}
