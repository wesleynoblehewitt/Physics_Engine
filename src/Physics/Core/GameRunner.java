package Physics.Core;

import Physics.Mathematics.Constants;
import org.newdawn.slick.*;

public class GameRunner extends BasicGame {

    private Boolean run = true;
    private Scene scene = new Scene();
    private float accumulator = 0;
    private long frameStart = System.currentTimeMillis();

    GameRunner(String title) {
        super(title);
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {

    }

    @Override
    public void update(GameContainer gameContainer, int i) throws SlickException {
        long currentTime = System.currentTimeMillis();

        accumulator += (currentTime - frameStart);
        frameStart = currentTime;

        // Clamp accumulator to an arbitrary value
        // This prevents a spiral of death if the physics cannot be computed fast enough to match the dt
        if (accumulator > 0.2f) {
            accumulator = 0.2f;
        }
        float delta = Constants.delta;
        while (accumulator > delta) {
            scene.step();
            accumulator -= delta;
        }
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        g.setColor(Color.white);
        g.drawString("Game", 250, 50);
        scene.render(g);
    }
}
