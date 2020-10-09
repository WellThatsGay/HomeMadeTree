package com.chris.element;


import com.lazyeye79.engine.draw.Draw;
import com.lazyeye79.engine.draw.Rectangle;
import com.lazyeye79.engine.draw.Text;

import java.awt.Color;
import java.awt.Graphics;

public class Button implements Draw {

  private Rectangle rec;
  private Text text;
  private boolean enabled;
  private Color disabledColor;
  private Color enabledColor;

  public Button(int x, int y, int width, int height, Color recColor, String text, Color textColor) {
    this(x, y, width, height, recColor, text, textColor, true, recColor);
  }

  public Button(int x, int y, int width, int height, Color recColor, String text, Color textColor,
                boolean enabled, Color disabledColor) {
    this.rec = new Rectangle(x, y, width, height, recColor, true);
    if (!enabled) {
      this.disable();
    }
    int textX = x + 5;
    int textY = y + (height + 6)/2;
    this.text = new Text(text, textX, textY, textColor.getRGB());
    this.enabled = enabled;
    this.disabledColor = disabledColor;
    this.enabledColor = recColor;
  }

  public void disable() {
    this.enabled = false;
    this.rec.setColor(this.disabledColor);
  }

  public void enable() {
    this.enabled = true;
    this.rec.setColor(this.enabledColor);
  }

  public boolean inBounds(int x, int y) {
    return (x >= this.rec.getX() && x <= this.rec.getX() + this.rec.getWidth()
            && y >= this.rec.getY() && y <= this.rec.getY() + this.rec.getHeight()
            && this.enabled);
  }

  @Override
  public void draw(Graphics g) {
    this.rec.draw(g);
    this.text.draw(g);
  }

  public void setX(int x) {
    this.rec.setX(x);
  }

  public int getX() {
    return this.rec.getX();
  }

  public void setY(int y) {
    this.rec.setY(y);
  }

  public int getY() {
    return this.rec.getY();
  }

  public int getWidth() {
    return this.rec.getWidth();
  }

  public int getHeight() {
    return this.rec.getHeight();
  }

  public boolean isEnabled() {
    return this.enabled;
  }

  public void setText(String text) {
    int textX = rec.getX() + 5;
    int textY = rec.getY() + (rec.getHeight() + 6)/2;
    this.text = new Text(text, textX, textY, this.text.getColor().getRGB());
  }
}
