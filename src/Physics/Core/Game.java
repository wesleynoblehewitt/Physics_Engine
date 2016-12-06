package Physics.Core;

import Physics.Mathematics.Constants;

import javax.swing.*;
import java.awt.*;


import static Physics.Mathematics.Constants.worldDimensions;

class Game extends JFrame {

    private Scene scene = new Scene();
    private float alpha = 0;


    Game(String title){
        setupGame(title);
    }

    private void setupGame(String title){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(title);
        getContentPane().add(new GamePanel("Game"), BorderLayout.CENTER);
        pack();
        setResizable(false);
        setVisible(true);
    }

    void run(){
        final float delta = Constants.delta;
        float accumulator = 0;

        float frameStart = System.currentTimeMillis();

        while(true){
            final float currentTime = System.currentTimeMillis();

            accumulator += currentTime - frameStart;
            frameStart = currentTime;

            if(accumulator > 0.2)
                accumulator = 0.2f;

            while(accumulator > delta){
                scene.step();
                accumulator -= delta;
            }

            alpha = accumulator/delta;
            repaint();
        }
    }

    private void renderGame(Graphics g){
        scene.render(g, alpha);
    }

    private class GamePanel extends JPanel {

        GamePanel(String tooltip){
            setupGame(tooltip);
        }

        private void setupGame(String tooltip){
            setToolTipText(tooltip);
            setPreferredSize(worldDimensions);
            setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            renderGame(g);
        }
    }
}