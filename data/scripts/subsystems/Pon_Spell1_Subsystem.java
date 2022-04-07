package data.scripts.subsystems;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipEngineControllerAPI;
import com.fs.starfarer.api.combat.ShipwideAIFlags;

public class Pon_Spell1_Subsystem extends dl_BaseSubsystem
{
     public static final String SUBSYSTEM_ID = "Pon_Spell1"; //this should match the id in the csv
   
    public Pon_Spell1_Subsystem() {
        super(SUBSYSTEM_ID);
}

    @Override
    public void apply(MutableShipStatsAPI stats, String id, SubsystemState state, float effectLevel) {
        if (state == SubsystemState.OUT) {
            stats.getMaxSpeed().unmodify(id); // to slow down ship to its regular top speed while powering drive down
            stats.getMaxTurnRate().unmodify(id);
        } else {
            stats.getMaxSpeed().modifyFlat(id, 50f);
            stats.getAcceleration().modifyPercent(id, 200f * effectLevel);
            stats.getDeceleration().modifyPercent(id, 200f * effectLevel);
            stats.getTurnAcceleration().modifyFlat(id, 30f * effectLevel);
            stats.getTurnAcceleration().modifyPercent(id, 200f * effectLevel);
            stats.getMaxTurnRate().modifyFlat(id, 15f);
            stats.getMaxTurnRate().modifyPercent(id, 100f);
        }

        stats.getEntity();
        ShipAPI ship = (ShipAPI) stats.getEntity();
        String key = ship.getId() + "_" + id;
        Object test = Global.getCombatEngine().getCustomData().get(key);
        if (state == SubsystemState.IN) {
            if (test == null && effectLevel > 0.2f) {
                Global.getCombatEngine().getCustomData().put(key, new Object());
                ship.getEngineController().getExtendLengthFraction().advance(1f);
                for (ShipEngineControllerAPI.ShipEngineAPI engine : ship.getEngineController().getShipEngines()) {
                    if (engine.isSystemActivated()) {
                        ship.getEngineController().setFlameLevel(engine.getEngineSlot(), 1f);
                    }
                }
            }
        } else {
            Global.getCombatEngine().getCustomData().remove(key);
        }
    }

    @Override
    public void unapply(MutableShipStatsAPI stats, String id) {
        stats.getMaxSpeed().unmodify(id);
        stats.getMaxTurnRate().unmodify(id);
        stats.getTurnAcceleration().unmodify(id);
        stats.getAcceleration().unmodify(id);
        stats.getDeceleration().unmodify(id);
    }

    @Override
    public String getStatusString() {
        return null; //use default text
    }

    @Override
    public String getInfoString() {
        if (isActive()) return "IMPROVED MANOEUVRABILITY";
        if (isCooldown()) return "GATHERING MANA";
        return "MANA FULL";
    }

    @Override
    public String getFlavourString() {
        return "+50 MAX SPEED AND ACCELERATION BOOST";
    }

    @Override
    public int getNumGuiBars() { //number of slots the subsystem uses in combat GUI
        return 1;
    }

    @Override
    public void aiInit() {
        //do nothing
    }

    @Override
    public void aiUpdate(float v) {
        if (ship == null || !ship.isAlive()) return;
        ShipwideAIFlags flags = ship.getAIFlags();
        if (
                flags.hasFlag(ShipwideAIFlags.AIFlags.MANEUVER_TARGET)
                || flags.hasFlag(ShipwideAIFlags.AIFlags.TURN_QUICKLY)
                || flags.hasFlag(ShipwideAIFlags.AIFlags.RUN_QUICKLY)
        ) {
            activate();
        }
    }
}
