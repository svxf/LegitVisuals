package com.svxf.legitvisuals;

import com.gitlab.candicey.zenithloader.ZenithLoader;
import com.gitlab.candicey.zenithloader.dependency.Dependencies;
import com.svxf.legitvisuals.commands.FixColor;
import com.svxf.legitvisuals.features.ChinaHat;
import com.svxf.legitvisuals.features.JumpCircle;
import com.svxf.legitvisuals.commands.PrimaryColor;
import com.svxf.legitvisuals.commands.SecondaryColor;
import com.svxf.legitvisuals.config.LegitVisualsConfig;
import com.svxf.legitvisuals.features.Targeting;
import com.svxf.legitvisuals.features.Trail;
import net.weavemc.loader.api.ModInitializer;
import net.weavemc.loader.api.command.CommandBus;
import net.weavemc.loader.api.event.EventBus;
import net.weavemc.loader.api.event.StartGameEvent;

public class Main implements ModInitializer {
    public static LegitVisualsConfig config;

    @Override
    public void preInit() {
        System.out.println("Initializing LegitVisuals!");

        EventBus.subscribe(this);

        ZenithLoader.INSTANCE.loadDependencies(
                Dependencies.INSTANCE.getConcentra().invoke(
                        "legitvisuals"
                )
        );

        EventBus.subscribe(StartGameEvent.Pre.class, (event) -> config = new LegitVisualsConfig());

        CommandBus.register(new PrimaryColor());
        CommandBus.register(new SecondaryColor());
        CommandBus.register(new FixColor());

        ChinaHat chinaHat = new ChinaHat();
        JumpCircle jumpCircle = new JumpCircle();
        Targeting targeting = new Targeting();
        Trail trail = new Trail();

        EventBus.subscribe(chinaHat);
        EventBus.subscribe(jumpCircle);
        EventBus.subscribe(targeting);
        EventBus.subscribe(trail);
    }
}