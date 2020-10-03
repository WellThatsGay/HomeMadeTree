package com.chris.display;

import com.lazyeye79.engine.draw.Draw;
import com.lazyeye79.engine.draw.Oval;
import com.lazyeye79.engine.draw.Text;

import java.awt.Color;
import java.awt.Graphics;

public class GraphicalNode {

  private int value;
  private GraphicalNode parent;
  private GraphicalNode left;
  private GraphicalNode right;

  public GraphicalNode(int value) {
    this.value = value;
  }

  public GraphicalNode(int value, GraphicalNode parent, GraphicalNode left, GraphicalNode right) {
    this.value = value;
    this.parent = parent;
    this.left = left;
    this.right = right;
  }

  // Returns -1, 0, or 1
  public int compare(int val) {
    return Integer.compare(val, this.value);
  }

  public boolean isLeaf() {
    return this.left == null && this.right == null;
  }

  public void draw(Graphics g, int x, int y, int radius, Color color) {
    g.setColor(color);
    new Oval(x - radius, y - radius, radius*2, radius*2, color, false).draw(g);
    new Text(this.getValue() + "", x, y, Color.BLACK).draw(g);
  }

  public int getValue() {
    return this.value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public GraphicalNode getParent() {
    return this.parent;
  }

  public void setParent(GraphicalNode parent) {
    this.parent = parent;
  }

  public GraphicalNode getLeft() {
    return this.left;
  }

  public void setLeft(GraphicalNode left) {
    this.left = left;
  }

  public GraphicalNode getRight() {
    return this.right;
  }

  public void setRight(GraphicalNode right) {
    this.right = right;
  }
}
