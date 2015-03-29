import ubco.ai.games.Amazon;
import ubco.ai.games.GameClient;

import java.util.ArrayList;


public class Run {

    public static void main(String[] args) {


        //Random Player Test
        
        GamePlayer gp = new GamePlayer("99","99");

        try {
            Thread.sleep(2000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        GamePlayer gp2 = new GamePlayer("Wayne", "Wayne");
    }
}