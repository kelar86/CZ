package home_work07;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Алексей on 05.08.2016.
 */
class GameMap extends JPanel{

    static final int MODE_PLAYER_VS_AI = 0;
    static final int MODE_PLAYER_VS_PLAYER = 1;

    private static final int EMPTY_DOT = 0;
    private static final int PLAYER_DOT = 1;
    private static final int COMP_DOT = 3;

    private static final int DRAW = 0;
    private static final int PLAYER_WIN = 2;
    private static final int AI_WIN = 3;

    private static final int DOTS_MARGIN = 4;

    private static final String DRAW_MSG = "Ничья!";
    private static final String PLAYER_WIN_MSG = "Вы выиграли!";
    private static final String AI_WIN_MSG = "Выиграл компьютер!";
    private final Font font = new Font("Times new roman", Font.BOLD, 48);


    private final BufferedImage xImg = ImageIO.read(GameMap.class.getResource("x.png"));
    private final BufferedImage oImg = ImageIO.read(GameMap.class.getResource("o.png"));
    private final BufferedImage backGround = ImageIO.read(GameMap.class.getResource("Background.jpg"));



    private boolean gameOver;
    private int gameOverState;


    private final Random rnd = new Random();
    private boolean initialize;
    private int[][] field;
    private int mapSizeX;
    private int mapSizeY;
    private int winLen;
    private int cellWidth;
    private int cellHeight;


