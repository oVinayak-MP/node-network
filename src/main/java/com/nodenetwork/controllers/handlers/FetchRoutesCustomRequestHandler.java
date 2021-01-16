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
import com.nodenetwork.controllers.CustomRequest;
import com.nodenetwork.controllers.CustomRequestHandler;
import com.nodenetwork.controllers.exceptions.InvalidCommandSyntaxException;
import com.nodenetwork.controllers.exceptions.ItemNotFoundException;
import com.nodenetwork.services.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

/**
 * The controller to find the routes between nodes
 *
 * @author vinayak
 */
@Controller
public class FetchRoutesCustomRequestHandler implements CustomRequestHandler {

    @Autowired
    NodeService service;

    @Override
    public ResponseEntity<Object> handle(CustomRequest request) {
        NodePathDTO dto;
        String fromNodeName = request.getQueryParam("from");
        String toNodeName = request.getQueryParam("to");
        if (fromNodeName == null || toNodeName == null) {
            throw new InvalidCommandSyntaxException("Invalid Request");
        }
        try {

            dto = service.findPath(fromNodeName, toNodeName);
            if (dto.getPath().isEmpty()) {
                throw new ItemNotFoundException("Route not found");
            }
        }
        catch (IllegalArgumentException | IllegalStateException ex) {
            throw new InvalidCommandSyntaxException(ex.getMessage(), ex);
        }

        return new ResponseEntity<>(new MessageResponseDTO("Route is " + dto.getPath()), HttpStatus.OK);
    }

}
