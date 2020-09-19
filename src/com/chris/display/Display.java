package com.chris.display;

import com.chris.tree.Node;
import com.chris.tree.Tree;
import com.lazyeye79.engine.GameEngine;

import java.awt.Color;
import java.awt.event.KeyEvent;

public class Display extends GameEngine {

  public static final int SCREEN_WIDTH = 800;
  public static final int SCREEN_HEIGHT = 400;

  private static int radius = 50;

  private Tree tree;

  private String typedValue;

  private boolean debug = false;


  public Display(int screenWidth, int screenHeight, String title) {
    super(screenWidth, screenHeight, title);
  }

  @Override
  protected boolean onUserStart() {

    this.typedValue = "";

    tree = new Tree();
    tree.add(24);
    tree.add(13);
    tree.add(28);
    tree.add(17);
    tree.add(25);
    tree.add(31);
    tree.add(14);
    tree.add(19);
    tree.add(27);
    tree.add(28);
    tree.add(26);

    return true;
  }

  @Override
  protected void onUserStop() {
//    System.out.println("Goodbye");
  }

  @Override
  protected boolean onUserUpdate(float v) {

    // Blank the screen before redrawing
    this.blank(Color.WHITE);

    // Clear the typed value if escaped is pressed
    if (this.keyPressed(KeyEvent.VK_ESCAPE)) {
      this.typedValue = "";
    }
    if (this.keyReleased(KeyEvent.VK_E)) {
      int val = Integer.parseInt(this.typedValue);
      tree.add(val);
      this.typedValue = "";
    } else if (this.keyReleased(KeyEvent.VK_R)) {
      int val = Integer.parseInt(this.typedValue);
      tree.remove(val);
      this.typedValue = "";
      this.debug = true;
    }

    if (this.keyTyped()) {
      char keyTyped = this.getKeyTyped();
      this.typedValue += keyTyped;
      this.typedValue = this.typedValue.replaceAll("[^0-9]+", "");
    }

    if (!this.typedValue.isEmpty()) {
      this.drawText(this.typedValue, 10, 10, Color.BLACK);
    }

//    this.drawOval(100 - this.radius/2, 100 - this.radius/2, this.radius, this.radius, Color.BLACK);
//    this.drawText(this.tree.getRoot().getValue() + "", 100, 100, Color.BLACK);

    depthFirstSearch(this.tree.getRoot(), 0, false, 0, 0, 0);

    return true;
  }

  // Pass -1 for root node side
  private void depthFirstSearch(Node start, int level, boolean youAreRightChild, int parentSpace, int px, int py) {

    if (this.debug) {
      System.out.println("debugging");
      this.debug = false;
    }

    // Calculate our space
    int ourSpace = parentSpace * 2 + (youAreRightChild ? 1 : 0);
    // Calculate spaces, max is 2^level
    int spaces = (int) Math.pow(2, level);
    // Get the spacing of each space, dividing the screen evenly
    int screenSpacing = SCREEN_WIDTH / spaces;
    // Our starting left position
    int leftSide = screenSpacing * (ourSpace);
    // Our start right position
    int rightSide = screenSpacing * (ourSpace + 1);
    // Draw in the middle of the left and right positions
    int x = ((leftSide + rightSide) / 2);
    int y = calculateY(level);
    if (!start.isLeaf()) {
      if (start.getLeft() != null) {
        depthFirstSearch(start.getLeft(), level+1, false, ourSpace, x, y);
      }
      if (start.getRight() != null) {
        depthFirstSearch(start.getRight(), level+1, true, ourSpace, x, y);
      }
    }

    drawNode(start, x, y);
    // Draw line from ourselves to our parent
    // Do we have a parent to draw lines to?
    if (start.getParent() != null) {
      // Line starts at bottom center of parent
      // And goes to top center of child
      // Offset parent x start line by half of radius
      px += radius/2;
      // Offset parent y start line by the radius
      py += radius;
      // Modify our x and y to make the lines look better
      int mx = x + radius/2;
      int my = y;
      this.drawLine(mx, my, px, py, Color.BLACK);
    }
  }

  private int calculateY(int level) {
    return level * 80 + 25;
  }

  private void drawNode(Node node, int x, int y) {
    this.drawOval(x, y, radius, radius, Color.BLACK);
    this.drawText(node.getValue() + "", x + radius/2, y + radius/2, Color.BLACK);
  }


  public static void main(String[] args) {
    Display d = new Display(SCREEN_WIDTH, SCREEN_HEIGHT, "Tree");

    d.start();

/*
    Tree tree = new Tree();
    if (tree.contains(5)) {
      System.out.println("Contains 5");
    } else {
      System.out.println("Doesn't contain 5");
    }
    tree.add(5);
    if (tree.contains(5)) {
      System.out.println("Contains 5");
    } else {
      System.out.println("Doesn't contain 5");
    }
    tree.add(3);
    tree.add(8);
    tree.add(12);
    tree.add(1);
    tree.add(3);
    tree.add(9);

    if (tree.contains(12)) {
      System.out.println("Contains 12");
    } else {
      System.out.println("Doesn't contain 12");
    }

    tree.remove(12);

    if (tree.contains(12)) {
      System.out.println("Contains 12");
    } else {
      System.out.println("Doesn't contain 12");
    }
*/

  }
}
