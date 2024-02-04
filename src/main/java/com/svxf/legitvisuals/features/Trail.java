package com.svxf.legitvisuals.features;

import com.svxf.legitvisuals.Main;
import com.svxf.legitvisuals.events.MotionEvent;
import com.svxf.legitvisuals.utils.pair.Pair;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Vec3;
import net.weavemc.loader.api.event.RenderWorldEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.svxf.legitvisuals.utils.Helpers.*;
import static net.minecraft.client.renderer.GlStateManager.disableDepth;

public class Trail {
    private final List<Vec3> path = new ArrayList<>();

    @SubscribeEvent
    public void onMotionEvent(MotionEvent e) {
        if (!Main.config.trailEnabled || !Main.config.lvEnabled)
        {
            path.clear();
            return;
        }

        if (mc.thePlayer.lastTickPosX != mc.thePlayer.posX || mc.thePlayer.lastTickPosY != mc.thePlayer.posY || mc.thePlayer.lastTickPosZ != mc.thePlayer.posZ) {
            path.add(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ));
        }

        while (path.size() > 15f) {
            path.remove(0);
        }
    }

    @SubscribeEvent
    public void onRenderWorldEvent(RenderWorldEvent event) {
        if (!Main.config.trailEnabled || !Main.config.lvEnabled) return;
        Pair<Color, Color> colors = Pair.of(Main.config.primaryColor.toJavaColor(), Main.config.secondaryColor.toJavaColor());
        renderLine(path, colors);
    }

    public void renderLine(final List<Vec3> path, Pair<Color, Color> colors) {
        disableDepth();

        setAlphaLimit(0);
        resetColor();
        setup2DRendering();
        startBlend();
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glLineWidth(3);
        GL11.glBegin(GL11.GL_LINE_STRIP);

        boolean isRainbow = Main.config.isRainbow;
        int count = 0;
        int alpha = 200;
        int fadeOffset = 15;
        for (Vec3 v : path) {
            if (fadeOffset > count) {
                alpha = count * (200 / fadeOffset);
            }

            resetColor();
            if (isRainbow)
            {
                color(rainbow(7, count * 4, 1, 1, isRainbow ? .5f : .2f).getRGB());
            } else {
                color(Main.config.isMovingColors ? mixColors(colors.getFirst(), colors.getSecond()).getRGB() :
                        new Color(interpolateColor(colors.getFirst().getRGB(), colors.getSecond().getRGB(), count)).getRGB());
            }
            final double x = v.xCoord - mc.getRenderManager().viewerPosX;
            final double y = (v.yCoord - mc.getRenderManager().viewerPosY);
            final double z = v.zCoord - mc.getRenderManager().viewerPosZ;
            GL11.glVertex3d(x, y, z);
            count++;
        }

        GL11.glEnd();

        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        end2DRendering();

        GlStateManager.enableDepth();
    }
}
