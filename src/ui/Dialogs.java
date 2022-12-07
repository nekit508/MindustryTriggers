package ui;

import arc.util.Log;
import ui.dialogs.CreateTriggerDialog;

public class Dialogs {
    public static CreateTriggerDialog createTriggerDialog;

    public static void init(){
        createTriggerDialog = new CreateTriggerDialog();
    }
}