    GameMap() throws IOException {

        setBackground(Color.WHITE);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                update(e);
            }
        });
    }

    private void update (MouseEvent e) {

        // логика игры
        if(gameOver||!initialize) return;
        int cellX = e.getX() / cellWidth;
        int cellY = e.getY() / cellHeight;

        if (!isCorrectAct(cellX,cellY)||!isEmpty(cellX, cellY)) return;
        field[cellY][cellX] = PLAYER_DOT;
        repaint();

        if (checkWin(PLAYER_DOT)) {
            gameOverState = PLAYER_WIN;
            gameOver = true;
            return;
        }

        if (isMapFull()) {
            gameOverState = DRAW;
            gameOver = true;
            return;
        }

        aiTurn();

        if (checkWin(COMP_DOT)) {
            gameOverState = AI_WIN;
            gameOver = true;
            return;
        }

        if (isMapFull()) {
            gameOverState = DRAW;
            gameOver = true;
        }

    }
    void startNewGame(int mode, int mapSizeX, int mapSizeY, int winLen) {

        this.mapSizeX = mapSizeX;
        this.mapSizeY = mapSizeY;
        this.winLen = winLen;
        field = new int[mapSizeY][mapSizeX];
        initialize = true;
        gameOver = false;
        repaint();

    }
    void showGameOver (Graphics g) {

        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 200, getWidth(), 70);
        g.setColor(Color.YELLOW);
        g.setFont(font);

        switch (gameOverState) {
            case DRAW:
                g.drawString(DRAW_MSG, 180, getHeight() / 2);
                break;
            case PLAYER_WIN:
                g.drawString(PLAYER_WIN_MSG, 90, getHeight() / 2);
                break;
            case AI_WIN:
                g.drawString(AI_WIN_MSG, 20, getHeight() / 2);
                break;
            default:
                throw new RuntimeException("Неизвестный gameOverState = " + gameOverState);
        }
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        render(g);

    }

    private void render (Graphics g) {

        if(!initialize) return;

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        cellWidth = panelWidth / mapSizeX;
        cellHeight = panelHeight / mapSizeY;


        g.drawImage(backGround, 0, 0, getWidth(), getHeight(), this);

        for (int i = 0; i <=mapSizeY ; i++) {
            int y = i * cellHeight;
            g.drawLine(0, y, panelWidth, y);
        }
        for (int i = 0; i <=mapSizeX ; i++) {
            int x = i*cellWidth;
            g.drawLine(x, 0, x, panelHeight);
        }
        for (int i = 0; i < mapSizeY ; i++) {
            for (int j = 0; j < mapSizeX; j++) {
               if (isEmpty(j,i))continue;
               if (field[i][j] == PLAYER_DOT) {
//                    g.setColor(Color.BLUE);

                   g.drawImage(xImg, j * cellWidth, i * cellHeight, cellWidth, cellHeight, this);

//                    g.drawLine(j * cellWidth ,i*cellHeight ,
//                            j*cellWidth+cellWidth, i*cellHeight+cellHeight);
//
//                    g.drawLine(j*cellWidth+cellWidth , i*cellHeight ,
//                            j*cellWidth , i*cellHeight+cellHeight);

                } else if (field[i][j] == COMP_DOT) {
//                    g.setColor(Color.BLUE);
//                    g.drawOval(j * cellWidth + DOTS_MARGIN, i * cellHeight + DOTS_MARGIN, cellWidth - DOTS_MARGIN*2, cellHeight - DOTS_MARGIN*2);
                      g.drawImage(oImg, j * cellWidth, i * cellHeight, cellWidth, cellHeight, this );
                } else {
                   throw new RuntimeException("Не корректное значение поля:  " + field[i][j]);
               }

            }
        }
        if (gameOver) showGameOver(g);
    }


    private void aiTurn() {
        int x, y;
        if(isAiCanWin())return;
        if(isPlayerCanWin())return;

        do {

            x = rnd.nextInt(mapSizeX);
            y = rnd.nextInt(mapSizeY);

        }
        while (!isEmpty(x, y) || !isCorrectAct(x, y));
        field[y][x] = COMP_DOT;
    }

    private boolean isAiCanWin() {
        for (int i = 0; i <mapSizeY ; i++) {
            for (int j = 0; j <mapSizeX ; j++) {
                if (isEmpty(j,i)) {
                    field[i][j] = COMP_DOT;
                    if (checkWin(COMP_DOT)) return true;
                    field[i][j] = EMPTY_DOT;
                }
            }
        }
        return false;
    }

    private boolean isPlayerCanWin() {
        for (int i = 0; i <mapSizeY ; i++) {
            for (int j = 0; j <mapSizeX ; j++) {

                if(isEmpty(j,i)) {
                    field [i][j] = PLAYER_DOT;
                    if (checkWin(PLAYER_DOT)) {
                        field[i][j] = COMP_DOT;
                        return true;
                    }
                    field[i][j] = EMPTY_DOT;
                }
            }
        }
        return false;
    }

    private boolean checkWin(int c) {

        for (int i = 0; i <mapSizeX ; i++) {
            for (int j = 0; j < mapSizeY; j++) {

                if (checkLine(i, j, 1, 0, winLen, c)) return true;
                if (checkLine(i, j, 1, 1, winLen, c)) return true;
                if (checkLine(i, j, 0, 1, winLen, c)) return true;
                if (checkLine(i, j, 1, -1, winLen, c)) return true;
            }

        }
        return false;
    }


    private boolean checkLine ( int x, int y, int vx, int vy, int len, int c){
        final int farX = x + (len - 1) * vx;
        final int farY = y + (len - 1) * vy;
        if (!isCorrectAct(farX, farY)) return false;
        for (int i = 0; i < len; i++) {

            if (field[y + i * vy][x + i * vx] != c) return false;

        }
        return true;
    }

    private  boolean isMapFull() {

        for (int i = 0; i < mapSizeY; i++) {

            for (int j = 0; j < mapSizeX; j++) {

                if (field[i][j] == EMPTY_DOT) return false;

            }

        }
        return true;
    }

    private boolean isCorrectAct(int x, int y) {
        return (x >= 0 && x < mapSizeX && y >= 0 && y < mapSizeY);
    }

    private boolean isEmpty(int x, int y) {return (field[y][x] == EMPTY_DOT);
    }
}


