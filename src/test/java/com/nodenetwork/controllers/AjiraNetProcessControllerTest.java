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

import com.nodenetwork.DTOs.MessageResponseDTO;
import com.nodenetwork.TestUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.isA;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 *
 * @author vinayak
 */
@RunWith(SpringRunner.class)
@WebMvcTest(AjiraNetProcessController.class)
public class AjiraNetProcessControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Mock
    CustomRequestHandler handler;
    @Autowired
    AjiraNetProcessController instance;

    public AjiraNetProcessControllerTest() {
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
     * Test of handleCustomRequest method, of class AjiraNetProcessController.
     */
    @Test
    public void testHandleCustomRequest() throws Exception {
        System.out.println("handleCustomRequest");
        instance.addMapping(new CustomRequestMapping("/connections","CREATE"), handler);

        when(handler.handle(isA(CustomRequest.class))).thenReturn(new ResponseEntity<>(new MessageResponseDTO("Successfully added A1"), HttpStatus.OK));
        mockMvc.perform(MockMvcRequestBuilders.post("/ajiranet/process").contentType(MediaType.TEXT_PLAIN).content(TestUtils.CREATE_CONNECTION)).andExpect(MockMvcResultMatchers.content().json("{\"msg\": \"Successfully added A1\"}")).
                andExpect(MockMvcResultMatchers.status().isOk());
        verify(handler, times(1)).handle(isA(CustomRequest.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/ajiranet/process").contentType(MediaType.TEXT_PLAIN).content(TestUtils.WRONG_REQUEST_1)).andExpect(MockMvcResultMatchers.content().json("{\"msg\": \"Parsing request failed\"}")).
                andExpect(MockMvcResultMatchers.status().isBadRequest());
        verify(handler, times(1)).handle(isA(CustomRequest.class));

    }

}
