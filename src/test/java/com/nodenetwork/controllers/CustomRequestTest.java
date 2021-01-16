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
package com.nodenetwork.controllers;

import static com.nodenetwork.TestUtils.CREATE_CONNECTION;
import static com.nodenetwork.TestUtils.CREATE_CONNECTION_NEWLINE_ONLY;
import static com.nodenetwork.TestUtils.FIND_ROUTE;
import static com.nodenetwork.TestUtils.FIND_ROUTE_NEWLINE_ONLY;
import static com.nodenetwork.TestUtils.MODIFY_STRENGTH;
import static com.nodenetwork.TestUtils.WRONG_REQUEST_1;
import static com.nodenetwork.TestUtils.WRONG_REQUEST_2;
import com.nodenetwork.controllers.CustomRequest.RequestMethod;
import java.text.ParseException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.util.StringUtils;

/**
 *
 * @author vinayak
 */
public class CustomRequestTest {

    public CustomRequestTest() {
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
     * Test of parse method, of class CustomRequest.
     */
    @Test
    public void testParse() throws ParseException {
        System.out.println("parse");
        CustomRequest result = CustomRequest.parse(CREATE_CONNECTION);
        assertEquals("{\"source\" : \"A1\", \"targets\" : [\"A2\", \"A3\"]}", result.getBody());
        assertEquals(RequestMethod.CREATE, result.getRequestMethod());
        assertEquals(null, result.getQueryParam("test"));
        assertEquals("/connections", result.getEndPointString());
        int size = CREATE_CONNECTION.length();
        size -= StringUtils.countOccurrencesOf(CREATE_CONNECTION, "\r");
        size -= StringUtils.countOccurrencesOf(CREATE_CONNECTION, "\n");
        assertEquals(size, result.getSize());
        assertEquals("application/json", result.getHeader("content-type"));
        
            result = CustomRequest.parse(CREATE_CONNECTION_NEWLINE_ONLY);
        assertEquals("{\"source\" : \"A1\", \"targets\" : [\"A2\", \"A3\"]}", result.getBody());
        assertEquals(RequestMethod.CREATE, result.getRequestMethod());
        assertEquals(null, result.getQueryParam("test"));
        assertEquals("/connections", result.getEndPointString());
         size = CREATE_CONNECTION_NEWLINE_ONLY.length();
//        size -= StringUtils.countOccurrencesOf(CREATE_CONNECTION_NEWLINE_ONLY, "\r");
        size -= StringUtils.countOccurrencesOf(CREATE_CONNECTION_NEWLINE_ONLY, "\n");
        assertEquals(size, result.getSize());
        assertEquals("application/json", result.getHeader("content-type"));

        result = CustomRequest.parse(FIND_ROUTE);
        assertEquals("", result.getBody());     //should be null 
        assertEquals(RequestMethod.FETCH, result.getRequestMethod());
        assertEquals("A2", result.getQueryParam("from"));
        assertEquals("R1", result.getQueryParam("to"));
        assertEquals("/info-routes", result.getEndPointString());
        size = FIND_ROUTE.length();
        size -= StringUtils.countOccurrencesOf(FIND_ROUTE, "\r");
        size -= StringUtils.countOccurrencesOf(FIND_ROUTE, "\n");
        assertEquals(size, result.getSize());
        assertEquals(null, result.getHeader("content-type"));
        
         result = CustomRequest.parse(FIND_ROUTE_NEWLINE_ONLY);
        assertEquals("", result.getBody());     //should be null 
        assertEquals(RequestMethod.FETCH, result.getRequestMethod());
        assertEquals("A2", result.getQueryParam("from"));
        assertEquals("R1", result.getQueryParam("to"));
        assertEquals("/info-routes", result.getEndPointString());
        size = FIND_ROUTE_NEWLINE_ONLY.length();
//        size -= StringUtils.countOccurrencesOf(FIND_ROUTE_NEWLINE_ONLY, "\r");
        size -= StringUtils.countOccurrencesOf(FIND_ROUTE_NEWLINE_ONLY, "\n");
        assertEquals(size, result.getSize());
        assertEquals(null, result.getHeader("content-type"));


        result = CustomRequest.parse(MODIFY_STRENGTH);
        assertEquals("{\"value\": 2}", result.getBody());
        assertEquals(RequestMethod.MODIFY, result.getRequestMethod());
        assertEquals(null, result.getQueryParam("from"));
        assertEquals(null, result.getQueryParam("to"));
        assertEquals("/devices/A1/strength", result.getEndPointString());
        size = MODIFY_STRENGTH.length();
        size -= StringUtils.countOccurrencesOf(MODIFY_STRENGTH, "\r");
        size -= StringUtils.countOccurrencesOf(MODIFY_STRENGTH, "\n");
        assertEquals(size, result.getSize());
        assertEquals("application/json", result.getHeader("content-type"));

        try {
            CustomRequest.parse(WRONG_REQUEST_1);
            fail("An exception was not thrown");
        }
        catch (Exception ex) {
            assertTrue(ex instanceof ParseException);
        }

        try {
            CustomRequest.parse(WRONG_REQUEST_2);
            fail("An exception was not thrown");
        }
        catch (Exception ex) {
            assertTrue(ex instanceof ParseException);
        }

    }

}
