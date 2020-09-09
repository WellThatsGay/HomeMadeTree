package com.chris.tree;

public class Tree {

  private Node root;

  public Tree() {
  }

  public Tree(int val) {
    this.root = new Node(val);
  }

  public Node getRoot() {
    return this.root;
  }

  public void add(int val) {
    if (this.root == null) {
      this.root = new Node(val);
      return;
    }

    // Already have a root
    addHelper(this.root, val);
    //addHelperLoop(this.root, int val);
  }

  private void addHelperLoop(Node root, int val) {
    Node toCheck = root;
    boolean done = false;
    while (!done) {
      if (val > toCheck.getValue()) { // Greater than, Right child
        if (toCheck.getRight() == null) {
          Node newNode = new Node(val);
          toCheck.setRight(newNode);
          newNode.setParent(toCheck);
          done = true;
        } else {
          toCheck = toCheck.getRight();
        }
      }
      if (val <= toCheck.getValue()) { // Less than or equal, Left child
        if (toCheck.getLeft() == null) {
          Node newNode = new Node(val);
          toCheck.setLeft(newNode);
          newNode.setParent(toCheck);
          done = true;
        } else {
          toCheck = toCheck.getLeft();
        }
      }
    }
  }

  private void addHelper(Node currentNode, int val) {
    if (val > currentNode.getValue()) { // Greater than, Right child
      if (currentNode.getRight() == null) {
        Node newNode = new Node(val);
        currentNode.setRight(newNode);
        newNode.setParent(currentNode);
        return;
      } else {
        addHelper(currentNode.getRight(), val);
        return;
      }
    }
    if (val <= currentNode.getValue()) { // Less than or equal, Left child
      if (currentNode.getLeft() == null) {
        Node newNode = new Node(val);
        currentNode.setLeft(newNode);
        newNode.setParent(currentNode);
        return;
      } else {
        addHelper(currentNode.getLeft(), val);
        return;
      }
    }
  }


  public void remove(int val) {
    Node toFind = find(val);
    if (toFind == null) {
      return;
    }

    if (toFind.getLeft() == null) {
      if (toFind.getRight() == null) {
        // No children of the node to remove
        Node tfParent = toFind.getParent();
        // Need to determine if this is a left or right child
        if (tfParent.getValue() > toFind.getValue()) {
          toFind.getParent().setLeft(null);
        } else {
          toFind.getParent().setRight(null);
        }
      }

      // Node to remove has no left children
      // Slide right child up
      toFind.getParent().setLeft(toFind.getRight());
      toFind.getRight().setParent(toFind.getParent());
    } else {
      Node greatest = findLeftGreatest(toFind.getLeft());
      hasLeftChild(toFind, greatest);
    }

  }

  private void hasLeftChild(Node toFind, Node greatest) {
    // Set toFind's parent's right child to greatest
    Node tfParent = toFind.getParent();
    tfParent.setRight(greatest);
    greatest.setParent(tfParent);

    // Set greatest's left child to greatest's parent right child
    Node gParent = greatest.getParent();
    Node gLeftChild = greatest.getLeft();
    if (gLeftChild != null) {
      gParent.setLeft(gLeftChild);
      gLeftChild.setParent(gParent);
    }

    // Set greatest's left child to toFind's left child
    greatest.setLeft(toFind.getLeft());
    toFind.getLeft().setParent(greatest);

    // Set greatest's right child to toFind's right child
    greatest.setRight(toFind.getRight());
    if (toFind.getRight() != null) {
      toFind.getRight().setParent(greatest);
    }
  }

  // From startNode right greatest value
  // This will be the most right child (will have no with now children)
  private Node findLeftGreatest(Node startNode) {
    Node greatest = startNode;
    boolean found = false;
    while (!found) {
      if (greatest.getRight() == null) {
        return greatest;
      } else {
        greatest = greatest.getRight();
      }
    }
    return null;
  }

  private Node find(int val) {
    if (this.root == null) {
      return null;
    }
    Node toCheck = this.root;
    boolean found = false;
    while (!found) {
      if (val == toCheck.getValue()) {
        return toCheck;
      }
      if (val > toCheck.getValue()) { // Greater than, Right child
        if (toCheck.getRight() == null) {
          return null;
        } else {
          toCheck = toCheck.getRight();
          continue;
        }
      }
      if (val <= toCheck.getValue()) { // Less than or equal, Left child
        if (toCheck.getLeft() == null) {
          return null;
        } else {
          toCheck = toCheck.getLeft();
        }
      }
    }
    return null;
  }

  public boolean contains(int val) {
    return find(val) != null;
  }
}


