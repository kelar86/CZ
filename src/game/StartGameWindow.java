package home_work07;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Алексей on 05.08.2016.
 */
class StartGameWindow extends JFrame {

    private final GameWindow gameWindow;
    private static final int WINDOW_WIDTH = 350;
    private static final int WINDOW_HEIGHT = 230;
    private static final int MIN_WIN_LEN = 3;
    private static final int MIN_MAP_SIZE = 3;
    private static final int MAX_MAP_SIZE = 10;

    private JRadioButton playerVsAI;
    private JRadioButton playerVsPlayer;
    private JSlider sliderMapSize;
    private JSlider sliderWinLen;

    StartGameWindow(GameWindow gameWindow) {

        this.gameWindow = gameWindow;
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        Rectangle gameWindowBounds = gameWindow.getBounds();
        setResizable(false);
        int posX = (int) gameWindowBounds.getCenterX()-WINDOW_WIDTH / 2;
        int posY = (int) gameWindowBounds.getCenterY() - WINDOW_HEIGHT / 2;
        setLocation(posX,posY);
        setTitle("Старт новой игры");

        setLayout(new GridLayout(10,1));
        addModeControls();
        addMapControls();
        JButton buttonStart = new JButton("Начать игру");
        buttonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                pressStart();
            }
        });
        add(buttonStart);

    }

    private void addModeControls() {
       add(new JLabel("Выберите режим игры:"));


        playerVsAI = new JRadioButton("Игрок против компьютера", true);
        playerVsPlayer = new JRadioButton("Игрок против игрока");

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(playerVsAI);
        buttonGroup.add(playerVsPlayer);
        add(playerVsAI);
        add(playerVsPlayer);
    }

    private void addMapControls() {
        final String WIN_LEN_PREF = "Win len: ";
        final JLabel labelWinLen = new JLabel(WIN_LEN_PREF + MIN_WIN_LEN);


        sliderWinLen = new JSlider(MIN_WIN_LEN, MIN_MAP_SIZE, MIN_WIN_LEN);
        sliderWinLen.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                labelWinLen.setText(WIN_LEN_PREF + sliderWinLen.getValue());
            }
        });

        final String MAP_SIZE_PREF = "Map size: ";
        final JLabel labelMapSize = new JLabel(MAP_SIZE_PREF + MIN_MAP_SIZE);
        sliderMapSize = new JSlider(MIN_MAP_SIZE, MAX_MAP_SIZE, MIN_MAP_SIZE);
        sliderMapSize.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int mapSliderValue = sliderMapSize.getValue();
                labelMapSize.setText(MAP_SIZE_PREF + mapSliderValue);
                sliderWinLen.setMaximum(mapSliderValue);
            }
        });
        add(new JLabel("Выберите размер поля"));
        add(labelMapSize);
        add(sliderMapSize);
        add(new JLabel("Выберите длину победной серии"));
        add(labelWinLen);
        add(sliderWinLen);



    }
    private void pressStart() {
        int mode;
        if (playerVsAI.isSelected()) {
            mode = GameMap.MODE_PLAYER_VS_AI;
        } else if (playerVsPlayer.isSelected()) { mode = GameMap.MODE_PLAYER_VS_PLAYER; }
        else { throw new RuntimeException("Не выбрана радиокнопка");}

        int mapSize = sliderMapSize.getValue();
        setVisible(false);
        gameWindow.startNewGame(mode, mapSize, mapSize,sliderWinLen.getValue());

    }
}
