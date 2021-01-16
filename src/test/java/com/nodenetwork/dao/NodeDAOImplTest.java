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

import com.nodenetwork.entities.Computer;
import com.nodenetwork.entities.Node;
import com.nodenetwork.entities.NodeFactory;
import com.nodenetwork.entities.NodeType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vinayak
 */
public class NodeDAOImplTest {

    public NodeDAOImplTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of createNode method, of class NodeDAOImpl. verify only clone are
     * created
     */
    @Test
    public void testCreateNode() {
        System.out.println("createNode");
        NodeType type = NodeType.COMPUTER;
        String nodeName = "Node1";
        int nodeStrength = 5;
        Node node = new Computer(nodeName, nodeStrength);
        NodeDAOImpl instance = new NodeDAOImpl();
        Node expResult = node;
        Node result = instance.insertNode(node);
        assertEquals(expResult, result);
        assertEquals(((Computer) expResult).getStrength(), ((Computer) result).getStrength());
        assertTrue(Arrays.equals(expResult.getDirectConnections().toArray(), result.getDirectConnections().toArray()));
        assertFalse(expResult == result);
        assertFalse(expResult.getDirectConnections() == result.getDirectConnections());

    }

    /**
     * Test of getNode method, of class NodeDAOImpl. Verifies only clones are
     * provided
     */
    @Test
    public void testGetNode() {
        System.out.println("getNode");
        NodeType type = NodeType.COMPUTER;
        String nodeName = "Node1";
        int nodeStrength = 5;
        Node node = new Computer(nodeName, nodeStrength);
        NodeDAOImpl instance = new NodeDAOImpl();
        Node expResult = node;
        node.setDirectConnections(getDirectList(new String[]{"a", "b", "c"}));
        instance.insertNode(node);
        Node result = instance.getNode(nodeName);
        assertEquals(expResult, result);
        assertFalse(result == expResult);
        assertEquals(((Computer) expResult).getStrength(), ((Computer) result).getStrength());
        assertTrue(Arrays.equals(expResult.getDirectConnections().toArray(), result.getDirectConnections().toArray()));
        assertTrue(result instanceof Computer);
        assertFalse(expResult.getDirectConnections() == result.getDirectConnections());
    }

    private List<String> getDirectList(String[] directions) {
        return Arrays.asList(directions);
    }

    private List<Node> getListOfNodes() {
        List<Node> nodeList = new ArrayList<>();
        int count = 1000;
        for (int i = 0; i < 1000; i++) {
            Node temp = NodeFactory.createNode(NodeType.COMPUTER, "computer" + 1, i);
            temp.setDirectConnections(nodeList.stream().map(Node::getName).collect(Collectors.toList()));
            nodeList.add(temp);
        }

        return nodeList;
    }

    /**
     * Test of updateNode method, of class NodeDAOImpl.
     */
    @Test
    public void testUpdateNode() {
        System.out.println("updateNode");
        String nodeName = "Node1";
        int nodeStrength = 5;
        Computer node = new Computer(nodeName, nodeStrength);
        NodeDAOImpl instance = new NodeDAOImpl();
        node.setDirectConnections(getDirectList(new String[]{"a", "b", "c"}));
        instance.insertNode(node);
        Node newNode = NodeFactory.createNode(node);
        Node expResult = newNode;
        newNode.setDirectConnections(getDirectList(new String[]{"a1", "b1", "c1"}));
        ((Computer) newNode).setStrength(7);
        Node result = instance.updateNode(newNode);
        Node result2 = instance.getNode(nodeName);
        assertFalse(result == expResult);
        assertEquals(result2, result);
        assertEquals(expResult, result);
        assertEquals(((Computer) result2).getStrength(), ((Computer) result).getStrength());
        assertNotEquals(((Computer) expResult).getStrength(), ((Computer) node).getStrength());
        assertEquals(((Computer) expResult).getStrength(), 7);
        assertTrue(result instanceof Computer);
        assertFalse(expResult.getDirectConnections() == result.getDirectConnections());
        assertTrue(Arrays.equals(expResult.getDirectConnections().toArray(), result.getDirectConnections().toArray()));
        assertFalse(Arrays.equals(node.getDirectConnections().toArray(), result.getDirectConnections().toArray()));
    }

    /**
     * Test of getAllNodes method, of class NodeDAOImpl.
     */
    @Test
    public void testGetAllNodes() {
        System.out.println("getAllNodes");
        NodeDAOImpl instance = new NodeDAOImpl();
        List<Node> expResult = getListOfNodes();
        expResult.forEach(node -> instance.insertNode(node));
        List<Node> result = instance.getAllNodes();
        assertEquals(new HashSet(expResult), new HashSet(result));
        String nodeName = "Node1";
        int nodeStrength = 5;
        Node node = new Computer(nodeName, nodeStrength);
        instance.insertNode(node);
        assertEquals(new HashSet(expResult), new HashSet(result));
    }

}
