package content;

import world.trigger.ExitGameTrigger;
import world.trigger.ScriptableTrigger;

public class TriggersMTM {
    public static ExitGameTrigger exit;
    public static ScriptableTrigger scriptable;

    public static void load() {
        exit = new ExitGameTrigger("exit-game"){{
            activateRadius = 16;
        }};

        scriptable = new ScriptableTrigger("scriptable-trigger");
    }
}
