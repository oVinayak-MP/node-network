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
package com.nodenetwork.controllers.handlers;

import com.nodenetwork.DTOs.MessageResponseDTO;
import com.nodenetwork.DTOs.NodePathDTO;
import com.nodenetwork.TestUtils;
import com.nodenetwork.controllers.CustomRequest;
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
public class SendMessageRequestHandlerTest {
        @Mock
    NodeService service;

    @InjectMocks
     SendMessageRequestHandler instance;
    
    public SendMessageRequestHandlerTest() {
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
     * Test of handle method, of class SendMessageRequestHandler.
     */
    @Test
    public void testHandle() throws ParseException {
        System.out.println("handle");
       String output="TEST";
       when(service.sendMessage("A3", "A4","aaaBBBcccDD")).thenReturn(new MessageResponseDTO(output));
        CustomRequest request = CustomRequest.parse(TestUtils.SEND_MESSAGE);
         ResponseEntity response = null ;
        try{
        response = instance.handle(request);
         assertNotNull(response);
        assertEquals("TEST",((MessageResponseDTO)response.getBody()).getMsg());
        }
        catch(Exception ex){
           fail();
        }
       
        
    }
    
}
