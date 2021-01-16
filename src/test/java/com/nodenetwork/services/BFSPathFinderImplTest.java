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

import com.nodenetwork.TestUtils;
import com.nodenetwork.dao.NodeDAOImpl;
import com.nodenetwork.entities.Computer;
import com.nodenetwork.entities.Node;
import java.util.ArrayList;
import java.util.List;
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
public class BFSPathFinderImplTest {

    public BFSPathFinderImplTest() {
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
     * Test of findAPath method, of class BFSPathFinderImpl.
     */
    @Test
    public void testFindAPath() {
        System.out.println("findAPath");
        NodeDAOImpl daoInstance = new NodeDAOImpl();     //not autowired
        List<Node> nodes = TestUtils.populateAndReturnListOfNodesForTreeA(daoInstance);
        BFSPathFinderImpl instance = new BFSPathFinderImpl();

        Node start = daoInstance.getNode("C1");      //same start and end
        Node end = daoInstance.getNode("C1");
        List<Node> result = instance.findAPath(daoInstance, (Computer) start, (Computer) end);
        List<Node> expResult = new ArrayList();
        expResult.add(start);
        expResult.add(end);
        assertEquals(expResult, result);

        start = daoInstance.getNode("C1");            //C1 to C4     c1->r1->c4
        end = daoInstance.getNode("C4");
        result = instance.findAPath(daoInstance, (Computer) start, (Computer) end);
        expResult = new ArrayList();
        expResult.add(start);
        expResult.add(daoInstance.getNode("R1"));
        expResult.add(end);
        assertEquals(expResult, result);

        start = daoInstance.getNode("C3");
        end = daoInstance.getNode("C1");
        result = instance.findAPath(daoInstance, (Computer) start, (Computer) end);
        expResult = new ArrayList();
        expResult.add(start);
        expResult.add(daoInstance.getNode("R1"));
        expResult.add(end);
        assertEquals(expResult, result);

        start = daoInstance.getNode("C3");
        end = daoInstance.getNode("C5");
        result = instance.findAPath(daoInstance, (Computer) start, (Computer) end);
        expResult = new ArrayList();
        expResult.add(start);
        expResult.add(daoInstance.getNode("R1"));
        expResult.add(daoInstance.getNode("C4"));
        expResult.add(end);
        assertEquals(expResult, result);

        start = daoInstance.getNode("C1");
        end = daoInstance.getNode("C5");
        result = instance.findAPath(daoInstance, (Computer) start, (Computer) end);
        expResult = new ArrayList();
        expResult.add(start);
        expResult.add(daoInstance.getNode("R1"));
        expResult.add(daoInstance.getNode("C4"));
        expResult.add(end);
        assertEquals(expResult, result);

        start = daoInstance.getNode("C1");
        ((Computer) start).setStrength(1);                  //reduce strength this path shouldn't be accessible
        daoInstance.updateNode(start);
        end = daoInstance.getNode("C5");
        result = instance.findAPath(daoInstance, (Computer) start, (Computer) end);
        expResult = new ArrayList();
        assertEquals(expResult, result);
        
         daoInstance = new NodeDAOImpl(); 
        
        nodes = TestUtils.populateAndReturnListOfNodesForTreeB(daoInstance);
        instance = new BFSPathFinderImpl();

         start = daoInstance.getNode("A1");      
        end = daoInstance.getNode("A4");
        result = instance.findAPath(daoInstance, (Computer) start, (Computer) end);
        expResult = new ArrayList();
        expResult.add(start);
        expResult.add(daoInstance.getNode("A2"));
        expResult.add(end);     
        assertEquals(expResult, result);
        
         start = daoInstance.getNode("A1");      
        end = daoInstance.getNode("A5");
        result = instance.findAPath(daoInstance, (Computer) start, (Computer) end);
        expResult = new ArrayList();
        expResult.add(start);
        expResult.add(daoInstance.getNode("A2"));
        expResult.add(daoInstance.getNode("R1"));
        expResult.add(end);     
        assertEquals(expResult, result);
        
          start = daoInstance.getNode("A4");      
        end = daoInstance.getNode("A3");
        result = instance.findAPath(daoInstance, (Computer) start, (Computer) end);
        expResult = new ArrayList();
        expResult.add(start);
        expResult.add(daoInstance.getNode("A2"));
        expResult.add(daoInstance.getNode("A1"));
        expResult.add(end);     
        assertEquals(expResult, result);    
    }

}
