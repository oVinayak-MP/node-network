/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nodenetwork.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * This object is the parent object all nodes in the network
 *
 * @author vinayak
 */
public abstract class Node {

    private final String name;
    private List<String> directConnections;

    Node(String name) {
        this.name = name;
        directConnections = new ArrayList<>();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + Objects.hashCode(this.name);
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
        final Node other = (Node) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    public List<String> getDirectConnections() {
        return directConnections;

    }

    public void setDirectConnections(List<String> directConnections) {
        this.directConnections = Collections.unmodifiableList(directConnections);
    }

    public void addDirectConnections(List<String> directConnections) {
        List<String> newConnectionList = new ArrayList<>(getDirectConnections());
        for (String newNode : directConnections) {
            if (!newConnectionList.contains(newNode)) {
                newConnectionList.add(newNode);
            }
        }
        this.directConnections = Collections.unmodifiableList(newConnectionList);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Node{" + "name=" + name + ", directConnections=" + directConnections + '}';
    }

}
