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
package com.nodenetwork.controllers.handlers;

import com.nodenetwork.DTOs.MessageResponseDTO;
import com.nodenetwork.DTOs.NodePathDTO;
import com.nodenetwork.TestUtils;
import com.nodenetwork.controllers.CustomRequest;
import com.nodenetwork.controllers.exceptions.InvalidCommandSyntaxException;
import com.nodenetwork.controllers.exceptions.ItemNotFoundException;
import com.nodenetwork.services.NodeService;
import java.text.ParseException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author vinayak
 */
public class FetchRoutesCustomRequestHandlerTest {

    @Mock
    NodeService service;

    @InjectMocks
    FetchRoutesCustomRequestHandler instance;

    public FetchRoutesCustomRequestHandlerTest() {
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
     * Test of handle method, of class FetchRoutesCustomRequestHandler.
     */
    @Test
    public void testHandle() throws ParseException {
        System.out.println("handle");
        when(service.findPath("A2", "A1")).thenReturn(new NodePathDTO("A2->A4->A3->A1"));
        CustomRequest request = CustomRequest.parse(TestUtils.FIND_ROUTE_2);
        ResponseEntity response = instance.handle(request);
        assertNotNull(response);
        MessageResponseDTO exp = new MessageResponseDTO("Route is A2->A4->A3->A1");
        assertEquals(exp, (MessageResponseDTO) response.getBody());

        request = CustomRequest.parse(TestUtils.FIND_ROUTE_WRONG_1);
        try {
            response = instance.handle(request);
            fail("An Exception should be thrown");
        }
        catch (Exception ex) {
            assertTrue(ex instanceof InvalidCommandSyntaxException);
            assertEquals("Invalid Request", ex.getMessage());
        }

        request = CustomRequest.parse(TestUtils.FIND_ROUTE_WRONG_2);
        try {
            response = instance.handle(request);
            fail("An Exception should be thrown");
        }
        catch (Exception ex) {
            assertTrue(ex instanceof InvalidCommandSyntaxException);
            assertEquals("Invalid Request", ex.getMessage());
        }

        when(service.findPath("A2", "A1")).thenReturn(new NodePathDTO(""));
        request = CustomRequest.parse(TestUtils.FIND_ROUTE_2);
        try {
            response = instance.handle(request);
            fail("An Exception should be thrown");
        }
        catch (Exception ex) {
            assertTrue(ex instanceof ItemNotFoundException);
            assertEquals("Route not found", ex.getMessage());
        }
    }

}
