package com.chris.server;

import com.chris.display.Display;
import com.chris.tree.Tree;
import com.lazyeye79.network.Message;
import com.lazyeye79.network.server.Packet;
import com.lazyeye79.network.server.Server;
import com.lazyeye79.network.server.ServerThread;

public class TreeServer extends Thread {

  private int port;
  private Server server;
  private Tree tree;

  private boolean atLeastOne;

  public TreeServer(int port) {
    this(port,  new Tree());
  }

  public TreeServer(int port, Tree tree) {
    this.port = port;
    this.tree = tree;
    this.atLeastOne = false;
  }

  @Override
  public void start() {
    this.server = new Server("Tree Server", this.port, false);
    this.server.start();
    super.start();
  }

  @Override
  public void run() {

    while (this.server.hasAConnection() || !this.atLeastOne) {
      Message<Display.Actions> treeMessage;
      while (this.server.hasNewConnection()) {
        if (!this.atLeastOne) {
          this.atLeastOne = true;
        }
        treeMessage = new Message<>(Display.Actions.TREE, this.tree);
        ServerThread st = this.server.getNewConnection();
        this.server.sendTo(st, treeMessage);
        System.out.println("Sending tree to: " + st.getName());
//        this.server.sendTo(this.server.getNewConnection(), treeMessage);
      }

      if (this.server.hasDataAvailable()) {
        Packet p = this.server.getReceivedData();
        Message<Display.Actions> m = p.getPayload();
        switch (m.getId()) {
          case ADD:
            int toAdd = (Integer) m.getPayload();
            this.tree.add(toAdd);
            break;
          case REMOVE:
            int toRemove = (Integer) m.getPayload();
            this.tree.remove(toRemove);
            break;
        }
        this.server.broadcast(p);
      }

    }

    if (this.server.getException() != null) {
      this.server.getException().printStackTrace();
    }
    System.out.println("Shutting down the server");
  }

  public boolean isReady() {
    return this.server.isReady();
  }


  public static void main(String args[]) {
    TreeServer ts = new TreeServer(8989);
    ts.start();
  }
}
