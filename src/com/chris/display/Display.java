package com.chris.display;

import com.chris.element.Button;
import com.chris.tree.Node;
import com.chris.tree.Tree;
import com.lazyeye79.engine.GameEngine;
import com.lazyeye79.network.Message;
import com.lazyeye79.network.client.Client;
import com.lazyeye79.network.server.Server;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Display extends GameEngine {

  private static final String NUM_REGEX = "[^0-9]+";

  public static final int SCREEN_WIDTH = 800;
  public static final int SCREEN_HEIGHT = 400;

  private static int radius = 25;
  private static float speed = 150;

  public enum State {
    SETUP, RUNNING, HOST, JOIN, CONNECTING
  }

  public enum Actions {
    ADD, REMOVE
  }

  private Client client;
  private Server server;
  private Integer port;
  private String address;

  // Buttons for navigation
  private Button offline;
  private Button host;
  private Button join;
  private Button next;

  private Tree tree;
  private int horizontalSpace;

  private float cameraXOffset;
  private float cameraYOffset;
  private float scale = 1.0f;
  private boolean center = true;

  private String typedValue;

  private State state;

  private boolean debug = false;


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

    this.state = State.SETUP;

    this.typedValue = "";

    this.offline = new Button(50, 25, 50, 20, Color.GRAY, "Offline", Color.BLACK);
    this.host = new Button(125, 25, 50, 20, Color.GRAY, "Host", Color.BLACK);
    this.join = new Button(200, 25, 50, 20, Color.GRAY, "Join", Color.BLACK);
    this.next = new Button(SCREEN_WIDTH - 100, 25, 50, 20, Color.GRAY, "Next", Color.BLACK, false, Color.DARK_GRAY);

    return true;
  }

  @Override
  protected void onUserStop() {
//    System.out.println("Goodbye");
  }

  @Override
  protected boolean onUserUpdate(float elapsedTime) {

    MouseEvent mouseClicked = this.getMouseClickedEvent();

    // Blank the screen before redrawing
    this.blank(Color.WHITE);

    if (this.state == State.RUNNING) {

      if (this.client != null) {
        while (this.client.isDataAvailable()) {
          Message received = this.client.getReceivedData();
          int payload = (int) received.getPayload();
          if (received.getId() == Actions.ADD) {
            this.tree.add(payload);
          } else {
            this.tree.remove(payload);
          }
        }
      }

      this.horizontalSpace = (int) (radius * 2 * Math.pow(2, this.tree.getLevels()) + (radius * 2 * 16));
      // Clear the typed value if escaped is pressed
      if (this.keyPressed(KeyEvent.VK_ESCAPE)) {
        this.typedValue = "";
      }
      if (!this.typedValue.isEmpty()) {
        if (this.keyReleased(KeyEvent.VK_E)) {
          int val = Integer.parseInt(this.typedValue);
          tree.add(val);
          this.typedValue = "";

          // Send data
          if (this.client != null) {
            Message<Actions> toSend = new Message<>(Actions.ADD, val);
            this.client.send(toSend);
          }

        } else if (this.keyReleased(KeyEvent.VK_R)) {
          int val = Integer.parseInt(this.typedValue);
          tree.remove(val);
          this.typedValue = "";

          // Send data
          if (this.client != null) {
            Message<Actions> toSend = new Message<>(Actions.REMOVE, val);
            this.client.send(toSend);
          }
        }
      }

      if (this.keyTyped()) {
        char keyTyped = this.getKeyTyped();
        if (keyTyped == 'z') {
          this.scale += 0.1f;
        }
        if (keyTyped == 'x') {
          this.scale -= 0.1f;
        }
        this.typedValue += keyTyped;
        this.typedValue = this.typedValue.replaceAll(NUM_REGEX, "");
      }

      if (!this.typedValue.isEmpty()) {
        this.drawText(this.typedValue, 10, 10, Color.BLACK);
      }

      // Move the camera
      if (this.keyPressed(KeyEvent.VK_RIGHT)) {
        this.cameraXOffset -= (speed * elapsedTime);
      }
      if (this.keyPressed(KeyEvent.VK_LEFT)) {
        this.cameraXOffset += (speed * elapsedTime);
      }
      if (this.keyPressed(KeyEvent.VK_UP)) {
        this.cameraYOffset += (speed * elapsedTime);
      }
      if (this.keyPressed(KeyEvent.VK_DOWN)) {
        this.cameraYOffset -= (speed * elapsedTime);
      }

      MouseEvent mr = this.getMouseReleasedEvent();
      MouseEvent md = this.getMouseDraggedEvent();
      if (md != null) {
        this.cameraXOffset = md.getX();
        this.cameraYOffset = md.getY();
      }
      if (mr != null) {
        this.resetMouseDraggedEvent();
      }

//    this.drawOval(100 - this.radius/2, 100 - this.radius/2, this.radius, this.radius, Color.BLACK);
//    this.drawText(this.tree.getRoot().getValue() + "", 100, 100, Color.BLACK);

      if (this.tree.getRoot() != null) {
        depthFirstSearch(this.tree.getRoot(), 0, false, 0, 0, 0);
      }
    } else if (this.state == State.SETUP) {

      this.draw(this.offline);
      this.draw(this.host);
      this.draw(this.join);

      if (mouseClicked != null) {
        int x = mouseClicked.getX();
        int y = mouseClicked.getY();
        if (this.offline.inBounds(x, y)) {
          this.state = State.RUNNING;
        }
        if (this.host.inBounds(x, y)) {
          this.state = State.HOST;
        }
        if (this.join.inBounds(x, y)) {
          this.state = State.JOIN;
        }
      }

      // Need to know if offline, hosting, joining
      // If offline, enter RUNNING state
      // If hosting, setup port for server, then join
      // If joining, setup address and port for client

    } else if (this.state == State.HOST) {

      if (this.typedValue.isEmpty()) {
        this.next.disable();
      } else {
        this.next.enable();
      }

      this.draw(this.next);
      if (!this.typedValue.isEmpty()) {
        this.drawText(this.typedValue, 20, 50, Color.BLACK);
      }

      if (this.keyTyped()) {
        char typedKey = this.getKeyTyped();
        this.typedValue += typedKey;
        this.typedValue = this.typedValue.replaceAll(NUM_REGEX, "");
      }

      if (mouseClicked != null) {
        if (this.next.inBounds(mouseClicked.getX(), mouseClicked.getY())) {
          this.port = Integer.parseInt(this.typedValue);
          this.typedValue = "";
          if (this.server == null) {
            this.server = new Server("Tree Server", this.port, true);
            this.server.start();
            this.state = State.JOIN;
          }
        }
      }

    } else if (this.state == State.JOIN) {
      if (this.typedValue.isEmpty()) {
        this.next.disable();
      } else {
        this.next.enable();
      }

      this.draw(this.next);
      if (!this.typedValue.isEmpty()) {
        this.drawText(this.typedValue, 20, 50, Color.BLACK);
      }

      if (this.keyTyped()) {
        char typedKey = this.getKeyTyped();
        if (typedKey == KeyEvent.VK_BACK_SPACE) {
          if (!this.typedValue.isEmpty()) {
            this.typedValue = this.typedValue.substring(0, this.typedValue.length() - 1);
          }
        } else {
          this.typedValue += typedKey;
        }
      }

      if (mouseClicked != null) {
        if (this.next.inBounds(mouseClicked.getX(), mouseClicked.getY())) {
          if (this.address == null) {
            this.address = this.typedValue;
            this.typedValue = "";
          } else if (this.port == null) {
            this.typedValue = this.typedValue.replaceAll(NUM_REGEX, "");
            if (!this.typedValue.isEmpty()) {
              this.port = Integer.parseInt(this.typedValue);
              this.typedValue = "";
            }
          } else {
            this.state = State.CONNECTING;
            this.typedValue = "";
          }
        }
      }

    } else if (this.state == State.CONNECTING) {
      if (this.address == null || this.port == null) {
        this.state = State.SETUP;
      } else {
        this.client = new Client("Tree Client", this.address, this.port);
        this.client.start();

        this.state = State.RUNNING;
      }
    }

    if (this.client != null) {
      if (this.client.getException() != null) {
        this.client.getException().printStackTrace();
        this.state = State.SETUP;
      }
    }

    return true;
  }

  // Pass -1 for root node side
  private void depthFirstSearch(Node start, int level, boolean youAreRightChild, int parentSpace, int px, int py) {

    if (this.debug) {
      this.debug = false;
    }

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
    if (start == this.tree.getRoot() && this.center) {
      this.cameraXOffset = -x/2;
      this.cameraYOffset = y;
      this.center = false;
    }
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
      // Offset parent x start line the radius
      px += radius;
      // Offset parent y start line by the radius
      py += radius*2;
      // Modify our x and y to make the lines look better
      int mx = x + radius;
      int my = y;
      this.drawLine(mx * scale + this.cameraXOffset, my * scale + this.cameraYOffset, px * scale + this.cameraXOffset, py * scale + this.cameraYOffset, Color.BLACK);
    }
  }

  private int calculateY(int level) {
    return level * (80 + (5 * this.tree.getLevels())) + 25;
  }

  private void drawNode(Node node, int x, int y) {
    this.drawOval(x * scale + this.cameraXOffset, y * scale + this.cameraYOffset, radius*2 * scale, radius*2 * scale, Color.BLACK);
    Font font = new Font("Node font", 0, (int) (12 * scale));
    this.drawText(node.getValue() + "", (int) ((x + radius) * scale + this.cameraXOffset), (int) ((y + radius) * scale + this.cameraYOffset), Color.BLACK, font);
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
