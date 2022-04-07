package data.hullmods;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI;
import data.scripts.subsystems.Pon_Spell2_Subsystem;
import data.scripts.util.dl_SubsystemUtils;


public class Pon_Spell2_Hullmod extends BaseHullMod
{
    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        dl_SubsystemUtils.queueSubsystemForShip(ship, Pon_Spell2_Subsystem.class);
    }
    
    @Override
	public boolean isApplicableToShip(ShipAPI ship)
        {
            return  ship.getVariant().getHullMods().contains("manapool");
        }
}
