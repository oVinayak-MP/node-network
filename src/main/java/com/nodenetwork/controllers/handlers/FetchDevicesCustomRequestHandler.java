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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nodenetwork.DTOs.MessageResponseDTO;
import com.nodenetwork.DTOs.NodeConnectionsDTO;
import com.nodenetwork.DTOs.NodeDTO;
import com.nodenetwork.DTOs.NodeListDTO;
import com.nodenetwork.controllers.CustomRequest;
import com.nodenetwork.controllers.CustomRequestHandler;
import com.nodenetwork.controllers.exceptions.InvalidCommandSyntaxException;
import com.nodenetwork.services.NodeService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

/**
 * The controller to handle fetch devices to request
 *
 * @author vinayak
 */
@Controller
public class FetchDevicesCustomRequestHandler implements CustomRequestHandler {

    @Autowired
    NodeService service;

    @Override
    public ResponseEntity<Object> handle(CustomRequest request) {
        List<NodeDTO> dto;
        try {

            dto = service.findAll();
        }
        catch (IllegalArgumentException | IllegalStateException ex) {
            throw new InvalidCommandSyntaxException(ex.getMessage(), ex);
        }

        return new ResponseEntity<>(new NodeListDTO((NodeDTO[]) dto.toArray(new NodeDTO[0])), HttpStatus.OK);
    }

}
