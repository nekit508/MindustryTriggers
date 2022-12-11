package ui.fragments;

import arc.Core;
import arc.Events;
import arc.math.Interp;
import arc.scene.Element;
import arc.scene.Group;
import arc.scene.actions.Actions;
import arc.scene.ui.layout.Table;
import arc.util.Align;
import mindustry.content.Blocks;
import mindustry.editor.MapEditorDialog;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Tex;
import world.Trigger;

import static mindustry.Vars.player;

public class TriggerConfigFrag {
    Table table = new Table();
    Trigger.TriggerObject selected;

    public void build(Group parent){
        table.visible = false;
        table.background(Tex.pane);
        parent.addChild(table);

        Events.on(EventType.ResetEvent.class, e -> forceHide());
    }

    public void forceHide(){
        table.visible = false;
        selected = null;
    }

    public boolean isShown(){
        return table.visible && selected != null;
    }

    public Trigger.TriggerObject getSelected(){
        return selected;
    }

    public void showConfig(Trigger.TriggerObject tile){
        if(selected != null) selected.hideConfiguration(table);
        if(tile.shouldShowConfig(table)){
            selected = tile;

            table.visible = true;
            table.clear();
            tile.showConfiguration(table);
            table.pack();
            table.setTransform(true);
            table.actions(Actions.scaleTo(0f, 1f), Actions.visible(true),
                    Actions.scaleTo(1f, 1f, 0.07f, Interp.pow3Out));

            table.update(() -> {
                if(selected != null && selected.shouldHideConfiguration(table)){
                    hideConfig();
                    return;
                }

                table.setOrigin(Align.center);
                if(selected == null){
                    hideConfig();
                }else{
                    selected.updateConfigAling(table);
                }
            });
        }
    }

    public boolean hasConfigMouse(){
        Element e = Core.scene.hit(Core.input.mouseX(), Core.graphics.getHeight() - Core.input.mouseY(), true);
        return e != null && (e == table || e.isDescendantOf(table));
    }

    public void hideConfig(){
        if(selected != null) selected.hideConfiguration(table);
        selected = null;
        table.actions(Actions.scaleTo(0f, 1f, 0.06f, Interp.pow3Out), Actions.visible(false));
    }
}
