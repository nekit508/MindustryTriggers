package world;

import arc.Core;
import arc.Events;
import arc.func.Prov;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.input.InputProcessor;
import arc.input.KeyCode;
import arc.math.geom.Vec2;
import arc.scene.event.ClickListener;
import arc.scene.event.EventListener;
import arc.scene.event.InputEvent;
import arc.scene.event.SceneEvent;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import arc.util.Log;
import arc.util.Strings;
import arc.util.Structs;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.core.GameState;
import mindustry.core.UI;
import mindustry.ctype.Content;
import mindustry.ctype.ContentType;
import mindustry.editor.MapEditorDialog;
import mindustry.game.EventType;
import mindustry.gen.Groups;
import mindustry.graphics.Layer;
import ui.Dialogs;
import ui.fragments.TriggerConfigFrag;

import java.lang.reflect.Constructor;
import java.util.HashMap;

public class Trigger {
    public static long globalTriggerIDCounter = 0;

    public static HashMap<String, Trigger> triggersTypes = new HashMap<String, Trigger>();
    public static HashMap<Long, TriggerObject> triggersObjects = new HashMap<Long, TriggerObject>();

    public Prov<TriggerObject> triggerObject;
    public final String name;

    public TextureRegion icon;

    public static TriggerConfigFrag triggerConfigFragment = new TriggerConfigFrag();

    static {
        Events.run(EventType.Trigger.update, () -> {
            if (Vars.state.isGame() && !Vars.state.isPaused())
                triggersObjects.forEach((i, t) -> t.update());
        });
        Events.run(EventType.Trigger.draw, () -> {
            if (Vars.state.isGame())
                triggersObjects.forEach((i, t) -> t.draw());
        });
        Events.run(EventType.Trigger.newGame, () -> {
            triggersObjects.clear();
            globalTriggerIDCounter = 0;
        });
        Events.run(EventType.ClientLoadEvent.class, () -> {
            triggersTypes.forEach((i, t) -> t.load());
            triggerConfigFragment.build(Vars.ui.hudGroup);
            Core.input.addProcessor(new InputProcessor() {
                Vec2 mouseWorldPos;
                final Vec2 tempVec = new Vec2();
                @Override
                public boolean touchUp(int screenX, int screenY, int pointer, KeyCode button) {
                    triggerConfigFragment.hideConfig();
                    triggersObjects.forEach((i, t) -> {
                        if (button == KeyCode.mouseLeft) {
                            mouseWorldPos = Core.input.mouseWorld();
                            tempVec.set(t.x, t.y);
                            if (tempVec.sub(mouseWorldPos).len() < t.clipSize) {
                                triggerConfigFragment.showConfig(t);
                            }
                        }
                    });
                    return true;
                }
            });
        });
    }

    public Trigger(String name){
        this.name = Vars.content.transformName(name);
        triggersTypes.put(name, this);
        initBuilding();
    }

    public void constructCreateTable(Table owner){
        owner.button("create", () -> {
            Dialogs.createTriggerDialog.hide();
        });
    }

    public void constructListElement(Table owner){
        owner.table(t -> {
            t.image(icon).marginRight(32f);
            t.add(Core.bundle.get(name + "-name"));
        }).left();
    }

    public void load() {
        icon = Core.atlas.find(name + "-icon");
    }

    public final TriggerObject createTrigger(){
        TriggerObject out = triggerObject.get();
        out.trigger = this;
        return out;
    }

    protected void initBuilding(){
        try{
            Class<?> current = getClass();

            if(current.isAnonymousClass()){
                current = current.getSuperclass();
            }

            while(triggerObject == null && Trigger.class.isAssignableFrom(current)){
                Class<?> type = Structs.find(current.getDeclaredClasses(), t -> TriggerObject.class.isAssignableFrom(t) && !t.isInterface());
                if(type != null){
                    Constructor<? extends TriggerObject> cons = (Constructor<? extends TriggerObject>)type.getDeclaredConstructor(type.getDeclaringClass());
                    triggerObject = () -> {
                        try{
                            return cons.newInstance(this);
                        }catch(Exception e){
                            throw new RuntimeException(e);
                        }
                    };
                }
                current = current.getSuperclass();
            }

        }catch(Throwable ignored){
        }
    }

    public class TriggerObject {
        public Trigger trigger;
        public long id;
        public float x, y;
        public float clipSize = 4;

        public TriggerObject(){
            id = globalTriggerIDCounter++;
        }

        public void add(){
            triggersObjects.put(id, this);
        }

        public void remove(){
            triggersObjects.remove(id);
        }

        public void save(Writes writes){
            writes.l(id);
        }

        public void load(Reads reads){
            globalTriggerIDCounter--;
            id = reads.l();
        }

        public void update(){

        }

        public void draw(){
            Draw.z(Layer.light + 1f);
            Draw.rect(icon, x, y);
        }

        // begin configuration
        protected Cell<TextField> xF, yF;

        public void showConfiguration(Table owner){
            xF = owner.field(String.valueOf((int) x), t -> {}).padRight(10).valid(Strings::canParseInt).maxTextLength(6);
            owner.add(Core.bundle.get("trigger-settings-x"));
            owner.row();
            yF = owner.field(String.valueOf((int) y), t -> {}).padRight(10).valid(Strings::canParseInt).maxTextLength(6);
            owner.add(Core.bundle.get("trigger-settings-y"));
            owner.row();
        }

        public void hideConfiguration(Table owner){
            if (xF.get().isValid() || yF.get().isValid()) {
                x = Integer.parseInt(xF.get().getText());
                y = Integer.parseInt(yF.get().getText());
            }
        }

        public boolean shouldShowConfig(Table owner){
            return true;
        }

        public boolean shouldHideConfiguration(Table owner){
            return false;
        }

        public void updateConfigAling(Table table) {
            Vec2 pos = Core.input.mouseScreen(this.x, this.y);
            table.setPosition(pos.x, pos.y, 2);
        }

        // end configuration
    }
}
