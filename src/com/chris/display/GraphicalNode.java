package com.chris.display;

import com.chris.tree.Node;
import com.lazyeye79.engine.draw.Draw;
import com.lazyeye79.engine.draw.Oval;
import com.lazyeye79.engine.draw.Text;

import java.awt.*;

public class GraphicalNode extends Node implements Draw {

  private int x;
  private int y;
  private int radius;

  public GraphicalNode(int val, int x, int y, int radius) {
    super(val);
    this.x = x;
    this.y = y;
    this.radius = radius;
  }

  @Override
  public void draw(Graphics g) {
    new Oval(x - this.radius/2, y - this.radius/2, this.radius, this.radius, Color.BLACK, false).draw(g);
    new Text(this.getValue() + "", x, y, Color.BLACK);
  }

  public int getX() {
    return this.x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return this.y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getRadius() {
    return this.radius;
  }

  public void setRadius(int radius) {
    this.radius = radius;
  }
}
