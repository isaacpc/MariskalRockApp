package com.isaacpc.mariskalrock.utils;

import android.content.Context;
import android.view.animation.Animation;
import android.widget.ImageButton;

public class MRAnimationUtils {

        /**
         * Aplica al botón un efecto de alpha
         * 
         * @param button
         * @param animationFx
         */
        public static void setAlphaRadioButtons(Context context, ImageButton button, int animationFx) {

                final Animation animation = android.view.animation.AnimationUtils.loadAnimation(context, animationFx);

                if (animation == null) {
                        return; // here, we don't care
                }
                // reset initialization state

                animation.reset();
                // find View by its id attribute in the XML

                // cancel any pending animation and start this one
                if (button != null) {
                        button.clearAnimation();
                        animation.setFillAfter(true); // Tell it to persist after the
                        // animation ends
                        button.startAnimation(animation);
                }
        }
}
