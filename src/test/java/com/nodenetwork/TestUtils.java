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
package com.nodenetwork;

import com.nodenetwork.dao.NodeDAO;
import com.nodenetwork.entities.Node;
import com.nodenetwork.entities.NodeFactory;
import com.nodenetwork.entities.NodeType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author vinayak
 */
public class TestUtils {

    public static List<String> getDirectList(String[] directions) {
        return Arrays.asList(directions);
    }

    /**
     *     C1
     *   / \
     * C2 — R1 — C3 
     *  |   | 
     * R2 — C4 — C5
     *
     *
     */
    public static List<Node> populateAndReturnListOfNodesForTreeA(NodeDAO dao) {
        List<Node> nodeList = new ArrayList<>();

        Node c1 = NodeFactory.createNode(NodeType.COMPUTER, "C1", 2);
        Node c2 = NodeFactory.createNode(NodeType.COMPUTER, "C2", 2);
        Node c3 = NodeFactory.createNode(NodeType.COMPUTER, "C3", 2);
        Node c4 = NodeFactory.createNode(NodeType.COMPUTER, "C4", 2);
        Node c5 = NodeFactory.createNode(NodeType.COMPUTER, "C5", 2);

        Node r1 = NodeFactory.createNode(NodeType.REPEATER, "R1");
        Node r2 = NodeFactory.createNode(NodeType.REPEATER, "R2");

        c1 = dao.insertNode(c1);
        c2 = dao.insertNode(c2);
        c3 = dao.insertNode(c3);
        c4 = dao.insertNode(c4);
        c5 = dao.insertNode(c5);
        r1 = dao.insertNode(r1);
        r2 = dao.insertNode(r2);

        c1.addDirectConnections(TestUtils.getDirectList(new String[]{"C2", "R1"}));
        c1 = dao.updateNode(c1);

        c2 = dao.getNode(c2.getName());
        c2.addDirectConnections(TestUtils.getDirectList(new String[]{"R2"}));
        c2 = dao.updateNode(c2);

        r1 = dao.getNode(r1.getName());
        r1.addDirectConnections(TestUtils.getDirectList(new String[]{"C3", "C4"}));
        r1 = dao.updateNode(r1);

        r2 = dao.getNode(r2.getName());
        r2.addDirectConnections(TestUtils.getDirectList(new String[]{"C4"}));
        r2 = dao.updateNode(r2);

        c4 = dao.getNode(c4.getName());
        c4.addDirectConnections(TestUtils.getDirectList(new String[]{"C5"}));
        c4 = dao.updateNode(c4);

        nodeList.add(c1);
        nodeList.add(c2);
        nodeList.add(c3);
        nodeList.add(c4);
        nodeList.add(c5);
        nodeList.add(r1);
        nodeList.add(r2);
        return nodeList;
    }
    
       /**  tree given in test case
     *     A5
     *   / \
     * R1  A4  A6
     *  \   / 
     *   A2
     *    | 
     *    A1
     *    |
     *    A3
     *[Computer{strength=2Node{name=A1, directConnections=[A2, A3]}}, 
     * Computer{strength=5Node{name=A2, directConnections=[A1, R1, A4]}}, 
     * Computer{strength=5Node{name=A3, directConnections=[A1]}}, 
     * Computer{strength=5Node{name=A4, directConnections=[A5, A2]}}, 
     * Computer{strength=5Node{name=A5, directConnections=[A4, R1]}}, 
     * Computer{strength=5Node{name=A6, directConnections=[]}}, 
     * Repeater{Node{name=R1, directConnections=[A2, A5]}}]
     *
     */
    
    public static List<Node> populateAndReturnListOfNodesForTreeB(NodeDAO dao) {
        List<Node> nodeList = new ArrayList<>();

        Node a1 = NodeFactory.createNode(NodeType.COMPUTER, "A1", 2);
        Node a2 = NodeFactory.createNode(NodeType.COMPUTER, "A2", 5);
        Node a3 = NodeFactory.createNode(NodeType.COMPUTER, "A3", 5);
        Node a4 = NodeFactory.createNode(NodeType.COMPUTER, "A4", 5);
        Node a5 = NodeFactory.createNode(NodeType.COMPUTER, "A5", 5);       
        Node a6 = NodeFactory.createNode(NodeType.COMPUTER, "A6", 5);

        Node r1 = NodeFactory.createNode(NodeType.REPEATER, "R1");


        a1 = dao.insertNode(a1);
        a2 = dao.insertNode(a2);
        a3 = dao.insertNode(a3);
        a4 = dao.insertNode(a4);
        a5 = dao.insertNode(a5);
        a6 = dao.insertNode(a6);
        r1 = dao.insertNode(r1);

        a1.addDirectConnections(TestUtils.getDirectList(new String[]{"A2", "A3"}));
        a1 = dao.updateNode(a1);

        a2 = dao.getNode(a2.getName());
        a2.addDirectConnections(TestUtils.getDirectList(new String[]{"R1","A4"}));
        a2 = dao.updateNode(a2);

        a5 = dao.getNode(a5.getName());
        a5.addDirectConnections(TestUtils.getDirectList(new String[]{"R1", "A4"}));
        a5 = dao.updateNode(a5);

      
        nodeList.add(a1);
        nodeList.add(a2);
        nodeList.add(a3);
        nodeList.add(a4);
        nodeList.add(a5);
        nodeList.add(a6);
        nodeList.add(r1);
        return nodeList;
    }
    
    
    
