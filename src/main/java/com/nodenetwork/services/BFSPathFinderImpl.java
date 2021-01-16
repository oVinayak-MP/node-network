/*
 * The MIT License
 *
 * Copyright 2020 vinayak.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.nodenetwork.services;

import com.nodenetwork.dao.NodeDAO;
import com.nodenetwork.entities.Computer;
import com.nodenetwork.entities.Node;
import com.nodenetwork.entities.NodeType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import org.springframework.stereotype.Service;

/**
 * This the BFS based
 *
 *
 *
 *
 *
 * implementation of path finder and is implemented using BFS algorithm to find
 * paths
 *
 * @author vinayak
 */
@Service
public class BFSPathFinderImpl implements PathFinder {
        private static final boolean NO_STRENGTH_LOSS_TO_REPEATER =true;
    /**
     * Finds the path to starting and ending node if it exists else an empty
     * list is returned
     *
     * @param dao Data access object to get nodes and connections
     * @param start Starting Computer
     * @param end Ending Computer
     * @return
     */
    @Override
    public List<Node> findAPath(NodeDAO dao, Computer start, Computer end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("The parameters cannot be null");
        } else if (start.equals(end)) {
            List<Node> path = new ArrayList<>();
            path.add(end);
            path.add(end);
            return path;
        }
        Map<Node, ParentNodeAndDepth> visitedNodeWithDepth = new HashMap();
        Queue<Node> queue = new LinkedList<>();
        List<Node> path = new ArrayList<>();
        queue.add(start);
        int currentDepth = 0;
        visitedNodeWithDepth.put(start, new ParentNodeAndDepth(null, currentDepth));
        int currentStrength = start.getStrength();
        boolean found = false;
        while (!queue.isEmpty() && currentStrength > 0) {

            Node currentNode = queue.poll();
            int prevDepth = currentDepth;
            currentDepth = visitedNodeWithDepth.get(currentNode).getDepth();
            if (prevDepth != currentDepth) {
                currentStrength--;
            }
            
            //no signal strength reduces when it reaches to repeater
            if (NodeType.getNodeType(currentNode).equals(NodeType.REPEATER)) {     //As per test case given the repeater amplifies the  strength at the previous node
                currentStrength = (currentStrength + ((NO_STRENGTH_LOSS_TO_REPEATER)?1:0))* 2;                             //ie in all case message in repeater will reach all its niegbhors
            }
            else if(NodeType.getNodeType(currentNode).equals(NodeType.BRIDGE)){
                  currentStrength--;
            }

            if (currentStrength > 0) {
                found = checkAndMarkConnectedNodes(dao, currentNode, end, visitedNodeWithDepth, queue, currentDepth);
            }

            if (found) {
                return generatePath(visitedNodeWithDepth, end);
            }

        }
        return path;
    }
/**Checks and marks visited nodes
     * 
     *
     */
    private boolean checkAndMarkConnectedNodes(NodeDAO dao, Node currentNode, Node endNode, Map<Node, ParentNodeAndDepth> visitedNodeWithDepth, Queue queue, int currentDepth) {
        for (String connectedNodeName : currentNode.getDirectConnections()) {
            Node connectedNode = dao.getNode(connectedNodeName);
            if (visitedNodeWithDepth.containsKey(connectedNode)) { //Already visited

            } else {
                visitedNodeWithDepth.put(connectedNode, new ParentNodeAndDepth(currentNode, currentDepth + 1));
                queue.add(connectedNode);
                if (endNode.equals(connectedNode)) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<Node> generatePath(Map<Node, ParentNodeAndDepth> map, Node end) {
        List<Node> reversePath = new ArrayList<>();
        ParentNodeAndDepth currentNode = map.get(end);
        reversePath.add(end);
        while (currentNode.getParent() != null) {
            reversePath.add(currentNode.getParent());
            currentNode = map.get(currentNode.getParent());
        }
        Collections.reverse(reversePath);
        return reversePath;
    }

    private static class ParentNodeAndDepth {

        private Node parent;
        private int depth;

        public ParentNodeAndDepth(Node parent, int depth) {
            this.parent = parent;
            this.depth = depth;
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public int getDepth() {
            return depth;
        }

        public void setDepth(int depth) {
            this.depth = depth;
        }
    }

}
