package com.chris.tree;

public class Node {
  private int value;

  private Node parent;

  private Node left;

  private Node right;

  public Node getParent() {
    return this.parent;
  }

  public void setParent(Node parent) {
    this.parent = parent;
  }

  public Node(int val){
    this.value = val;
  }

  public boolean isLeaf() {
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