    public static List<Node> populateAndReturnListOfNodesForTreeC(NodeDAO dao) {
        List<Node> nodeList = new ArrayList<>();

        Node a1 = NodeFactory.createNode(NodeType.COMPUTER, "A1", 5);
        Node a2 = NodeFactory.createNode(NodeType.BRIDGE, "A2", 5,"LOWER");
        Node a3 = NodeFactory.createNode(NodeType.COMPUTER, "A3", 5);
        Node a4 = NodeFactory.createNode(NodeType.BRIDGE, "A4", 5,"UPPER");

        a1 = dao.insertNode(a1);
        a2 = dao.insertNode(a2);
        a3 = dao.insertNode(a3);
        a4 = dao.insertNode(a4);


        a1.addDirectConnections(TestUtils.getDirectList(new String[]{"A2"}));
        a1 = dao.updateNode(a1);

        a2 = dao.getNode(a2.getName());
        a2.addDirectConnections(TestUtils.getDirectList(new String[]{"A4"}));
        a2 = dao.updateNode(a2);

        a4 = dao.getNode(a4.getName());
        a4.addDirectConnections(TestUtils.getDirectList(new String[]{ "A3"}));
        a4 = dao.updateNode(a4);

      
        nodeList.add(a1);
        nodeList.add(a2);
        nodeList.add(a3);
        nodeList.add(a4);
        return nodeList;
    }

    public static final String CREATE_CONNECTION = "CREATE /connections\r\n"
            + "content-type : application/json\r\n"
            + "\r\n"
            + "{\"source\" : \"A1\", \"targets\" : [\"A2\", \"A3\"]}";
    
     public static final String CREATE_CONNECTION_NEWLINE_ONLY= "CREATE /connections\n"
            + "content-type : application/json\n"
            + "\n"
            + "{\"source\" : \"A1\", \"targets\" : [\"A2\", \"A3\"]}";
     
     
    public static final String CREATE_CONNECTION_WRONG_1 = "CREATE /connections\r\n"
            + "content-type : application/json\r\n"
            + "\r\n"
            + "{\"source\" : \"A1\", }";
    public static final String CREATE_CONNECTION_WRONG_2 = "CREATE /connections\r\n"
            + "content-type : application/json\r\n"
            + "\r\n";

    public static final String FETCH_DEVICES = "FETCH /devices\r\n"
            + "content-type : application/json\r\n"
            + "\r\n"
            + "";
      public static final String FETCH_DEVICES_NEWLINE_ONLY = "FETCH /devices\n"
            + "content-type : application/json\r\n"
            + "\n"
            + "";

    public static final String CREATE_DEVICE = "CREATE /devices\r\n"
            + "content-type : application/json\r\n"
            + "\r\n"
            + "{\"type\" : \"COMPUTER\", \"name\" : \"A3\"}";
    
    
     public static final String CREATE_DEVICE_BRIDGE = "CREATE /devices\r\n"
            + "content-type : application/json\r\n"
            + "\r\n"
            + "{\"type\" : \"BRIDGE\", \"name\" : \"A8\",\"bridgeType\" : \"UPPER\"}";
    public static final String CREATE_DEVICE_WRONG = "CREATE /devices\r\n"
            + "content-type : application/json\r\n"
            + "\r\n"
            + "";
    public static final String CREATE_DEVICE_WRONG_2 = "CREATE /devices\r\n"
            + "content-type : application/json\r\n"
            + "\r\n"
            + "{\"type\" : \"PHONE\", \"name\" : \"A3\"}";
    
    
    public static final String SEND_MESSAGE = "SEND /devices\r\n"
            + "content-type : application/json\r\n"
            + "\r\n"
            + "{\"message\" : \"aaaBBBcccDD\", \"fromNode\" : \"A3\",\"toNode\" : \"A4\"}";

    public static final String FIND_ROUTE = "FETCH /info-routes?from=A2&to=R1\r\n"
            + "\r\n";
    public static final String FIND_ROUTE_NEWLINE_ONLY = "FETCH /info-routes?from=A2&to=R1\n";
    public static final String FIND_ROUTE_2 = "FETCH /info-routes?from=A2&to=A1\r\n"
            + "\r\n";
    public static final String FIND_ROUTE_WRONG_1 = "FETCH /info-routes?from=A2\r\n"
            + "\r\n";
    public static final String FIND_ROUTE_WRONG_2 = "FETCH /info-routes?from=a2,to\r\n"
            + "\r\n";

    public static final String MODIFY_STRENGTH = "MODIFY /devices/A1/strength\r\n"
            + "content-type : application/json\r\n"
            + "\r\n"
            + "{\"value\": 2}";
     public static final String MODIFY_STRENGTH_NEWLINE_ONLY = "MODIFY /devices/A1/strength\r\n"
            + "content-type : application/json\n"
            + "\n"
            + "{\"value\": 2}";
       public static final String MODIFY_STRENGTH_WRONG_1 = "MODIFY /devices/A1/strength\r\n"
            + "content-type : application/json\r\n"
            + "\r\n"
            + "{\"value\": \"test\"}";

    public static final String WRONG_REQUEST_1 = "WRONG /devices/A1/strength\r\n"
            + "content-type : application/json\r\n"
            + "\r\n"
            + "{\"value\": 2}";
    public static final String WRONG_REQUEST_2 = "MODIFY /devices/A1/strength\r"
            + "content-type : application/json\r\n"
            + "\r\n"
            + "{\"value\": 2}";

}
