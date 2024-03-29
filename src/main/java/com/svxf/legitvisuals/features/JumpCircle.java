package com.svxf.legitvisuals.features;

import com.svxf.legitvisuals.LVMain;
import com.svxf.legitvisuals.events.MotionEvent;
import com.svxf.legitvisuals.utils.animations.Animation;
import com.svxf.legitvisuals.utils.animations.Direction;
import com.svxf.legitvisuals.utils.animations.impl.DecelerateAnimation;
import com.svxf.legitvisuals.utils.pair.Pair;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.weavemc.loader.api.event.RenderWorldEvent;
import net.weavemc.loader.api.event.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.svxf.legitvisuals.utils.Utils.*;
import static org.lwjgl.opengl.GL11.*;

public class JumpCircle {
    private boolean inAir = false;

    private final List<Circle> circles = new ArrayList<>();
    private final List<Circle> toRemove = new ArrayList<>();

    @SubscribeEvent
    public void onMotionEvent(MotionEvent event)
    {
        if (!LVMain.config.jumpCirclesEnabled || !LVMain.config.lvEnabled) return;
        if (!event.isOnGround() && !inAir) {
            if (LVMain.config.jumpCircleSpawning == 1)
            {
                // did a raycast it so i know the y position they jumped from
                // otherwise it will spawn it in the air (which is ugly)
                BlockPos jumpBlockPos = raycastToBlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
                if (jumpBlockPos != null) {
                    circles.add(new Circle(mc.thePlayer.posX, jumpBlockPos.getY() + 1.0, mc.thePlayer.posZ));
                }
            }
            inAir = true;
        } else if (event.isOnGround() && inAir) {
            if (LVMain.config.jumpCircleSpawning == 0)
                circles.add(new Circle(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ));
            inAir = false;
        }
    }

    @SubscribeEvent
    public void onRenderWorldEvent(RenderWorldEvent event) {
        if (!LVMain.config.jumpCirclesEnabled || !LVMain.config.lvEnabled)
        {
            circles.clear();
            toRemove.clear();
            return;
        }

        Iterator<Circle> iterator = circles.iterator();

        while (iterator.hasNext()) {
            Circle circle = iterator.next();
            circle.drawCircle();

            if (circle.fadeAnimation.finished(Direction.BACKWARDS)) {
                iterator.remove();
            }
        }
    }

    private BlockPos raycastToBlockPos(double startX, double startY, double startZ) {
        BlockPos blockPos = new BlockPos(startX, startY, startZ);
        while (blockPos.getY() > 0) {
            blockPos = blockPos.down();
            Block block = mc.theWorld.getBlockState(blockPos).getBlock();
            if (block != Blocks.air && block != Blocks.water && block != Blocks.lava) {
                return blockPos;
            }
        }
        return null;
    }

    private static class Circle {
        private final float x, y, z;
        private final Animation expandAnimation;
        private final Animation fadeAnimation;

        public Circle(double x, double y, double z) {
            this.x = (float) x;
            this.y = (float) y;
            this.z = (float) z;
            this.fadeAnimation = new DecelerateAnimation((int) (LVMain.config.jumpCircleFadeSpeed * 100), 1);
            this.expandAnimation = new DecelerateAnimation((int) (LVMain.config.jumpCircleExpandSpeed * 100), LVMain.config.jumpCircleRadius);
        }

        public void drawCircle() {
            Pair<Color, Color> colors = Pair.of(LVMain.config.primaryColor.toJavaColor(), LVMain.config.secondaryColor.toJavaColor());
            boolean isRainbow = LVMain.config.isRainbow;

            if (expandAnimation.getOutput() > (LVMain.config.jumpCircleRadius) * .7f) {
                fadeAnimation.setDirection(Direction.BACKWARDS);
            }

            glPushMatrix();
            setAlphaLimit(0);

            float animation = expandAnimation.getOutput().floatValue();
            float fade = fadeAnimation.getOutput().floatValue();
            setup2DRendering();
//            glDisable(GL_DEPTH_TEST);
            glEnable(GL_DEPTH_TEST);
            glDepthMask(false);
            glShadeModel(GL_SMOOTH);
            double pi2 = PI2;

            double xVal = x - mc.getRenderManager().viewerPosX;
            double yVal = (y - mc.getRenderManager().viewerPosY) + 0.01f;
            double zVal = z - mc.getRenderManager().viewerPosZ;

            glBegin(GL_TRIANGLE_STRIP);

            Color color1 = colors.getSecond();
            Color color2 = colors.getFirst();

            float newAnim = (float) Math.max(0, animation - (.3f * (animation / expandAnimation.getEndPoint())));

            for (int i = 0; i <= 90; ++i) {
                float value = (float) Math.sin((i * 4) * (PI / 180));
                int color;

                if (isRainbow) {
                    color = rainbow(7, i * 4, 1, 1, fade * .6f).getRGB();
                } else {
                    color = LVMain.config.isMovingColors ? mixColors(color1, color2).getRGB() :
                            interpolateColor(color1.getRGB(), color2.getRGB(), Math.abs(value));
                }

                color(color, fade * .6f);
                glVertex3d(xVal + animation * Math.cos(i * pi2 / 45), yVal, zVal + animation * Math.sin(i * pi2 / 45));

                color(color, fade * .15f);
                glVertex3d(xVal + newAnim * Math.cos(i * pi2 / 45), yVal, zVal + newAnim * Math.sin(i * pi2 / 45));
            }

            glEnd();


            glShadeModel(GL_FLAT);
            glDepthMask(true);
            glEnable(GL_DEPTH_TEST);
            end2DRendering();
            glPopMatrix();
            resetColor();
            color(-1);
        }
    }
}
