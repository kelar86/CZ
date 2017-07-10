package home_work07;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Алексей on 05.08.2016.
 */

class GameWindow extends JFrame {

    private static final int POS_Y = 100;
    private static final int POS_X = 200;
    private static final int  WINDOW_WIDTH = 507;
    private static final int WINDOW_HEIGHT = 555;


    private final StartGameWindow startGameWindow;
    private final GameMap gameMap;

    GameWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocation(POS_X, POS_Y);

        setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        setTitle("Крестики нолики");

        setResizable(false);
        startGameWindow = new StartGameWindow(this);

        JButton btnNewGame = new JButton("Новая игра");
        btnNewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                startGameWindow.setVisible(true);
            }
        });

        JButton btnExit = new JButton("Выход");
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        try {
            gameMap = new GameMap();
        }catch (Exception e){ throw new RuntimeException("Не найдена картинка " + e); }

        add(gameMap, BorderLayout.CENTER);

        JPanel panelBottom = new JPanel();
        panelBottom.setLayout(new GridLayout(1,2));
        panelBottom.add(btnNewGame);
        panelBottom.add(btnExit);
        add(panelBottom, BorderLayout.SOUTH);


        setVisible(true);
        startGameWindow.setVisible(true);
    }

    void startNewGame(int mode, int mapSizeX, int mapSizeY, int winLen) {
        gameMap.startNewGame(mode, mapSizeX, mapSizeY, winLen);
    }

}

