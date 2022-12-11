package ui;

import arc.util.Log;
import ui.dialogs.CreateTriggerDialog;
import ui.dialogs.ScratchCanvasDialog;

public class Dialogs {
    public static CreateTriggerDialog createTriggerDialog;
    public static ScratchCanvasDialog scratchCanvasDialog;

    public static void init(){
        createTriggerDialog = new CreateTriggerDialog();
        scratchCanvasDialog = new ScratchCanvasDialog();
    }
}
