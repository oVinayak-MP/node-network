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

import com.nodenetwork.DTOs.NodeConnectionsDTO;
import com.nodenetwork.DTOs.NodeDTO;
import com.nodenetwork.DTOs.NodePathDTO;
import com.nodenetwork.DTOs.NodeStrengthDTO;
import com.nodenetwork.dao.NodeDAO;
import com.nodenetwork.entities.Computer;
import com.nodenetwork.entities.Node;
import com.nodenetwork.entities.NodeFactory;
import com.nodenetwork.entities.NodeType;
import com.nodenetwork.utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author vinayak
 */
public class NodeServiceTest {

    @Mock
    NodeDAO dao;

    @Mock
    PathFinder pathFinderImpl;
    @InjectMocks
    NodeService instance;

    public NodeServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of createNode method, of class NodeService.
     */
    @Test
    public void testCreateNode() {
        System.out.println("createNode");
        NodeDTO nodeDTO = new NodeDTO("C1", "COMPUTER");
        instance.createNode(nodeDTO);
        Node originalNode = NodeFactory.createNode(NodeType.COMPUTER, "C1");
        verify(dao, times(1)).insertNode(originalNode);
        verify(dao, times(1)).getNode("C1");
        try {
            nodeDTO = new NodeDTO(null, "COMPUTER");
            instance.createNode(nodeDTO);
            fail("Exception was not thrown");
        }
        catch (Exception ex) {
            assertTrue(ex instanceof IllegalArgumentException);
        }
        try {
            nodeDTO = new NodeDTO(null, "Different");
            instance.createNode(nodeDTO);
            fail("Exception was not thrown");
        }
        catch (Exception ex) {
            assertTrue(ex instanceof IllegalArgumentException);
        }
        when(dao.getNode("C1")).thenReturn(originalNode);
        try {
            nodeDTO = new NodeDTO("C1", "COMPUTER");
            instance.createNode(nodeDTO);
            fail("Exception was not thrown");
        }
        catch (Exception ex) {
            assertTrue(ex instanceof IllegalStateException);
        }
    }

    /**
     * Test of addConnections method, of class NodeService.
     */
    @Test
    public void testAddConnections() {
        System.out.println("addConnections");
        NodeConnectionsDTO nodeConnectionsDTO = new NodeConnectionsDTO("C1", new String[]{"C1"});
        try {

            instance.addConnections(nodeConnectionsDTO);
            fail("An exception should be thrown");
        }
        catch (Exception ex) {
            assertTrue(ex instanceof IllegalStateException);
            assertEquals("Node 'C1' not found", ex.getMessage());
        }
        when(dao.getNode("C1")).thenReturn(NodeFactory.createNode(NodeType.COMPUTER, "C1"));

        try {

            instance.addConnections(nodeConnectionsDTO);
            fail("An exception should be thrown");
        }
        catch (Exception ex) {
            assertTrue(ex instanceof IllegalArgumentException);
            assertEquals("Cannot connect device to itself", ex.getMessage());
        }

        nodeConnectionsDTO = new NodeConnectionsDTO("C1", new String[]{"C3", "C2"});
        Node originalNode = NodeFactory.createNode(NodeType.COMPUTER, "C3");
        originalNode.addDirectConnections(Arrays.asList(new String[]{"C3", "C2"}));
        try {
            instance.addConnections(nodeConnectionsDTO);

        }
        catch (Exception ex) {
            assertTrue(ex instanceof IllegalArgumentException);
            assertEquals("Devices are already connected", ex.getMessage());
        }

        originalNode = NodeFactory.createNode(NodeType.COMPUTER, "C3");
        when(dao.getNode("C2")).thenReturn(NodeFactory.createNode(NodeType.COMPUTER, "C2"));
        when(dao.getNode("C3")).thenReturn(NodeFactory.createNode(originalNode));

        nodeConnectionsDTO = new NodeConnectionsDTO("C3", new String[]{"C2", "C1"});
        try {
            instance.addConnections(nodeConnectionsDTO);
            verify(dao, times(1)).updateNode(originalNode);

        }
        catch (Exception ex) {
            fail("No exception should be thrown");
        }
    }

