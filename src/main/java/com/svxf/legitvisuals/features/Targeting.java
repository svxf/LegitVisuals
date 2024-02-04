package com.svxf.legitvisuals.features;

import com.svxf.legitvisuals.LVMain;
import com.svxf.legitvisuals.events.AttackEntityEvent;
import com.svxf.legitvisuals.utils.pair.Pair;
import com.svxf.legitvisuals.utils.TimerUtil;
import com.svxf.legitvisuals.utils.animations.Animation;
import com.svxf.legitvisuals.utils.animations.impl.DecelerateAnimation;
import com.svxf.legitvisuals.utils.animations.Direction;
import net.minecraft.entity.EntityLivingBase;
import net.weavemc.loader.api.event.RenderWorldEvent;
import net.weavemc.loader.api.event.SubscribeEvent;

import java.awt.*;

import static com.svxf.legitvisuals.utils.Utils.*;

public class Targeting {
    private EntityLivingBase target;
    private EntityLivingBase auraESPTarget;

    private final Animation auraESPAnim = new DecelerateAnimation(300, 1);
    private final TimerUtil targetTimer = new TimerUtil();

    @SubscribeEvent
    public void onAttackEvent(AttackEntityEvent event) {
        if (!LVMain.config.targetingEnabled || !LVMain.config.lvEnabled) return;
        if (event.getTarget() != null)
        {
            target = (EntityLivingBase) event.getTarget();
            targetTimer.reset();
        }
    }

    @SubscribeEvent
    public void onRender3DEvent(RenderWorldEvent event)
    {
        if (!LVMain.config.targetingEnabled || !LVMain.config.lvEnabled || target == null || !target.isEntityAlive()) {
            target = null;
            return;
        }

        if (targetTimer.hasTimeElapsed(5000)) {
            target = null;
        }

        auraESPAnim.setDirection(target != null ? Direction.FORWARDS : Direction.BACKWARDS);
        if(target != null) {
            auraESPTarget = target;
        }

        if(auraESPAnim.finished(Direction.BACKWARDS)) {
            auraESPTarget = null;
        }

        if (auraESPTarget != null) {
            float animationProgress = auraESPAnim.getOutput().floatValue();
            boolean isRainbow = LVMain.config.isRainbow;
            Pair<Color, Color> colors = Pair.of(LVMain.config.primaryColor.toJavaColor(), LVMain.config.secondaryColor.toJavaColor());

            Color finalColor;
            if (isRainbow) {
                finalColor = rainbow(7, (int) (animationProgress * 4), 1, 1, .5f);
            } else {
                finalColor = LVMain.config.isMovingColors ? mixColors(colors.getFirst(), colors.getSecond()) :
                        new Color(interpolateColor(colors.getFirst().getRGB(), colors.getSecond().getRGB(), animationProgress));
            }

            switch(LVMain.config.targetingStyle)
            {
                case 0:
                    drawCircle(auraESPTarget, event.getPartialTicks(), .75f, finalColor.getRGB(), animationProgress);
                    break;
                case 1:
                    drawTracerLine(auraESPTarget, 4f, Color.BLACK, auraESPAnim.getOutput().floatValue());
                    drawTracerLine(auraESPTarget, 2.5f, finalColor, auraESPAnim.getOutput().floatValue());
                    break;
                case 2:
                    renderBoundingBox(auraESPTarget, finalColor, auraESPAnim.getOutput().floatValue());
                    break;
            }
        }
    }
}
