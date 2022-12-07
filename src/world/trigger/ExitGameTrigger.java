package world.trigger;

import arc.util.Log;
import mindustry.Vars;
import mindustry.core.Logic;
import mindustry.game.Team;
import mindustry.gen.Call;
import world.Trigger;

public class ExitGameTrigger extends Trigger {
    public float activateRadius;

    public ExitGameTrigger(String name) {
        super(name);
    }

    public ExitGameTriggerObject spawn(float x, float y){
        ExitGameTriggerObject out = (ExitGameTriggerObject) createTrigger();
        out.x = x;
        out.y = y;
        return out;
    }

    public class ExitGameTriggerObject extends TriggerObject {
        public float x, y;

        @Override
        public void update() {
            float pdx = Vars.player.x  - x;
            float pdy = Vars.player.y - y;
            if (pdx * pdx + pdy * pdy <= activateRadius * activateRadius) {
                Logic.gameOver(Team.get(100));
            }
        }
    }
}
