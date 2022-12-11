package ui.dialogs;

import arc.func.Cons;
import arc.func.Func;
import arc.func.Prov;
import arc.input.KeyCode;
import arc.math.geom.Vec2;
import arc.scene.Element;
import arc.scene.event.ElementGestureListener;
import arc.scene.event.InputEvent;
import arc.scene.ui.Dialog;
import arc.scene.ui.layout.Table;
import arc.scene.ui.layout.WidgetGroup;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import ui.Dialogs;
import world.trigger.ScratchTrigger;

public class ScratchCanvasDialog extends BaseDialog {
    public View view;

    public boolean pressed = false;
    public float visualPressed = 0;

    public Seq<View.ScratchElement> elements = new Seq<View.ScratchElement>();
    public Seq<Connection> connections = new Seq<Connection>();
    public View.ScratchElement moving;

    public ScratchTrigger.ScratchTriggerObject owner;

    public ScratchCanvasDialog(){
        super("@scratch-canvas");
        shown(this::build);
    }

    public void build(){
        cont.clear();
        cont.add(view = new View());
        view.constructScratch();
        addCaptureListener(new ElementGestureListener(){
            int pressPointer = -1;
            final Vec2 deltaVec = new Vec2();

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY){
                if (moving != null) {
                    moving.setPosition(moving.x + deltaX, moving.y + deltaY);
                } else {
                    view.offset.add(deltaVec.set(deltaX, deltaY));
                    view.setPosition(view.x + deltaX, view.y + deltaY);
                }
            }

            @Override
            public void tap(InputEvent event, float x, float y, int count, KeyCode button){

            }

            @Override
            public void touchDown(InputEvent event, float x, float y, int pointer, KeyCode button){
                if(pressPointer != -1) return;
                pressPointer = pointer;
                pressed = true;
                visualPressed = Time.millis() + 100;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, KeyCode button){
                if(pointer == pressPointer){
                    pressPointer = -1;
                    pressed = false;
                }
            }
        });
    }

    public Dialog show(ScratchTrigger.ScratchTriggerObject o) {
        owner = o;
        return super.show();
    }

    public class View extends WidgetGroup{
        public Vec2 offset = new Vec2();

        public View() {
            setFillParent(true);
        }

        public void constructScratch(){
            new ScratchElement((s) -> {}, 4, 4, 0, 0);
        }

        @Override
        public void draw() {
            super.draw();
        }

        @Override
        public void addChild(Element actor) {
            if (actor instanceof ScratchElement e)
                elements.add(e);
            super.addChild(actor);
        }

        public class ScratchElement extends Table {
            public Seq<Connection> in, out;

            public Cons<ScratchElement> code;

            public ScratchElement(Cons<ScratchElement> f, int i, int o){
                this(f, 0, 0, i, o);
            }

            public ScratchElement(Cons<ScratchElement> f, int i, int o, float x, float y) {
                in = new Seq<Connection>(i);
                out = new Seq<Connection>(o);
                code = f;

                view.addChild(this);
                setSize(200, 200);
                background(Tex.pane);

                defaults().size(32);

                table(t -> {
                    for (int ind = 0; ind < i; ind++) {
                        t.button(Icon.right, Styles.emptyi, () -> {

                        });
                        t.row();
                    }
                }).left().center();

                button(Icon.trash, Styles.emptyi, () -> {
                    remove();
                }).right().top();
                button(Icon.move, Styles.emptyi, () -> moving = moving == null ? this : null).left().top();

                table(t -> {
                    t.defaults().size(32);
                    for (int ind = 0; ind < o; ind++) {
                        t.button(Icon.add, Styles.emptyi, () -> {

                        });
                        t.row();
                    }
                    t.defaults().size(32);
                }).right().center();

                setPosition(x, y);
            }
        }
    }
}

class Connection {
    public ScratchCanvasDialog.View.ScratchElement in, out;

    public Connection() {
        Dialogs.scratchCanvasDialog.connections.add(this);
    }

    public void delete(){
        Dialogs.scratchCanvasDialog.connections.remove(this);
        in.out.remove(this);
        out.in.remove(this);
    }
}
