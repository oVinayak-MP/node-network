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
import com.nodenetwork.controllers.CustomRequest;
import com.nodenetwork.controllers.CustomRequestHandler;
import com.nodenetwork.controllers.exceptions.InvalidCommandSyntaxException;
import com.nodenetwork.services.NodeService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

/**
 * This handles to add connections to Node requests
 *
 * @author vinayak
 */
@Controller
public class CreateConnectionsRequestHandler implements CustomRequestHandler {

    @Autowired
    NodeService service;

    @Override
    public ResponseEntity<Object> handle(CustomRequest request) {
        String jsonString = request.getBody();
        ObjectMapper objectMapper = new ObjectMapper();                //can be a bean
        NodeConnectionsDTO nodeConnectionsDTO;
        try {
            nodeConnectionsDTO = objectMapper.readValue(jsonString, NodeConnectionsDTO.class);
            service.addConnections(nodeConnectionsDTO);
        }
        catch (IOException ex) {
            throw new InvalidCommandSyntaxException("Invalid command syntax", ex);
        }
        catch (IllegalArgumentException | IllegalStateException ex) {

            switch (ex.getMessage()) {                                              //abstract messages
                case "Node name cannot be null":
                case "Target nodes cannot be null":

                    throw new InvalidCommandSyntaxException("Invalid command syntax", ex);

                default:
                    throw new InvalidCommandSyntaxException(ex.getMessage(), ex);
            }
        }

        return new ResponseEntity<>(new MessageResponseDTO("Successfully connected"), HttpStatus.OK);
    }

}
