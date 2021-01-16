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

import com.nodenetwork.DTOs.MessageResponseDTO;
import com.nodenetwork.DTOs.NodeConnectionsDTO;
import com.nodenetwork.DTOs.NodeDTO;
import com.nodenetwork.DTOs.NodePathDTO;
import com.nodenetwork.DTOs.NodeStrengthDTO;
import com.nodenetwork.dao.NodeDAO;
import com.nodenetwork.entities.Computer;
import com.nodenetwork.entities.Node;
import com.nodenetwork.entities.NodeType;
import com.nodenetwork.utils.Utils;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for node
 *
 * @author vinayak
 */
@Service
public class NodeService {

    @Autowired
    NodeDAO dao;
    @Autowired
    PathFinder algo;
    
    @Autowired
    SendMessage message;

    /**
     * Create node with given DTO if it doesn't exist
     *
     * @param nodeDTO node DTO whose Node has to be persisted
     */
    public void createNode(NodeDTO nodeDTO) {
        Node node = Utils.nodeDTOToNode(nodeDTO);
        Node oldNode = dao.getNode(node.getName());
        if (oldNode != null) {
            throw new IllegalStateException("Device '" + node.getName() + "' already exists");
        }
        dao.insertNode(node);

    }

    /**
     * Method will add connections to corresponding node for the provided DTO
     *
     * @param nodeConnectionsDTO
     */
    public void addConnections(NodeConnectionsDTO nodeConnectionsDTO) {
        if (nodeConnectionsDTO.getSource() == null) {
            throw new IllegalArgumentException("Node name cannot be null");
        }
        if (nodeConnectionsDTO.getTargets() == null) {
            throw new IllegalArgumentException("Target nodes cannot be null");
        }
        Node temp = dao.getNode(nodeConnectionsDTO.getSource());
        if (temp == null) {
            throw new IllegalStateException("Node '" + nodeConnectionsDTO.getSource() + "' not found");
        }

        for (String targetNodeName : nodeConnectionsDTO.getTargets()) {
            if (temp.getDirectConnections().contains(targetNodeName)) {
                throw new IllegalArgumentException("Devices are already connected");
            }
            if (temp.getName().equals(targetNodeName)) {
                throw new IllegalArgumentException("Cannot connect device to itself");
            }
        }
        temp.addDirectConnections(Arrays.asList(nodeConnectionsDTO.getTargets()));
        dao.updateNode(temp);
    }

    /**
     * Modify strength of the persisted Node
     *
     * @param nodeStrength
     */
    public void modifyStrength(NodeStrengthDTO nodeStrength) {
        if (nodeStrength.getNodeName() == null) {
            throw new IllegalArgumentException("Node name cannot be null");
        }
        if (nodeStrength.getStrength() == null || nodeStrength.getStrength() < 0) {
            throw new IllegalArgumentException("value should be an integer");
        }
        Node temp = dao.getNode(nodeStrength.getNodeName());
        if (!NodeType.getNodeType(temp).equals(NodeType.COMPUTER)) {
            throw new IllegalArgumentException("Node is not an Computer");
        }
        ((Computer) temp).setStrength(nodeStrength.getStrength());
        dao.updateNode(temp);
    }

    /**
     * Finds the path between start and end node and returns it in NodePathDTO
     *
     * @param startNodeName starting nodename
     * @param endNodeName ending nodename
     * @return path in NodePathDTO
     */
    public NodePathDTO findPath(String startNodeName, String endNodeName) {
        Node startNode = dao.getNode(startNodeName);
        if (startNode == null) {
            throw new IllegalArgumentException("Node '" + startNodeName + "' not found");
        }
        Node endNode = dao.getNode(endNodeName);
        if (endNode == null) {
            throw new IllegalArgumentException("Node '" + endNodeName + "' not found");
        }

        if (!(NodeType.getNodeType(endNode).equals(NodeType.COMPUTER) && NodeType.getNodeType(startNode).equals(NodeType.COMPUTER))) {
            throw new IllegalArgumentException("Route cannot be calculated with repeater");
        }
        List<Node> path = algo.findAPath(dao, (Computer) startNode, (Computer) endNode);

        return Utils.nodePathToTONodePathDTO(path);
    }
    
    
    
    
        /**
     * Finds the path between start and end node and returns it in NodePathDTO
     *
     * @param startNodeName starting nodename
     * @param endNodeName ending nodename
     * @return path in MessageResponseDTO
     */
    public MessageResponseDTO sendMessage(String startNodeName, String endNodeName,String Message) {
        Node startNode = dao.getNode(startNodeName);
        if (startNode == null) {
            throw new IllegalArgumentException("Node '" + startNodeName + "' not found");
        }
        Node endNode = dao.getNode(endNodeName);
        if (endNode == null) {
            throw new IllegalArgumentException("Node '" + endNodeName + "' not found");
        }

        if (!(NodeType.getNodeType(endNode).equals(NodeType.COMPUTER) && NodeType.getNodeType(startNode).equals(NodeType.COMPUTER))) {
            throw new IllegalArgumentException("Route cannot be calculated with repeater");
        }
        List<Node> path = algo.findAPath(dao, (Computer) startNode, (Computer) endNode);
        String output=message.SendMessage(path, Message);
        if(path.isEmpty())output="No path found to send Message";
        
        return new MessageResponseDTO(output);
    }

    /**
     * Provides all nodes persisted in the application
     *
     * @return list of node DTO's
     */
    public List<NodeDTO> findAll() {
        List<Node> find = dao.getAllNodes();
        List<NodeDTO> returnList = find.stream().map(x -> Utils.nodeToNodeDTO(x)).collect(Collectors.toList());
        return returnList;
    }

    /**
     * Provides NodeDTO for the node else returns null if it doesn't exist
     *
     * @param nodeName Name of the node
     * @return list of node DTO's
     */
    public NodeDTO findNode(String nodeName) {
        Node node = dao.getNode(nodeName);
        if (node == null) {
            return null;
        } else {
            return Utils.nodeToNodeDTO(node);
        }
    }
}
