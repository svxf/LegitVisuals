package com.svxf.legitvisuals.commands;

import cc.polyfrost.oneconfig.config.core.OneColor;
import com.svxf.legitvisuals.Main;
import net.weavemc.loader.api.command.Command;

public class PrimaryColor extends Command {
    public PrimaryColor()
    {
        super("primarycolor");
    }

    @Override
    public void handle(String[] args) {
        if (args.length == 0) return;

        String colorString = args[0];

        // format: "/primarycolor rgb(255,255,255)"
        if (colorString.matches("^rgb\\(\\d{1,3},\\d{1,3},\\d{1,3}\\)$")) {
            String[] rgbValues = colorString.replaceAll("[^0-9,]", "").split(",");

            if (rgbValues.length == 3) {
                int red = Integer.parseInt(rgbValues[0]);
                int green = Integer.parseInt(rgbValues[1]);
                int blue = Integer.parseInt(rgbValues[2]);

                Main.config.primaryColor = new OneColor(red, green, blue);
            }
        }
    }
}