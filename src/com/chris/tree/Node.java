package com.chris.tree;

public class Node {
    private int value;

    private Node left;

    private Node right;

    public boolean isleaf(){
        return this.left == null && this.right == null;
    }

    public int getValue() {
        return value;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }
}
