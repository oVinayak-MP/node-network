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
import com.nodenetwork.DTOs.NodeStrengthDTO;
import com.nodenetwork.controllers.CustomRequest;
import com.nodenetwork.controllers.CustomRequestHandler;
import com.nodenetwork.controllers.exceptions.InvalidCommandSyntaxException;
import com.nodenetwork.controllers.exceptions.ItemNotFoundException;
import com.nodenetwork.services.NodeService;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

/**
 * The controller that takes modify requests
 *
 * @author vinayak
 */
@Controller
public class ModifyDevicesRequestHandler implements CustomRequestHandler {

    private final Pattern p = Pattern.compile("\\/devices\\/([A-Za-z0-9]+)\\/strength");
    @Autowired
    NodeService service;

    @Override
    public ResponseEntity<Object> handle(CustomRequest request) {
        NodeStrengthDTO dto;
        String path = request.getEndPointString();
        Matcher m = p.matcher(path);
        String nodeName;
        try {
            m.find();
            nodeName = m.group(1);
        }
        catch (IllegalArgumentException | IndexOutOfBoundsException |IllegalStateException ex) {
            throw new InvalidCommandSyntaxException(ex.getMessage(), ex);
        }
        if (service.findNode(nodeName) == null) {
            throw new ItemNotFoundException("Device Not Found");
        }
        String jsonString = request.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        try {

            Map dataMap = objectMapper.readValue(jsonString, Map.class);
            if (dataMap.get("value") instanceof String) {
                throw new InvalidCommandSyntaxException("value should be an integer");
            }
            dto = new NodeStrengthDTO(nodeName, (Integer) dataMap.get("value"));
            service.modifyStrength(dto);
        }
        catch (IllegalArgumentException | IllegalStateException ex) {
            throw new InvalidCommandSyntaxException(ex.getMessage(), ex);
        }
        catch (IOException ex) {
            throw new InvalidCommandSyntaxException("Invalid Command.", ex);
        }

        return new ResponseEntity<>(new MessageResponseDTO("Successfully defined strength"), HttpStatus.OK);
    }

}
