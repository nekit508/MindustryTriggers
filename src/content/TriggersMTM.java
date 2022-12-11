package content;

import world.trigger.ExitGameTrigger;
import world.trigger.ScratchTrigger;
import world.trigger.ScriptableTrigger;

public class TriggersMTM {
    public static ExitGameTrigger exit;
    public static ScriptableTrigger scriptable;
    public static ScratchTrigger scratch;

    public static void load() {
        exit = new ExitGameTrigger("exit-game"){{
            activateRadius = 16;
        }};

        scriptable = new ScriptableTrigger("scriptable-trigger");

        scratch = new ScratchTrigger("scratch-trigger");
    }
}
