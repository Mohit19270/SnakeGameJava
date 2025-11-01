package SnakeGameJava;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

  private Image dot, head, apple;

  private final int DOT_SIZE = 10;
  private final int ALL_DOTS = 900;
  private final int[] x = new int[ALL_DOTS];
  private final int[] y = new int[ALL_DOTS];
  private int dots;
  private int apple_x;
  private int apple_y;
  private final int RAND_POS = 29;
  private final int BOARD_SIZE = 300;

  private Timer timer;
  private boolean leftDirection = false;
  private boolean rightDirection = true;
  private boolean upDirection = false;
  private boolean downDirection = false;
  private boolean inGame = true;

  public Board() {
    addKeyListener(new TAdapter());
    setBackground(Color.BLACK);
    setFocusable(true);
    

    loadImages();
    initGame();
  }

  private void initGame() {
    dots = 3;

    // Initialize snake position
    for (int z = 0; z < dots; z++) {
      y[z] = 50;
      x[z] = 50 - z * DOT_SIZE;
    }

    locateApple();

    timer = new Timer(140, this);
    timer.start();
  }

  private void locateApple() {
    int r = (int) (Math.random() * RAND_POS);
    apple_x = r * DOT_SIZE;
    r = (int) (Math.random() * RAND_POS);
    apple_y = r * DOT_SIZE;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    doDrawing(g);
  }

  private void doDrawing(Graphics g) {
    if (inGame) {
      g.drawImage(apple, apple_x, apple_y, this);

      for (int z = 0; z < dots; z++) {
        if (z == 0) {
          g.drawImage(head, x[z], y[z], this);
        } else {
          g.drawImage(dot, x[z], y[z], this);
        }
      }

      Toolkit.getDefaultToolkit().sync();
    } else {
      gameOver(g);
    }
  }

  private void gameOver(Graphics g) {
    String msg = "Game Over";
    Font small = new Font("Helvetica", Font.BOLD, 20);
    FontMetrics metr = getFontMetrics(small);

    g.setColor(Color.RED);
    g.setFont(small);
    g.drawString(msg, (BOARD_SIZE - metr.stringWidth(msg)) / 2, BOARD_SIZE / 2);
  }

  private void loadImages() {
    ImageIcon i1 = new ImageIcon("/Users/mohitmac/SnakeGameJava/icons/apple.png");
    apple = i1.getImage();

    ImageIcon i2 = new ImageIcon("/Users/mohitmac/SnakeGameJava/icons/dot.png");
    dot = i2.getImage();

    ImageIcon i3 = new ImageIcon("/Users/mohitmac/SnakeGameJava/icons/head.png");
    head = i3.getImage();
  }

  private void move() {
    for (int z = dots; z > 0; z--) {
      x[z] = x[z - 1];
      y[z] = y[z - 1];
    }

    if (leftDirection) {
      x[0] -= DOT_SIZE;
    }
    if (rightDirection) {
      x[0] += DOT_SIZE;
    }
    if (upDirection) {
      y[0] -= DOT_SIZE;
    }
    if (downDirection) {
      y[0] += DOT_SIZE;
    }
  }

  private void checkApple() {
    if ((x[0] == apple_x) && (y[0] == apple_y)) {
      dots++;
      locateApple();
    }
  }

  private void checkCollision() {
    for (int z = dots; z > 0; z--) {
      if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
        inGame = false;
      }
    }

    if (y[0] >= BOARD_SIZE) {
      inGame = false;
    }

    if (y[0] < 0) {
      inGame = false;
    }

    if (x[0] >= BOARD_SIZE) {
      inGame = false;
    }

    if (x[0] < 0) {
      inGame = false;
    }

    if (!inGame) {
      timer.stop();
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (inGame) {
      checkApple();
      checkCollision();
      move();
    }
    repaint();
  }

  public class TAdapter extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
      int key = e.getKeyCode();

      if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
        leftDirection = true;
        upDirection = false;
        downDirection = false;
      }

      if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
        rightDirection = true;
        upDirection = false;
        downDirection = false;
      }

      if ((key == KeyEvent.VK_UP) && (!downDirection)) {
        upDirection = true;
        rightDirection = false;
        leftDirection = false;
      }

      if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
        downDirection = true;
        rightDirection = false;
        leftDirection = false;
      }
    }
  }
}
