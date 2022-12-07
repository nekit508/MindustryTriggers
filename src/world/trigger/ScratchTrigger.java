package world.trigger;

import arc.scene.ui.layout.Table;
import world.Trigger;

public class ScratchTrigger extends Trigger {
    public ScratchTrigger(String name) {
        super(name);
    }

    @Override
    public void constructCreateTable(Table owner) {
        super.constructCreateTable(owner);
    }

    public class ScratchTriggerObject extends TriggerObject {

    }
}
