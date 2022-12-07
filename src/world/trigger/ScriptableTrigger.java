package world.trigger;

import arc.func.Prov;
import arc.graphics.g2d.Draw;
import arc.scene.ui.TextArea;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Table;
import arc.util.Log;
import arc.util.Strings;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.graphics.Drawf;
import ui.Dialogs;
import world.Trigger;

import java.util.concurrent.atomic.AtomicReference;

public class ScriptableTrigger extends Trigger {
    public ScriptableTrigger(String name) {
        super(name);
    }

    public ScriptableTriggerObject spawn(float x, float y, String script) {
        ScriptableTriggerObject out = (ScriptableTriggerObject) createTrigger();
        out.x = x;
        out.y = y;
        out.script = script;
        return out;
    }

    protected TextField yF, xF;
    protected TextArea sA;
    @Override
    public void constructCreateTable(Table owner) {
        owner.table(pos -> {
            xF = pos.field("X", (t) -> {}).get();
            pos.row();
            yF = pos.field("Y", (t) -> {}).get();
            pos.row();
        }).center();
        sA = owner.area("Enter script", (text) -> {}).size(500, 500).get();
        owner.button("Create", () -> {
            Dialogs.createTriggerDialog.hide();
            ScriptableTriggerObject trigger = spawn(Float.parseFloat(xF.getText()), Float.parseFloat(yF.getText()), sA.getText());
        });
    }

    public class ScriptableTriggerObject extends TriggerObject {
        public float x, y;
        public String script, preScriptStatic;
        public Prov<String> preScriptDynamic = () -> "";

        @Override
        public void update() {
            preScriptStatic = Strings.format("X = @;\nY = @;\n", x, y);

            Vars.mods.getScripts().runConsole(preScriptStatic + preScriptDynamic.get() + script);
        }

        @Override
        public void draw() {
            Drawf.circles(x, y, 10);
        }

        @Override
        public void save(Writes writes) {
            super.save(writes);
        }
    }
}
