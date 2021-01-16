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
import com.nodenetwork.TestUtils;
import com.nodenetwork.controllers.CustomRequest;
import com.nodenetwork.controllers.exceptions.InvalidCommandSyntaxException;
import com.nodenetwork.services.NodeService;
import java.text.ParseException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author vinayak
 */
public class CreateConnectionsRequestHandlerTest {

    @Mock
    NodeService service;

    @InjectMocks
    CreateConnectionsRequestHandler instance;

    public CreateConnectionsRequestHandlerTest() {
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
     * Test of handle method, of class CreateConnectionsRequestHandler.
     */
    @Test
    public void testHandle() throws ParseException {
        System.out.println("handle");
        CustomRequest request = CustomRequest.parse(TestUtils.CREATE_CONNECTION);
        ResponseEntity response = instance.handle(request);
        assertNotNull(response);
        MessageResponseDTO exp = new MessageResponseDTO("Successfully connected");
        assertEquals(exp, (MessageResponseDTO) response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        request = CustomRequest.parse(TestUtils.CREATE_CONNECTION_WRONG_1);
        try {
            response = instance.handle(request);
            fail("An Exception should be thrown");
        }
        catch (Exception ex) {
            assertTrue(ex instanceof InvalidCommandSyntaxException);
            assertEquals("Invalid command syntax", ex.getMessage());
        }

        request = CustomRequest.parse(TestUtils.CREATE_CONNECTION_WRONG_2);
        try {
            response = instance.handle(request);
            fail("An Exception should be thrown");
        }
        catch (Exception ex) {
            assertTrue(ex instanceof InvalidCommandSyntaxException);
            assertEquals("Invalid command syntax", ex.getMessage());
        }

    }

}
