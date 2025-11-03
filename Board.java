package SnakeGameJava;

import java.awt.Color;
import java.awt.Dimension; // For setPreferredSize
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

  // Variable for the scoring system
  private int score;

  public Board() {
    addKeyListener(new TAdapter());
    setBackground(Color.BLACK);
    setFocusable(true);

    // --- BONUS FIX for window size ---
    setPreferredSize(new Dimension(BOARD_SIZE, BOARD_SIZE));

    loadImages();
    initGame();
  }

  private void initGame() {
    dots = 3;

    // Initialize score
    score = 0;

    // Initialize snake position
    for (int z = 0; z < dots; z++) {
      y[z] = 50;
      x[z] = 50 - z * DOT_SIZE;
    }

    locateApple();

    // Game speed
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

      // --- Draw Score during game ---
      String scoreMsg = "Score: " + score;
      Font small = new Font("Helvetica", Font.BOLD, 14);
      g.setColor(Color.WHITE);
      g.setFont(small);
      g.drawString(scoreMsg, 10, 20);

      Toolkit.getDefaultToolkit().sync();
    } else {
      gameOver(g);
    }
  }

  private void gameOver(Graphics g) {
    String msg = "Game Over";

    // --- THIS IS THE LINE I FIXED (Removed the extra "Read:") ---
    String scoreMsg = "Score: " + score;

    Font small = new Font("Helvetica", Font.BOLD, 20);
    FontMetrics metr = getFontMetrics(small);

    g.setColor(Color.RED);
    g.setFont(small);
    g.drawString(msg, (BOARD_SIZE - metr.stringWidth(msg)) / 2, BOARD_SIZE / 2);

    // --- Draw Final Score ---
    g.setColor(Color.WHITE);
    g.drawString(scoreMsg, (BOARD_SIZE - metr.stringWidth(scoreMsg)) / 2, (BOARD_SIZE / 2) + 30);
  }

  private void loadImages() {
    // --- POTENTIAL MISTAKE #2 ---
    // If your game shows a blank screen, it's because these paths are wrong.
    // Make sure this path is 100% correct for *your* computer.
    String basePath = "/Users/mohitmac/SnakeGameJava/icons/";

    ImageIcon i1 = new ImageIcon(basePath + "apple.png");
    apple = i1.getImage();

    ImageIcon i2 = new ImageIcon(basePath + "dot.png");
    dot = i2.getImage();

    ImageIcon i3 = new ImageIcon(basePath + "head.png");
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
      score += 10; // Add to score
      locateApple();
    }
  }

  private void checkCollision() {
    // --- COLLISION LOGIC FIX ---
    // Checks if the head collides with *any* part of its body
    for (int z = dots; z > 0; z--) {
      // (Removed the 'z > 4' bug)
      if ((x[0] == x[z]) && (y[0] == y[z])) {
        inGame = false;
      }
    }

    // Check if the head hits the wall boundaries
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
      // --- GAME OVER LOGIC FIX (Correct Order) ---
      move(); // 1. Move snake to new spot
      checkCollision(); // 2. Check if new spot is a crash
      checkApple(); // 3. Check if new spot has an apple
    }
    repaint();
  }

  public class TAdapter extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
      int key = e.getKeyCode();

      // Prevents snake from turning 180 degrees into itself
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