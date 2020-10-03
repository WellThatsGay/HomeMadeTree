package com.chris.display;

import com.lazyeye79.engine.draw.Draw;
import com.lazyeye79.engine.draw.Line;

import java.awt.Color;
import java.awt.Graphics;

public class GraphicalTree implements Draw {

  private GraphicalNode root;
  private int levels;

  private GraphicalNode currentNode;

  private GraphicalNode toAddOrRemove;
  private int currentLevel;
  private boolean stillAdding;

  private float scale;
  private float radius;
  private int cameraXOffset;
  private int cameraYOffset;

  public GraphicalTree() {
    this.levels = 0;
    this.stillAdding = false;
    this.scale = 1.0f;
    this.radius = 25.0f;
    this.cameraXOffset = 0;
    this.cameraYOffset = 0;
  }

  public GraphicalTree(int val) {
    this.root = new GraphicalNode(val);
    this.levels = 0;
    this.stillAdding = false;
    this.scale = 1.0f;
    this.radius = 25.0f;
    this.cameraXOffset = 0;
    this.cameraYOffset = 0;
  }

  public void add(int val) {
    if (this.stillAdding) {
      return;
    }
    if (this.root == null) {
      this.root = new GraphicalNode(val);
      return;
    }

    this.currentNode = this.root;
    this.toAddOrRemove = new GraphicalNode(val);
    this.currentLevel = 0;
    this.stillAdding = true;
  }

  public void continueAdding() {
    int compare = this.currentNode.compare(this.toAddOrRemove.getValue());
    // Left child (less than or equal)
    this.currentLevel++;
    if (compare < 1) {
      if (this.currentNode.getLeft() == null) {
        this.currentNode.setLeft(this.toAddOrRemove);
        this.stillAdding = false;
      } else {
        this.currentNode = this.currentNode.getLeft();
      }
      // Right child (greater than)
    } else {
      if (this.currentNode.getRight() == null) {
        this.currentNode.setRight(this.toAddOrRemove);
        this.stillAdding = false;
      } else {
        this.currentNode = this.currentNode.getRight();
      }
    }

    if (this.currentLevel > this.levels) {
      this.levels = this.currentLevel;
    }
  }

  public GraphicalNode getRoot() {
    return this.root;
  }

  public int getLevels() {
    return this.levels;
  }

  public GraphicalNode getCurrentNode() {
    return this.currentNode;
  }

  public boolean isStillAdding() {
    return this.stillAdding;
  }

  @Override
  public void draw(Graphics g) {
    this.depthFirstSearch(g, this.root, 0, false, 0, 0, 0);
  }
  private void depthFirstSearch(Graphics g, GraphicalNode start, int level, boolean youAreRightChild, int parentSpace, int px, int py) {

    int horizontalSpace = (int) (this.radius * Math.pow(2, this.levels) + (this.radius * 16));

    // Calculate our space
    int ourSpace = parentSpace * 2 + (youAreRightChild ? 1 : 0);
    // Calculate spaces, max is 2^level
    int spaces = (int) Math.pow(2, level);
    // Get the spacing of each space, dividing the screen evenly
    int screenSpacing = horizontalSpace / spaces;
    // Our starting left position
    int leftSide = screenSpacing * (ourSpace);
    // Our start right position
    int rightSide = screenSpacing * (ourSpace + 1);
    // Draw in the middle of the left and right positions
    int x = ((leftSide + rightSide) / 2);
    int y = calculateY(level);
    if (!start.isLeaf()) {
      if (start.getLeft() != null) {
        depthFirstSearch(g, start.getLeft(), level+1, false, ourSpace, x, y);
      }
      if (start.getRight() != null) {
        depthFirstSearch(g, start.getRight(), level+1, true, ourSpace, x, y);
      }
    }

    Color color = Color.BLACK;
    if (start == this.currentNode) {
      color = Color.RED;
    }
    start.draw(g, x, y, (int) this.radius, color);
    // Draw line from ourselves to our parent
    // Do we have a parent to draw lines to?
    if (start.getParent() != null) {
      // Line starts at bottom center of parent
      // And goes to top center of child
      // Offset parent x start line by half of radius
      px += this.radius;
      // Offset parent y start line by the radius
      py += this.radius*2;
      // Modify our x and y to make the lines look better
      int mx = (int) (x + this.radius);
      int my = y;
      new Line(mx + this.cameraXOffset, my + this.cameraYOffset, px + this.cameraXOffset, py + this.cameraYOffset, Color.BLACK).draw(g);
    }
  }

  private int calculateY(int level) {
    return level * (80) + 25;
  }
}
