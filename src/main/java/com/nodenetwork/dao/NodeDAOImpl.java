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
package com.nodenetwork.dao;

import com.nodenetwork.entities.Node;
import com.nodenetwork.entities.NodeFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

/**
 * Memory based implementation for Node DAO
 * Uses adjacency list to represnt the data
 * This class is not thread safe
 * @author vinayak
 */
@Repository
public class NodeDAOImpl implements NodeDAO {

    private static final boolean IGNORE_IF_NOT_EXIST = true;
    private final Map<String, Node> memory = new ConcurrentHashMap();

    /**
     * Persists a node into the memory
     *
     * @param node The node to be persisted
     * @return Returns the updated node
     */
    @Override
    public Node insertNode(Node node) {
        return insertNode(node, true);
    }

    /**
     * Gets the persisted node if it exists else returns null
     *
     * @param nodeName The name of the node to be obtained
     * @return persisted Node
     */
    @Override
    public Node getNode(String nodeName) {
        Node newNode = null;
        if (nodeName != null && memory.containsKey(nodeName)) {
            newNode = NodeFactory.createNode(memory.get(nodeName));
        }
        return newNode;
    }

    /**
     * Persists the provided node. This is same as createNode
     *
     * @param node The Node to be persisted
     * @return returns the persisted node
     */
    @Override
    public Node updateNode(Node node) {
        return insertNode(node, true);
    }

    private Node insertNode(Node node, boolean updateReference) {
        Node newNode = NodeFactory.createNode(node);
        if (updateReference) {
            newNode.setDirectConnections(updateReferences(newNode, IGNORE_IF_NOT_EXIST));
        } else {
            newNode.setDirectConnections(node.getDirectConnections());
        }
        memory.put(node.getName(), newNode);
        return getNode(node.getName());
    }

    /**
     * Get all persisted nodes at the time of invoking
     *
     * @return List of persisted nodes
     */
    @Override
    public List<Node> getAllNodes() {
        return Collections.unmodifiableList(new ArrayList<>(memory.values()));
    }

    /*private
    to remove not persisted nodes from direct connection
     */
    private List<String> updateReferences(Node node, boolean ignoreIfNotExist) {
        List<String> connectionList = new ArrayList<>();
        node.getDirectConnections().forEach((String directNodeName) -> {
            if (getNode(directNodeName) == null && !ignoreIfNotExist) {
                throw new IllegalStateException("The referenced entity is not persisted");
            } else {
                connectionList.add(directNodeName);
                Node neighborNode = getNode(directNodeName);
                if (neighborNode != null && !neighborNode.getDirectConnections().contains(node.getName())) {                    //needs locking
                    List<String> modified = new ArrayList<>(neighborNode.getDirectConnections());
                    modified.add(node.getName());
                    neighborNode.setDirectConnections(modified);
                    insertNode(neighborNode, false);
                }
            }
        });
        return connectionList;
    }

}
