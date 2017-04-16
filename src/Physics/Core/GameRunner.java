package Physics.Core;

import Physics.Mathematics.Constants;
import Physics.Mathematics.Vector;
import Physics.Objects.Circle;
import Physics.Objects.Material;
import Physics.Objects.PhysicsObject;
import Physics.Objects.Polygon;
import org.newdawn.slick.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameRunner extends BasicGame {

    private Boolean run = true;
    private Scene scene = new Scene();
    private float accumulator = 0;
    private long frameStart = System.nanoTime();

    GameRunner(String title) {
        super(title);
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {

    }

    @Override
    public void update(GameContainer gameContainer, int i) throws SlickException {
        long currentTime = System.nanoTime();

        accumulator += (currentTime - frameStart) / 1000000000f;
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

    @Override
    public void mouseClicked(int button, int x, int y, int clickCount){
        Random random = new Random();
        // Add random circle on left click
        if(button == Constants.LeftClick){
            int radius = random.nextInt(10) + 5;
            PhysicsObject co = new PhysicsObject(Material.TEST , new Circle(new Vector(x, y), radius));
            scene.addObject(co);
        }

        // Add random polygon on right click
        if(button == Constants.RightClick){
            int contacts = random.nextInt(10 - 2) + 3;

            Vector[] points = new Vector[contacts];
            int dist = random.nextInt(20) + 10;
            for(int i = 0; i < contacts; i++){
                int Vx = random.nextInt(2 * dist) - dist;
                int Vy = random.nextInt(2 * dist) - dist;
                if(Vx <10 &&  Vx> -10){
                    Vx = 20;
                }
                if(Vy < 10 && Vy> -10){
                    Vy = 20;
                }
                points[i] = new Vector(Vx, Vy);
            }

            int mat = random.nextInt(4) + 1;
//			MyObject po = new MyObject(new Vectors(x, y), Material.getMaterialByID(mat), points);
//			float pi = (float) (Math.PI - (random.nextFloat() * 2 * Math.PI));
//			po.setOrientation(pi);
//			add(po);
            List<Vector> vertices = new ArrayList<>();
            vertices.add(new Vector(x - 100, y + 10));
            vertices.add(new Vector(x - 100, y - 10));
            vertices.add(new Vector(x + 100, y + 10));
            vertices.add(new Vector(x + 100, y - 10));
            PhysicsObject o = new PhysicsObject(Material.TEST, new Polygon(new Vector(x, y), vertices));
//            o.setOrientation((float) Math.PI / 2f);
            scene.addObject(o);
        }

        if(button == Constants.MiddleClick){
            scene.reset();
        }
    }
}
