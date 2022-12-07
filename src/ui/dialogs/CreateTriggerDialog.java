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
    public Table triggersListContainer;
    public Table triggerCreateTable;

    public CreateTriggerDialog(){
        super("@create-trigger");
        shown(this::setup);
        addCloseButton();
    }

    public void setup(){
        cont.clear();

        cont.table(t -> {
            Table triggersList = new Table().top();
            triggersListContainer = t;

            Trigger.triggersTypes.forEach((id, triggerType) -> {
                triggersList.table(tt -> {
                    triggerType.constructListElement(tt);
                    tt.button(new TextureRegionDrawable(Core.atlas.find("mtm-plus")), () -> {
                        setup(triggerType);
                    }).right();
                });
                triggersList.row();
                triggersList.setWidth(triggersListContainer.getWidth()*0.8f);
            });
            ScrollPane scroll = new ScrollPane(triggersList, Styles.smallPane);
            scroll.setOverscroll(false, false);
            scroll.setScrollingDisabled(true, false);
            triggersListContainer.add(scroll).maxHeight(triggersListContainer.getHeight()).width(triggersListContainer.getWidth()*0.8f);
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
