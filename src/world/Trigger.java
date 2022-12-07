package world;

import arc.Core;
import arc.Events;
import arc.func.Prov;
import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.layout.Table;
import arc.util.Log;
import arc.util.Structs;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.core.GameState;
import mindustry.ctype.Content;
import mindustry.ctype.ContentType;
import mindustry.game.EventType;
import ui.Dialogs;

import java.lang.reflect.Constructor;
import java.util.HashMap;

public class Trigger {
    public static long globalTriggerIDCounter = 0;

    public static HashMap<String, Trigger> triggersTypes = new HashMap<String, Trigger>();
    public static HashMap<Long, TriggerObject> triggersObjects = new HashMap<Long, TriggerObject>();

    public Prov<TriggerObject> triggerObject;
    public final String name;

    public TextureRegion icon;

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
    }

    public Trigger(String name){
        this.name = Vars.content.transformName(name);;
        load();
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
        out.add();
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

        }
    }
}
