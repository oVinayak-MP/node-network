/*
 * The MIT License
 *
 * Copyright 2021 vinayak.
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
import com.nodenetwork.dao.NodeDAO;
import com.nodenetwork.dao.NodeDAOImpl;
import com.nodenetwork.entities.Node;
import java.util.ArrayList;
import java.util.List;
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
import org.mockito.MockitoAnnotations;

/**
 *
 * @author vinayak
 */
public class SendMessageDefaultImplTest {

    @Mock
    NodeService service;
    @InjectMocks
    SendMessageDefaultImpl instance;
    public SendMessageDefaultImplTest() {
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
     * Test of SendMessage method, of class SendMessageDefaultImpl.
     */
    @Test
    public void testSendMessage() {
        System.out.println("SendMessage");
        NodeDAOImpl dao = new NodeDAOImpl();     //not autowired
        List<Node> nodes = TestUtils.populateAndReturnListOfNodesForTreeC(dao);
        List<Node> path = new ArrayList<>();
        Node start = dao.getNode("A1");      //same start and end
        path.add(start);
        Node end = dao.getNode("A4");
        path.add(end);
        String response =instance.SendMessage(path, "aaAABBbb");
        assertEquals("AAAABBBB",response);
    }
    
}
