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
package com.nodenetwork.utils;

import com.nodenetwork.DTOs.BridgeNodeDTO;
import com.nodenetwork.DTOs.NodeDTO;
import com.nodenetwork.DTOs.NodePathDTO;
import com.nodenetwork.controllers.CustomRequest;
import com.nodenetwork.controllers.CustomRequestMapping;
import com.nodenetwork.entities.Node;
import com.nodenetwork.entities.NodeFactory;
import com.nodenetwork.entities.NodeType;
import java.util.Iterator;
import java.util.List;

/**
 * Contains utility functions
 *
 * @author vinayak
 */
public class Utils {

    /**Converts NodeDTo to Node
     *
     * @param nodeDTO DTO to be converted
     * @return converted Node
     */
    public static Node nodeDTOToNode(NodeDTO nodeDTO) {
        if (nodeDTO.getName() == null) {
            throw new IllegalArgumentException("Node name cannot be null");
        }
        if (nodeDTO.getType() == null) {
            throw new IllegalArgumentException("Node name cannot be null");
        }
        String bridgeType =null;
        if(NodeType.getNodeType(nodeDTO.getType()).equals(NodeType.BRIDGE) )bridgeType= ((BridgeNodeDTO) nodeDTO).getBridgeType();
        return NodeFactory.createNode(NodeType.getNodeType(nodeDTO.getType()), nodeDTO.getName(),bridgeType);
    }

    /**Converts Node to NodeDTO
     *
     * @param node Node to be converted
     * @return ConvertedDTO
     */
    public static NodeDTO nodeToNodeDTO(Node node) {

        return new NodeDTO(node.getName(), NodeType.getNodeType(node).getProperString());
    }

    /**Converts NodePath to NodePathDTO
     *
     * @param path NodePaNodePathDTO
     */
    public static NodePathDTO nodePathToTONodePathDTO(List<Node> path) {
        StringBuilder pathString = new StringBuilder();
        Iterator<Node> iterator = path.iterator();
        while (iterator.hasNext()) {
            pathString.append(iterator.next().getName());
            if (iterator.hasNext()) {
                pathString.append("->");
            }
        }
        return new NodePathDTO(pathString.toString());
    }

    /**Extracts mapping details from custom request
     *
     * @param request From which mapping has to be extracted
     * @return
     */
    public static CustomRequestMapping getCustomRequestMapping(CustomRequest request) {
        String method = request.getRequestMethod().toString();
        String topEndPoint = request.getEndPointString();
        int start=topEndPoint.indexOf("/");
        int stop = topEndPoint.indexOf("/",start+1);
        if(stop==-1){
            stop=topEndPoint.length();
        }
        topEndPoint=topEndPoint.substring(0,stop);  

        return new CustomRequestMapping(topEndPoint,method);
    }
}
