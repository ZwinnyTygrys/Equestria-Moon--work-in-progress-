package data.scripts.world.system;

import com.fs.starfarer.api.FactoryAPI;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.EconomyAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.FullName;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.fs.starfarer.api.impl.campaign.ids.Ranks;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;
import data.scripts.util.MagicCampaign;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class EquestrianSystem {
    public void generate(SectorAPI sector){
        StarSystemAPI system = sector.createStarSystem("Equineus Prime");
        system.getLocation().set((float) (Math.random() * -78000) + 39000, 39000); // Adding some variablility to the spawning placement. completely superficial
        system.setBackgroundTextureFilename("graphics/backgrounds/background1.jpg");

        PlanetAPI equestrianStar = system.initStar("equestrian_helios",
                "star_yellow",
                1100f,
                450);

        PlanetAPI equestriaA = system.addPlanet("equestrian_alpha",
                null,
                "Equus",
                "arid",
                180,
                400f,
                0f,
                0f);

        PlanetAPI equestrianA1 = system.addPlanet("equestrian_beta",
                equestriaA,
                "Lua",
                "barren-bombarded",
                180f,
                120f,
                2420f,
                30f);

        PlanetAPI equestrianA2 = system.addPlanet("equestrian_beta",
                equestriaA,
                "Selene",
                "barren-bombarded",
                0f,
                80f,
                2420f,
                30f);

        equestrianStar.setCircularOrbit(equestriaA,270f,5060f,60f);

        system.setLightColor(new Color(255,255,255));
        system.autogenerateHyperspaceJumpPoints(true,true);

        MarketAPI equestrianMarket = addMarketplace("equestrian", equestriaA, null, "Equestria", 10,
                Arrays.asList(
                        Conditions.POPULATION_10,
                        Conditions.FARMLAND_BOUNTIFUL,
                        Conditions.HABITABLE,
                        Conditions.MILD_CLIMATE,
                        Conditions.ORE_ABUNDANT ),
                Arrays.asList(
                        Submarkets.LOCAL_RESOURCES,
                        Submarkets.SUBMARKET_STORAGE ),
                Arrays.asList(
                        Industries.FARMING,
                        Industries.GROUNDDEFENSES,
                        Industries.LIGHTINDUSTRY,
                        Industries.ORBITALSTATION
                ),0.18f,true,true);

        MagicCampaign.addCustomPerson(equestrianMarket,"Celestia","","equestrian_celestia",FullName.Gender.FEMALE,"equestrian","equestrian_princess_sun","equestrian_princess",false,0,0);

        Global.getLogger(this.getClass()).info("Created Equestrian Star System");
    }

    /**
     * Shorthand function for adding a market -- this is derived from tahlan mod
     */
    public static MarketAPI addMarketplace(String factionID, SectorEntityToken primaryEntity, List<SectorEntityToken> connectedEntities, String name,
                                           int popSize, List<String> marketConditions, List<String> submarkets, List<String> industries, float tariff,
                                           boolean isFreePort, boolean floatyJunk) {
        EconomyAPI globalEconomy = Global.getSector().getEconomy();
        String planetID = primaryEntity.getId();
        String marketID = planetID + "_market"; //IMPORTANT this is a naming convention for markets. didn't want to have to pass in another variable :D

        MarketAPI newMarket = Global.getFactory().createMarket(marketID, name, popSize);
        newMarket.setFactionId(factionID);
        newMarket.setPrimaryEntity(primaryEntity);
        //newMarket.getTariff().modifyFlat("generator", tariff);
        newMarket.getTariff().setBaseValue(tariff);

        //Add submarkets, if any
        if (null != submarkets) {
            for (String market : submarkets) {
                newMarket.addSubmarket(market);
            }
        }

        //Add conditions
        for (String condition : marketConditions) {
            newMarket.addCondition(condition);
        }

        //Add industries
        for (String industry : industries) {
            newMarket.addIndustry(industry);
        }

        //Set free port
        newMarket.setFreePort(isFreePort);

        //Add connected entities, if any
        if (null != connectedEntities) {
            for (SectorEntityToken entity : connectedEntities) {
                newMarket.getConnectedEntities().add(entity);
            }
        }

        //set market in global, factions, and assign market, also submarkets
        globalEconomy.addMarket(newMarket, floatyJunk);
        primaryEntity.setMarket(newMarket);
        primaryEntity.setFaction(factionID);

        if (null != connectedEntities) {
            for (SectorEntityToken entity : connectedEntities) {
                entity.setMarket(newMarket);
                entity.setFaction(factionID);
            }
        }

        //Finally, return the newly-generated market
        return newMarket;
    }
}
