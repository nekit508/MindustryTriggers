package ui.fragments;

import mindustry.Vars;
import mindustry.gen.Tex;
import ui.Dialogs;

public class TriggerHudFrag {
    public static void init(){
        Vars.ui.hudGroup.fill(t -> {
            t.center().top();
            t.table(mbt -> {
                mbt.background(Tex.pane);
                mbt.button(Tex.alphaaaa, () -> {
                    Dialogs.createTriggerDialog.show();
                });
            });
        });
    }
}
