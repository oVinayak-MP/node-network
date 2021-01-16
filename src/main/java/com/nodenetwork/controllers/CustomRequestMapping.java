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

import java.util.Objects;

/**
 * The class to represent type of custom request
 *
 * @author vinayak
 */
public class CustomRequestMapping {

    private final String endPoint;    //end point should be the first end point this is to consider the path variables
    private final String methodType;

    public String getEndPoint() {
        return endPoint;
    }

    public String getMethodType() {
        return methodType;
    }

    public CustomRequestMapping(String endPoint, String methodType) {
        this.endPoint = endPoint;
        this.methodType = methodType;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 11 * hash + Objects.hashCode(this.endPoint);
        hash = 11 * hash + Objects.hashCode(this.methodType);
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
        final CustomRequestMapping other = (CustomRequestMapping) obj;
        if (!Objects.equals(this.endPoint, other.endPoint)) {
            return false;
        }
        if (!Objects.equals(this.methodType, other.methodType)) {
            return false;
        }
        return true;
    }

}
