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
import com.nodenetwork.controllers.exceptions.InvalidCommandSyntaxException;
import com.nodenetwork.controllers.exceptions.ItemNotFoundException;
import com.nodenetwork.utils.Utils;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Controller class for rest services
 *
 * @author vinayak
 */
@RestController
public class AjiraNetProcessController {

    private final Map<CustomRequestMapping, CustomRequestHandler> mapping = new HashMap();

    @RequestMapping(value = "/ajiranet/process", method = RequestMethod.POST,consumes = "text/plain")
    public ResponseEntity<Object> handleCustomRequest(@RequestBody String customRequestString) {
        CustomRequest customRequest;
        try {
            customRequest = CustomRequest.parse(customRequestString);
        }
        catch (ParseException ex) {
            throw new InvalidCommandSyntaxException(ex.getMessage(), ex);
        }
        CustomRequestHandler handler = getMapping(customRequest);
        if (handler == null) {
            throw new InvalidCommandSyntaxException("Invalid Request");
        }
        ResponseEntity response = handler.handle(customRequest);
        return response;
    }

    Map<CustomRequestMapping, CustomRequestHandler> getMappingMap() {                //for junits
        return mapping;
    }

    public void addMapping(CustomRequestMapping request, CustomRequestHandler handler) {
        mapping.put(request, handler);
    }

    private CustomRequestHandler getMapping(CustomRequest request) {
        CustomRequestMapping requestMapping = Utils.getCustomRequestMapping(request);
        return mapping.get(requestMapping);
    }

    @ExceptionHandler(InvalidCommandSyntaxException.class)
    public ResponseEntity<MessageResponseDTO> handleInvalidRequestException(InvalidCommandSyntaxException ex) {

        return new ResponseEntity<>(new MessageResponseDTO(ex.getMessage()), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<MessageResponseDTO> handleItemNotFoundException(ItemNotFoundException ex) {

        return new ResponseEntity<>(new MessageResponseDTO(ex.getMessage()), HttpStatus.NOT_FOUND);

    }
    
     @RequestMapping(value = "/ajiranet/process", method = RequestMethod.POST,consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE) // to work with curl command given in test case
    public ResponseEntity<Object> handleCustomRequestURLEncoded(@RequestBody String customURLEncodedRequestString) throws UnsupportedEncodingException {
         String properData = URLDecoder.decode(customURLEncodedRequestString,"UTF-8");
         if(properData.endsWith("=")){
             properData = properData.substring(0, properData.length()-1);
         }
        return handleCustomRequest(properData);
    
    }
}
