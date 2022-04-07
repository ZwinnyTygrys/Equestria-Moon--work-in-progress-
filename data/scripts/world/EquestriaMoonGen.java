package data.scripts.world;

import com.fs.starfarer.api.campaign.SectorAPI;
import data.scripts.world.system.EquestrianSystem;

public class EquestriaMoonGen {
    public void generate(SectorAPI sector){
        new EquestrianSystem().generate(sector);
    }
}
