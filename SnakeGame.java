package SnakeGameJava;

import javax.swing.JFrame;

public class SnakeGame extends JFrame {
  public SnakeGame() {
    super("Snake Game");
    add(new Board());
    pack();
    setLocationRelativeTo(null);
    setSize(300, 300);
    setResizable(false);

  }

  public static void main(String[] args) {
    new SnakeGame().setVisible(true);
  }

}
