package data.scripts;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import data.scripts.world.EquestriaMoonGen;

public class EquestriaMoonPlugin extends BaseModPlugin {

    private void initMod(){
        new EquestriaMoonGen().generate(Global.getSector());
    }

    @Override
    public void onNewGame() {
        Global.getLogger(this.getClass()).debug("Hooray My mod plugin in a jar is loaded!");
        initMod();
    }
}