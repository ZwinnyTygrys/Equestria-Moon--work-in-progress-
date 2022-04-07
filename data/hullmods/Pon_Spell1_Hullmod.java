
package data.hullmods;

import com.fs.starfarer.api.combat.ShipAPI;
import data.scripts.subsystems.Pon_Spell1_Subsystem;
import data.scripts.util.dl_SubsystemUtils;
import com.fs.starfarer.api.combat.BaseHullMod;


public class Pon_Spell1_Hullmod extends BaseHullMod 
{
    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        dl_SubsystemUtils.queueSubsystemForShip(ship, Pon_Spell1_Subsystem.class);
    }
    
    @Override
	public boolean isApplicableToShip(ShipAPI ship)
        {
            return  ship.getVariant().getHullMods().contains("manapool");
        }
}
