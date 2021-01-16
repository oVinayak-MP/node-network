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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class for cutom request sent by the client
 *
 * @author vinayak
 */
public class CustomRequest {
    private static boolean strict =false;
    private final Map<String, String> headers;
    private String body;
    private final RequestLine requestLine;
    private final int size;

    public static CustomRequest parse(String requestString) throws ParseException {
        int length = 0;
        CustomRequest request = null;
        try (InputStream requestStream = new ByteArrayInputStream(requestString.getBytes());) {
            RequestLine requestLine = new RequestLine();

            length += parseRequestLine(requestStream, requestLine);
            Map<String, String> headers = new HashMap<>();
            length += parseHeaders(requestStream, headers);
            StringBuilder body = new StringBuilder();
            length += parseBody(requestStream, body);

            request = new CustomRequest(headers, body.toString(), requestLine, length);
        }
        catch (ParseException ex) {
            throw ex;   //wrong offset
        }
        catch (IOException ex) {
            throw new ParseException("Parsing request failed due to IoException", length);   //wrong offset
        }
        catch (Exception ex) {
            throw new ParseException("Parsing request failed", length);
        }
        return request;
    }

    private static String getParsedLine(InputStream stream) throws ParseException {
        int byteRead = 0;
        boolean lineEnded = false;
        boolean errored = false;
        String lineString = null;
        try (ByteArrayOutputStream line = new ByteArrayOutputStream()) {
            while (!lineEnded) {
                byteRead = stream.read();

                if (byteRead > 0) {

                    if (byteRead == '\r') {       //standard 

                        byteRead = stream.read();

                        if (byteRead == '\n') {
                            lineEnded = true;
                        } else {
                            errored = true;
                        }

                    }else if(byteRead=='\n'){     //input from curls ends with \n
                         lineEnded = true;
                    }else {
                        line.write(byteRead);
                    }

                } else {                         //end of stream has been reached without \r or \n  .Can be cosidered as a line
                    if(strict)errored = true;
                    else{
                        lineEnded = true;
                    }
                }
                if (errored) {
                    throw new ParseException("Parsing request line failed", line.size());
                }
            }
            lineString = line.toString("UTF-8");
        }
        catch (IOException ex) {
            throw new ParseException("Parsing request line failed due to IoException", lineString.length());   //wrong offset
        }
        finally {

        }
        return lineString;
    }

    private static int parseHeaders(InputStream stream, Map<String, String> headers) throws ParseException {
        int length = 0;
        String line = getParsedLine(stream);
        length = length + line.length();                   //no validation  
        while (!line.equals("")) {
            String[] keyAndValue = line.split(":", 2);
            headers.put(keyAndValue[0].trim(), keyAndValue[1].trim());
            line = getParsedLine(stream);
        }
        return length;
    }

    private static int parseBody(InputStream stream, StringBuilder body) throws ParseException {
        int length = 0;
        int b;
        body.delete(0, body.length());
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            while ((b = stream.read()) >= 0) {
                out.write(b);
            }
            body.append(out.toString("UTF-8"));
        }
        catch (IOException ex) {
            Logger.getLogger(CustomRequest.class.getName()).log(Level.SEVERE, null, ex);
        }

        length = body.length();
        return length;
    }

    public int getSize() {
        return size;
    }

    private static int parseRequestLine(InputStream stream, RequestLine requestLine) throws ParseException {
        int length = 0;
        String line = getParsedLine(stream);
        String[] values = line.split(" ", 2);
        requestLine.setRequestMethod(RequestMethod.valueOf(values[0].trim()));
        String[] URIAndQuery = values[1].trim().split("\\?", 2);
        requestLine.setEndPoint(URIAndQuery[0]);
        if (URIAndQuery.length >= 2) {
            requestLine.setQuery(URIAndQuery[1]);
        }
        length = length + line.length();
        return length;
    }

    public String getBody() {
        return body;
    }

    public String getHeader(String headerName) {
        return headers.get(headerName);
    }

    public String getQueryParam(String paramName) {
        return this.requestLine.query.get(paramName);
    }

    private void setBody(String body) {
        this.body = body;
    }

    public static enum RequestMethod {
        CREATE, MODIFY, FETCH,SEND
    }

    public static class RequestLine {

        private RequestMethod requestMethod;
        private String endPoint;
        private Map<String, String> query = new HashMap<>();

        ;

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + Objects.hashCode(this.requestMethod);
            hash = 97 * hash + Objects.hashCode(this.endPoint);
            hash = 97 * hash + Objects.hashCode(this.query);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final RequestLine other = (RequestLine) obj;
            return true;
        }

        void setRequestMethod(RequestMethod requestMethod) {
            this.requestMethod = requestMethod;
        }

        void setEndPoint(String endPoint) {
            this.endPoint = endPoint;
        }

        void setQuery(String query) throws ParseException {

            this.query.clear();
            try {
                String[] queryParamEntry = query.split("&");
                for (String entry : queryParamEntry) {
                    String[] keyAndValue = entry.split("=");
                    this.query.put(keyAndValue[0], keyAndValue[1]);
                }
            }
            catch (Exception ex) {
                throw new ParseException("Parsing queries of request failed", 0);
            }

        }

        public RequestMethod getRequestMethod() {
            return requestMethod;
        }

        public String getEndPoint() {
            return endPoint;
        }

        public String getQueryString() {
            return query.toString();
        }
    }

    CustomRequest(Map<String, String> headers, String body, RequestLine requestLine, int size) {
        this.headers = headers;
        this.body = body;
        this.requestLine = requestLine;
        this.size = size;
    }

    public RequestMethod getRequestMethod() {
        return this.requestLine.getRequestMethod();
    }

    public String getQueryString() {
        return this.requestLine.getQueryString();
    }

    public String getEndPointString() {
        return this.requestLine.getEndPoint();
    }
}
