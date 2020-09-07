package com.chris.tree;

public class Tree {

  private Node root;

  public Tree(){
  }

  public Tree(int val){
    this.root = new Node(val);
  }

  public void add(int val){
    if (this.root == null){
      this.root = new Node(val);
      return;
    }

  }
}


