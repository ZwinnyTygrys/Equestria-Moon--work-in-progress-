package data.scripts.subsystems;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.SoundPlayerAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipwideAIFlags;
import com.fs.starfarer.api.combat.WeaponAPI;
import static data.scripts.subsystems.dl_BaseSubsystem.SubsystemState.ACTIVE;
import java.awt.Color;
import java.util.List;
import org.lwjgl.util.vector.Vector2f;



public class Pon_Spell2_Subsystem extends dl_BaseSubsystem
{
    
    public static float DAMAGE_MULT = 0.9f;
    public static final String SUBSYSTEM_ID = "Pon_Spell2";
    public Pon_Spell2_Subsystem() {
        super(SUBSYSTEM_ID);
}
    public dl_BaseSubsystem.SubsystemState getState(dl_BaseSubsystem.SubsystemState state)
    {       
        return state;
    }
    @Override
    public void apply(MutableShipStatsAPI stats, String id, dl_BaseSubsystem.SubsystemState state, float effectLevel) {
        ShipAPI ship;
        if (stats.getEntity() instanceof ShipAPI) {
            ship = (ShipAPI) stats.getEntity();
            } else {
            return;
        }
        

      
        float shieldArc = ship.getHullSpec().getShieldSpec().getArc();//ships original shield arc
        Color innerShieldColor = ship.getHullSpec().getShieldSpec().getInnerColor();//ships original inner shield color
        Color ringShieldColor = ship.getHullSpec().getShieldSpec().getRingColor();//ships original ring color
        Color activeShieldColor = new Color(0, 100, 0);//inner shield color
        Color activeRingShieldColor = new Color(200, 200, 200);//outer ring color
        SoundPlayerAPI soundPlayer = Global.getSoundPlayer();
        
        stats.getShieldTurnRateMult().modifyMult(id, 1f);
        stats.getShieldUnfoldRateMult().modifyPercent(id, 150);//2000 originally
        stats.getShieldDamageTakenMult().modifyMult(id, 1f - DAMAGE_MULT * effectLevel);
	stats.getShieldDamageTakenMult().modifyMult(id, 0.1f);
        stats.getShieldUpkeepMult().modifyMult(id, 0f);
        //ship.getShield().setArc(360f);
        if(isActive() || isFadingIn()){
            ship.getShield().toggleOn();
            ship.getShield().setArc(360f);
            ship.getShield().setInnerColor(activeShieldColor);
            ship.getShield().setRingColor(activeRingShieldColor);
            
                //Global.getLogger(this.getClass()).debug("wrong thing");
                Global.getSoundPlayer().playLoop("system_fortress_shield_loop", ship, 1f, 1f, ship.getLocation(), new Vector2f()); //playloop(sound ID, object playing, pitch, volume, location, velocity)
                //Global.getSoundPlayer().applyLowPassFilter(0.75f, 0.0f); 
            //}
            
        }else if (isFadingOut()){
            //ship.getShield().toggleOff();
            ship.getShield().setArc(shieldArc);
            ship.getShield().setInnerColor(innerShieldColor);
            ship.getShield().setRingColor(ringShieldColor);
            
        }
        
    }

    @Override
    public void unapply(MutableShipStatsAPI stats, String id) {
        ShipAPI ship;
        if (stats.getEntity() instanceof ShipAPI) {
            ship = (ShipAPI) stats.getEntity();
            } else {
            return;
        }
        float shieldArc = ship.getHullSpec().getShieldSpec().getArc();
        /*List<WeaponAPI> shipWeapons = ship.getAllWeapons();
        for(WeaponAPI weapon: shipWeapons)
        {
            weapon.setRemainingCooldownTo(0.0f);
        }*/
        if(isFadingOut()){
        //ship.getShield().toggleOff();
        ship.getShield().setArc(shieldArc);
        }
        stats.getShieldArcBonus().unmodify(id);
        stats.getShieldDamageTakenMult().unmodify(id);
        stats.getShieldTurnRateMult().unmodify(id);
        stats.getShieldUnfoldRateMult().unmodify(id);
        stats.getShieldUpkeepMult().unmodify(id);
    }

    @Override
    public void aiInit() {
        
    }

    @Override
    public void aiUpdate(float amount) {
        if (ship == null || !ship.isAlive()) return;
        ShipwideAIFlags flags = ship.getAIFlags();
        if (
                flags.hasFlag(ShipwideAIFlags.AIFlags.KEEP_SHIELDS_ON)
                || flags.hasFlag(ShipwideAIFlags.AIFlags.BACKING_OFF)
                || flags.hasFlag(ShipwideAIFlags.AIFlags.HAS_INCOMING_DAMAGE)
        ) {
            activate();
        }
    }

    @Override
    public String getStatusString() {
        return null; 
    }

    @Override
    public String getInfoString() {
        if (isActive()) return "SHIELD UP";
        if (isFadingIn()) return "SHIELD UP";
        if (isCooldown()) return "COOLING DOWN";
        if (isFadingOut()) return "COOLING DOWN";
        return "READY";
    }

    @Override
    public String getFlavourString() {
        return "FLUX EFFICIENT SHIELD";
    }

    @Override
    public int getNumGuiBars() {
        return 1;
    }
    
}
