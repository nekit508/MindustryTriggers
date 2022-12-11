package ui.dialogs;

import arc.Core;
import arc.graphics.g2d.TextureAtlas;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import world.Trigger;

public class CreateTriggerDialog extends BaseDialog {
    public Table triggerCreateTable;

    public CreateTriggerDialog(){
        super("@create-trigger");
        shown(this::setup);
        addCloseButton();
    }

    public void setup(){
        cont.clear();

        cont.fill(t -> {
            Table triggersList = new Table().top();
            triggersList.setWidth(t.getWidth());

            Trigger.triggersTypes.forEach((id, triggerType) -> {
                triggerType.constructListElement(triggersList);
                triggersList.button(new TextureRegionDrawable(Core.atlas.find("mtm-plus")), () -> {
                    setup(triggerType);
                }).right();
                triggersList.row();
            });

            ScrollPane scroll = new ScrollPane(triggersList, Styles.smallPane);
            scroll.setOverscroll(false, false);
            scroll.setScrollingDisabled(true, false);
            t.add(scroll).maxHeight(t.getHeight());
        });
    }

    public void setup(Trigger trigger){
        cont.clear();

        cont.table(t -> {
            triggerCreateTable = t;
            t.table(trigger::constructCreateTable);
        });
    }
}
