package data.scripts.rulecmd;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.util.Misc;
import data.scripts.rulecmd.dialog.EquestrianDialogFirstEncounter;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EquestriaDialogManager extends BaseCommandPlugin {

    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap){
        String interactionTargetID = dialog.getInteractionTarget().getId();
        Global.getLogger(this.getClass()).info("Fired Event " + ruleId.toLowerCase(Locale.ROOT) + " " + interactionTargetID);

        // When the player first interacts with Equestria
        if (interactionTargetID.equals("equestrian_alpha")){
            new EquestrianDialogFirstEncounter().init(dialog);
        }
        return true;
    }
}
