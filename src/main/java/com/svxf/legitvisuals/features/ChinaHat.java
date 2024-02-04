package com.svxf.legitvisuals.features;

import com.svxf.legitvisuals.LVMain;
import com.svxf.legitvisuals.utils.pair.Pair;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.AxisAlignedBB;
import net.weavemc.loader.api.event.RenderWorldEvent;
import net.weavemc.loader.api.event.SubscribeEvent;

import java.awt.*;

import static com.svxf.legitvisuals.utils.Utils.*;
import static org.lwjgl.opengl.GL11.*;

public class ChinaHat {
    @SubscribeEvent
    public void onRender3DEvent(RenderWorldEvent event)
    {
        if (!LVMain.config.lvEnabled || !LVMain.config.hatEnabled || mc.thePlayer == null || mc.theWorld == null || mc.thePlayer.isInvisible() || mc.thePlayer.isDead
                || (!LVMain.config.renderInFirstPerson && mc.gameSettings.thirdPersonView == 0)) {
            return;
        }

        double posX = mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * getTimer().renderPartialTicks - mc.getRenderManager().viewerPosX, // renderPosX
                posY = mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * getTimer().renderPartialTicks - mc.getRenderManager().viewerPosY,
                posZ = mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * getTimer().renderPartialTicks - mc.getRenderManager().viewerPosZ;

        AxisAlignedBB axisalignedbb = mc.thePlayer.getEntityBoundingBox();
        double height = axisalignedbb.maxY - axisalignedbb.minY + 0.02,
                radius = axisalignedbb.maxX - axisalignedbb.minX;

        glPushMatrix();
        GlStateManager.disableCull();
        glEnable(GL_DEPTH_TEST);
//        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        glDisable(GL_TEXTURE_2D);
        glShadeModel(GL_SMOOTH);
        glEnable(GL_BLEND);
        GlStateManager.disableLighting();
        GlStateManager.color(1, 1, 1, 1);
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);

        float yaw = interpolate(mc.thePlayer.prevRotationYaw, mc.thePlayer.rotationYaw, getTimer().renderPartialTicks).floatValue();
        float pitchInterpolate = interpolate(mc.thePlayer.prevRenderArmPitch, mc.thePlayer.renderArmPitch, getTimer().renderPartialTicks).floatValue();

        glTranslated(posX, posY, posZ);
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glRotated(yaw, 0, -1, 0);
        glRotated(pitchInterpolate / 3.0, 0, 0, 0);
        glTranslatef(0, 0, pitchInterpolate / 270.0F);
        glLineWidth(2);
        glBegin(GL_LINE_LOOP);

        Pair<Color, Color> colors = Pair.of(LVMain.config.primaryColor.toJavaColor(), LVMain.config.secondaryColor.toJavaColor());
        boolean isRainbow = LVMain.config.isRainbow;

        // outline/border
        for (int i = 0; i <= 180; i++) {
            float value = (float) Math.sin((i * 4) * (PI / 180));
            Color color1;
            if (isRainbow) {
                color1 = rainbow(7, i * 4, 1, 1, .5f);
            } else {
                color1 = LVMain.config.isMovingColors ? mixColors(colors.getFirst(), colors.getSecond()) :
                        new Color(interpolateColor(colors.getFirst().getRGB(), colors.getSecond().getRGB(), value));
            }

            GlStateManager.color(1, 1, 1, 1);
            color(color1.getRGB());
            glVertex3d(
                    posX - Math.sin(i * PI2 / 90) * radius,
                    posY + height - (mc.thePlayer.isSneaking() ? 0.23 : 0) - 0.002,
                    posZ + Math.cos(i * PI2 / 90) * radius
            );
        }

        glEnd();

        glBegin(GL_TRIANGLE_FAN);
        int color12 = interpolateColor(colors.getFirst().getRGB(), colors.getSecond().getRGB(), 0.5f);
        int color12R = rainbow(7, 4, 1, 1, .7f).getRGB();
        color(!isRainbow ? color12 : color12R);
        glVertex3d(posX, posY + height + 0.3 - (mc.thePlayer.isSneaking() ? 0.23 : 0), posZ);

        // draw hat
        for (int i = 0; i <= 180; i++) {
            float value = (float) Math.sin((i * 4) * (PI / 180));
            Color color1;
            if (isRainbow) {
                color1 = rainbow(7, i * 4, 1, 1, .2f);
            } else {
                color1 = LVMain.config.isMovingColors ? mixColors(colors.getFirst(), colors.getSecond()) :
                        new Color(interpolateColorA(colors.getFirst().getRGB(), colors.getSecond().getRGB(), value));
            }

            GlStateManager.color(1, 1, 1, 1);
            color(color1.getRGB());
            glVertex3d(
                    posX - Math.sin(i * PI2 / 90) * radius,
                    posY + height - (mc.thePlayer.isSneaking() ? 0.23F : 0),
                    posZ + Math.cos(i * PI2 / 90) * radius
            );
        }

        glVertex3d(posX, posY + height + 0.3 - (mc.thePlayer.isSneaking() ? 0.23 : 0), posZ);
        glEnd();


        glPopMatrix();

        glEnable(GL_CULL_FACE);
        glEnable(GL_TEXTURE_2D);
        glShadeModel(GL_FLAT);
        glDepthMask(true);
        glEnable(GL_DEPTH_TEST);
    }
}