    /**
     * Test of modifyStrength method, of class NodeService.
     */
    @Test
    public void testModifyStrength() {
        System.out.println("modifyStrength");
        NodeStrengthDTO nodeStrengthDTO = new NodeStrengthDTO("C1", 56);
        Node originalNode = NodeFactory.createNode(NodeType.COMPUTER, "C1");
        when(dao.getNode("C1")).thenReturn(originalNode);
        instance.modifyStrength(nodeStrengthDTO);
        verify(dao, times(1)).getNode("C1");
        verify(dao, times(1)).updateNode(originalNode);
    }

    /**
     * Test of findPath method, of class NodeService.
     */
    @Test
    public void testFindPath() {
        System.out.println("findPath");
        String startNode = "C1";
        String endNode = "R1";
        when(dao.getNode("C1")).thenReturn(NodeFactory.createNode(NodeType.COMPUTER, "C1"));
        when(dao.getNode("C2")).thenReturn(NodeFactory.createNode(NodeType.COMPUTER, "C2"));
        when(dao.getNode("R1")).thenReturn(NodeFactory.createNode(NodeType.REPEATER, "R1"));
        try {
            instance.findPath(startNode, endNode);
            fail("Exception should be thrown");
        }
        catch (Exception ex) {
            assertTrue(ex instanceof IllegalArgumentException);
            assertEquals("Route cannot be calculated with repeater", ex.getMessage());
        }
        endNode = "C2";
        when(dao.getNode("C2")).thenReturn(NodeFactory.createNode(NodeType.COMPUTER, "C2"));
        Node startNodeObject = NodeFactory.createNode(NodeType.COMPUTER, startNode);
        startNodeObject.setDirectConnections(Arrays.asList(new String[]{"C2"}));
        Node endNodeObject = NodeFactory.createNode(NodeType.COMPUTER, endNode);

        when(dao.getNode(startNode)).thenReturn(startNodeObject);
        when(dao.getNode(endNode)).thenReturn(endNodeObject);

        List<Node> pathNode = new ArrayList<>();
        pathNode.add(startNodeObject);
        pathNode.add(endNodeObject);
        when(pathFinderImpl.findAPath(dao, (Computer) startNodeObject, (Computer) endNodeObject)).thenReturn(pathNode);

        NodePathDTO pathDTO = instance.findPath(startNode, endNode);
        verify(pathFinderImpl, times(1)).findAPath(dao, (Computer) NodeFactory.createNode(NodeType.COMPUTER, startNode), (Computer) NodeFactory.createNode(NodeType.COMPUTER, endNode));
        assertEquals("C1->C2", pathDTO.getPath());

        pathNode.clear();
        when(pathFinderImpl.findAPath(dao, (Computer) startNodeObject, (Computer) endNodeObject)).thenReturn(pathNode);
        pathDTO = instance.findPath(startNode, endNode);
        assertTrue(pathDTO.getPath().isEmpty());

    }

    /**
     * Test of findAll method, of class NodeService.
     */
    @Test
    public void testFindAll() {
        System.out.println("findAll");
        List<Node> list = new ArrayList<>();
        list.add(NodeFactory.createNode(NodeType.COMPUTER, "C2"));
        list.add(NodeFactory.createNode(NodeType.COMPUTER, "C1"));
        list.add(NodeFactory.createNode(NodeType.COMPUTER, "C3"));

        List<NodeDTO> expectedList = list.stream().map(x -> Utils.nodeToNodeDTO(x)).collect(Collectors.toList());
        when(dao.getAllNodes()).thenReturn(list);
        List<NodeDTO> result = instance.findAll();

        verify(dao, times(1)).getAllNodes();
        assertEquals(expectedList.size(), result.size());
        Iterator<NodeDTO> a = expectedList.iterator();
        Iterator<NodeDTO> b = result.iterator();

        while (a.hasNext()) {
            NodeDTO DTOFromA = a.next();
            NodeDTO DTOFromB = b.next();
            assertEquals(DTOFromA.getName(), DTOFromB.getName());
            assertEquals(DTOFromA.getType(), DTOFromB.getType());
        }
    }

    /**
     * Test of findNode method, of class NodeService.
     */
    @Test
    public void testFindNode() {
        System.out.println("findNode");
        String nodeName = "";
   
         when(dao.getNode(nodeName)).thenReturn(NodeFactory.createNode(NodeType.COMPUTER, "C3"));
        NodeDTO result = instance.findNode(nodeName);
        NodeDTO expResult = Utils.nodeToNodeDTO(NodeFactory.createNode(NodeType.COMPUTER, "C3"));
        assertEquals(expResult, result);
    }

}
