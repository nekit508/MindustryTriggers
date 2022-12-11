package world.trigger;

import arc.func.Cons;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import mindustry.ui.dialogs.BaseDialog;
import ui.Dialogs;
import ui.dialogs.ScratchCanvasDialog;
import world.Trigger;

public class ScratchTrigger extends Trigger {
    public ScratchTrigger(String name) {
        super(name);
    }

    public ScratchTriggerObject spawn(float x, float y) {
        ScratchTriggerObject out = (ScratchTriggerObject) createTrigger();
        out.x = x;
        out.y = y;
        return out;
    }

    protected Cell<TextField> yF, xF;
    @Override
    public void constructCreateTable(Table owner) {
        yF = owner.field("X", (t) -> {}).center().center().pad(10);
        xF = owner.field("Y", (t) -> {}).center().center().pad(10);
        owner.row();
        owner.button("Create", () -> {
            Dialogs.createTriggerDialog.hide();
            ScratchTriggerObject obj = spawn(Integer.parseInt(xF.get().getText()), Integer.parseInt(yF.get().getText()));
            obj.add();
        }).size(100, 50).pad(10).center();
    }

    public class ScratchTriggerObject extends TriggerObject {
        @Override
        public void showConfiguration(Table owner) {
            super.showConfiguration(owner);
            owner.button("edit scratch", () -> {
                triggerConfigFragment.hideConfig();
                Dialogs.scratchCanvasDialog.show(this);
            }).fillX();
        }

        public class ScratchElement {
            public Seq<Connection> in, out;

            public Cons<ScratchElement> code;

            public ScratchElement(Cons<ScratchElement> f, int i, int o){
                this(f, 0, 0, i, o);
            }

            public ScratchElement(Cons<ScratchElement> f, int i, int o, float x, float y) {
                in = new Seq<Connection>(i);
                out = new Seq<Connection>(o);
                code = f;
            }

            public void update() {
                if (in.count(c -> c.active) == in.size) {
                    exec();
                    in.each(c -> c.tempActive = false);
                }
            }

            public void postUpdate(){
                out.each(c -> {
                    c.active = c.tempActive;
                });
            }

            public void exec() {
                code.get(this);
            }

            public class Connection {
                public boolean active = false;
                public boolean tempActive = false;
                Object data = null;

                public ScratchElement in, out;

                public Connection() {
                }

                public void delete(){
                    in.out.remove(this);
                    out.in.remove(this);
                }

                public Object getData(){
                    tempActive = false;
                    return data;
                }

                public void setData(Object d) {
                    tempActive = true;
                    data = d;
                }
            }
        }
    }
}
