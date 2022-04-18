package data.scripts.rulecmd.dialog;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.InteractionDialogPlugin;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.combat.EngagementResultAPI;

import java.awt.*;
import java.util.Map;

public class EquestrianDialogFirstEncounter implements InteractionDialogPlugin {

    public static enum OptionId {
        INIT,
        INSPECT_CRYSTALS,
        LEAVE,
    }

    protected InteractionDialogAPI dialog;
    protected TextPanelAPI text;
    protected OptionPanelAPI options;

    @Override
    public void init(InteractionDialogAPI dialog) {
        this.dialog = dialog;
        this.text = dialog.getTextPanel();
        this.options = dialog.getOptionPanel();
        this.optionSelected(null, OptionId.INIT);
    }

    @Override
    public void optionSelected(String optionText, Object optionData) {
        if(optionData instanceof OptionId){
            dialog.getOptionPanel().clearOptions();

            switch ((OptionId) optionData){
                case INIT:
                    text.addPara("Your fleet closes in on a dreary world.");
                    text.addPara("Suddenly, a report from your quartermaster chimes through.");
                    text.addPara("\"Sir! You- I can't believe this. Some form of crystal is materializing into our cargo holds!\"");
                    text.addPara("100 Crystals Added to Inventory.");
                    text.highlightInLastPara(Color.orange,"100 Crystals Added to Inventory.");
                    options.addOption("Interesting...", OptionId.LEAVE);
                    options.addOption("Inspect new cargo.", OptionId.INSPECT_CRYSTALS);

                    Global.getSector().getPlayerFleet().getCargo().addCommodity("equestrian_crystals", 100);
                    break;
                case INSPECT_CRYSTALS:
                    text.addPara("Your Quartermaster brings up recordings of your hold from not but a minute ago onto your tri-pad." +
                            " You see slots of empty racks on the center of your screen. Nothing appears to be happening for a few seconds before" +
                            " suddenly a flash of light blinds the camera only to reveal stacks of crystals now standing in the empty space.");

                    options.addOption("Interesting...", OptionId.LEAVE);
                    break;
                case LEAVE:
                    dialog.dismiss();
                    break;
            }
        }
    }

    @Override
    public void optionMousedOver(String optionText, Object optionData) {

    }

    @Override
    public void advance(float amount) {

    }

    @Override
    public void backFromEngagement(EngagementResultAPI battleResult) {

    }

    @Override
    public Object getContext() {
        return null;
    }

    @Override
    public Map<String, MemoryAPI> getMemoryMap() {
        return null;
    }
}
