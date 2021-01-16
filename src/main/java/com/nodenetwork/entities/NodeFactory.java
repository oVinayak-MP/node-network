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
package com.nodenetwork.entities;

/**
 * The factory class to produce Nodes
 *
 * @author vinayak
 */
public class NodeFactory {

    private static final int DEFAULT_STRENGTH = 5;

    /**
     *
     * @param type Type of node being created
     * @param nodeName Name of the node being created
     * @param strength Strength of the node being created
     * @param bridgeType
     * @return
     */
    public static Node createNode(NodeType type, String nodeName, int strength,String bridgeType) {
        Node node;
        switch (type) {
            case COMPUTER:
                node = new Computer(nodeName, strength);
                break;
            case REPEATER:
                node = new Repeater(nodeName);
                break;
            case BRIDGE:
                node = new Bridge(nodeName,BridgeType.valueOf(bridgeType));
                break;
            default:
                throw new IllegalArgumentException("Create node doesn't support " + type.name());

        }
        return node;
    }

    /**
     *
     * @param type Type of node being created
     * @param nodeName Name of the node being created
     * @return
     */
    public static Node createNode(NodeType type, String nodeName,int strength) {

        return createNode(type, nodeName, strength,null);
    }
    
       /**
     *
     * @param type Type of node being created
     * @param nodeName Name of the node being created
     * @return
     */
    public static Node createNode(NodeType type, String nodeName) {

        return createNode(type, nodeName, DEFAULT_STRENGTH,null);
    }
    
    
         /**
     *
     * @param type Type of node being created
     * @param nodeName Name of the node being created
     * @param bridgeType
     * @return
     */
    public static Node createNode(NodeType type, String nodeName,String bridgeType) {

        return createNode(type, nodeName, DEFAULT_STRENGTH,bridgeType);
    }


    public static Node createNode(Node node) {
        Node newNode;
        switch (NodeType.getNodeType(node)) {
            case COMPUTER:
                Computer computer = (Computer) node;
                newNode = new Computer(computer.getName(), computer.getStrength());
                newNode.setDirectConnections(node.getDirectConnections());
                break;
            case REPEATER:
                newNode = new Repeater(node.getName());
                newNode.setDirectConnections(node.getDirectConnections());
                break;
            case BRIDGE:
                Bridge bridge = (Bridge) node;
                newNode = new Bridge(node.getName(),bridge.getTypeOfBridge());
                newNode.setDirectConnections(node.getDirectConnections());
                break;
            default:
                throw new IllegalArgumentException("Create node doesn't support " + NodeType.getNodeType(node).name());

        }
        return newNode;
    }

}
