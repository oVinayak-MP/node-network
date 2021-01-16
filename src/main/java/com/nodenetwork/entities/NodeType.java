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
package com.nodenetwork.entities;

/**
 * This enum mentions different types of node and specific features.
 *
 * @author vinayak
 */
public enum NodeType {
    COMPUTER("COMPUTER"), REPEATER("REPEATER"),BRIDGE("BRIDGE");
    String properString;

    NodeType(String string) {
        this.properString = string;
    }

    public static NodeType getNodeType(Node node) {

        if (node instanceof Computer) {
            return NodeType.COMPUTER;
        } else if (node instanceof Repeater) {
            return NodeType.REPEATER;
        } else if (node instanceof Bridge) {
            return NodeType.BRIDGE;
        }
         else if (node == null) {
            throw new NullPointerException("Node cannot be null");
        } else {
            throw new IllegalArgumentException("type '"+node.getClass()+"' is not supported" );
        }
    }

    public static NodeType getNodeType(String typeName) {

        if ("COMPUTER".equals(typeName)) {
            return NodeType.COMPUTER;
        } else if ("REPEATER".equals(typeName)) {
            return NodeType.REPEATER;
        }
         else if ("BRIDGE".equals(typeName)) {
            return NodeType.BRIDGE;
        }else if (typeName == null) {
            throw new NullPointerException("Node type  cannot be null");
        } else {
            throw new IllegalArgumentException("type '"+typeName+"' is not supported" );
        }
    }

    public String getProperString() {
        return properString;
    }

}
