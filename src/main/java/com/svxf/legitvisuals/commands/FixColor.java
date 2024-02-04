package com.svxf.legitvisuals.commands;

import cc.polyfrost.oneconfig.config.core.OneColor;
import com.svxf.legitvisuals.Main;
import net.weavemc.loader.api.command.Command;

public class FixColor extends Command {
    public FixColor()
    {
        super("fixcolor");
    }

    @Override
    public void handle(String[] args) {
        Main.config.primaryColor = new OneColor(1,1,1);
    }
}
