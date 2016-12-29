package Physics.Core;

import Physics.Mathematics.Constants;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;


class Game extends AppGameContainer {

    Game(GameRunner game) throws SlickException {
        super(game);
        setDisplayMode(Constants.worldDimensions.width, Constants.worldDimensions.height, false);
    }
}