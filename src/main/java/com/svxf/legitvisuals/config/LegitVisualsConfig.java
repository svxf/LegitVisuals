package com.svxf.legitvisuals.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Color;
import cc.polyfrost.oneconfig.config.annotations.Dropdown;
import cc.polyfrost.oneconfig.config.annotations.Number;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;

public class LegitVisualsConfig extends Config {

    @Switch(
            name = "Main Toggle",
            description = "Global toggle for Legit Visuals features.",
            subcategory = "Legit Visuals"
    )
    public boolean lvEnabled = true;

    @Switch(
            name = "Render Hat",
            description = "Toggle visibility of the hat.",
            subcategory = "Legit Visuals"
    )
    public boolean hatEnabled = true;

    @Switch(
            name = "Render Jump Circles",
            description = "Toggle visibility of jump circles.",
            subcategory = "Legit Visuals"
    )
    public boolean jumpCirclesEnabled = true;

    @Switch(
            name = "Render Targeting",
            description = "Toggle visibility of target esp.",
            subcategory = "Legit Visuals"
    )
    public boolean targetingEnabled = true;

    @Switch(
            name = "Render Trail",
            description = "Toggle visibility of your trail.",
            subcategory = "Legit Visuals"
    )
    public boolean trailEnabled = true;

    @Switch(
            name = "Show Hat in first person",
            description = "Toggle hat visibility in first-person view.",
            subcategory = "Hat"
    )
    public boolean renderInFirstPerson = true;

    @Number(
            name = "Jump Circle Radius",
            min = 0.0F,
            max = 15.0F,
            description = "Radius of the jump circle.",
            subcategory = "Jump Circle"
    )
    public float jumpCircleRadius = 0.1f;
    @Number(
            name = "Jump Circle Fade Speed",
            min = 0.0F,
            max = 15000.0F,
            description = "Speed of the fade animation for jump circles. (Value * 100 = MS)",
            subcategory = "Jump Circle"
    )
    public float jumpCircleFadeSpeed = 6f;

    @Number(
            name = "Jump Circle Expand Speed",
            min = 0.0F,
            max = 15000.0F,
            description = "Speed of the expansion for jump circles. (Value * 100 = MS)",
            subcategory = "Jump Circle"
    )
    public float jumpCircleExpandSpeed = 10f;

    @Dropdown(
            name = "Targeting Style",
            options = {"Circle", "Tracers", "Cube"},
            description = "Style of the targeting ESP.",
            subcategory = "Targeting"
    )
    public int targetingStyle;

    @Color(
            name = "Primary Color",
            description = "The primary color of all of the features.",
            subcategory = "Colors"
    )
    public OneColor primaryColor = new OneColor(255, 0, 0);

    @Color(
            name = "Secondary Color",
            description = "The secondary color of all of the features.",
            subcategory = "Colors"
    )
    public OneColor secondaryColor = new OneColor(0, 0, 255);

    @Switch(
            name = "Moving Colors",
            description = "Enable dynamic color mixing.",
            subcategory = "Colors"
    )
    public boolean isMovingColors = true;

    @Switch(
            name = "Rainbow Toggle",
            description = "Make the colors rainbow.",
            subcategory = "Colors"
    )
    public boolean isRainbow = true;

    public LegitVisualsConfig() {
        super(new Mod("Legit Visuals", ModType.HUD), "legit-visuals.json");
        this.initialize();
    }
}
