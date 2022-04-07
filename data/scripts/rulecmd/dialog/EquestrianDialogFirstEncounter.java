package data.scripts.rulecmd.dialog;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.campaign.rules.Option;
import com.fs.starfarer.api.combat.EngagementResultAPI;
import com.fs.starfarer.api.fleet.FleetAPI;
import data.scripts.util.MagicCampaign;
import data.scripts.util.MagicVariables;

import java.awt.*;
import java.util.Map;

public class EquestrianDialogFirstEncounter implements InteractionDialogPlugin {
    protected InteractionDialogAPI dialog;
    protected TextPanelAPI text;
    protected OptionPanelAPI options;

    public static enum OptionID{
        INIT,
        LEAVE
    }

    @Override
    public void init(InteractionDialogAPI dialog) {
        this.dialog = dialog;
        this.text = dialog.getTextPanel();
        this.options = dialog.getOptionPanel();
        this.optionSelected(null, OptionID.INIT);
    }

    @Override
    public void optionSelected(String optionText, Object optionData) {
        if(optionData instanceof OptionID){
            dialog.getOptionPanel().clearOptions();

            switch ((OptionID) optionData){
                case INIT:
                    text.addPara("Your fleet closes in on a dreary world.");
                    text.addPara("Suddenly, a report from your inventory manager chimes through.");
                    text.addPara("\"Sir! You- I can't believe this. Some form of crystal is materializing into our cargo holds!\"");
                    text.addPara("100 Crystals Added to Inventory.");
                    text.highlightInLastPara(Color.orange,"100 Crystals Added to Inventory.");
                    options.addOption("Interesting...", OptionID.LEAVE, null);

                    Global.getSector().getPlayerFleet().getCargo().addCommodity("equestrian_crystals", 100);
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
