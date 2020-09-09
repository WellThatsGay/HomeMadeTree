package com.chris.display;

import com.chris.tree.Node;
import com.chris.tree.Tree;
import com.lazyeye79.engine.GameEngine;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Display extends GameEngine {

  public static final int SCREEN_WIDTH = 800;
  public static final int SCREEN_HEIGHT = 400;

  private static int radius = 50;

  private Tree tree;


  public Display(int screenWidth, int screenHeight, String title) {
    super(screenWidth, screenHeight, title);
  }

  @Override
  protected boolean onUserStart() {

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

    this.blank(Color.WHITE);

//    this.drawOval(100 - this.radius/2, 100 - this.radius/2, this.radius, this.radius, Color.BLACK);
//    this.drawText(this.tree.getRoot().getValue() + "", 100, 100, Color.BLACK);

    List<Node> alreadyDrawn = new ArrayList<>();

    depthFirstSearch(alreadyDrawn, this.tree.getRoot(), 5, 0);

    return true;
  }

  private void depthFirstSearch(List<Node> alreadyDrawn, Node start, int horizontalPos, int level) {

    if (!alreadyDrawn.contains(start)) {
      int x = calculateX(horizontalPos);
      int y = calculateY(level);
      drawNode(start, x, y);
      alreadyDrawn.add(start);
      if (start.getParent() != null) {
        boolean rightChild = start.getValue() > start.getParent().getValue();
        int mx;
        if (rightChild) {
          mx = x;
        } else {
          mx = x + radius;
        }
        int my = y;
        int px;
        if (rightChild) {
          px = calculateX(horizontalPos-(4 - level));
        } else {
          px = calculateX(horizontalPos+(4 - level)) + radius;
        }
        int py = calculateY(level-1) + radius;
        this.drawLine(mx, my, px, py, Color.BLACK);
      }
    }
    if (start.isLeaf()) {
      return;
    }
    if (start.getLeft() != null) {
      depthFirstSearch(alreadyDrawn, start.getLeft(), horizontalPos-(4 - level), level+1);
    }
    if (start.getRight() != null) {
      depthFirstSearch(alreadyDrawn, start.getRight(), horizontalPos+(4 - level), level+1);
    }
  }

  private int calculateX(int horizontalPos) {
    return horizontalPos * 50 + 25;
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

  }
}
