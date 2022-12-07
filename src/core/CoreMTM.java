package core;

import arc.util.Log;
import content.TriggersMTM;
import mindustry.mod.Mod;
import ui.Dialogs;
import ui.Fragments;

public class CoreMTM extends Mod {
    public CoreMTM() {
    }

    @Override
    public void init() {
        Dialogs.init();
        Fragments.init();
    }

    @Override
    public void loadContent() {
        TriggersMTM.load();
    }
}
