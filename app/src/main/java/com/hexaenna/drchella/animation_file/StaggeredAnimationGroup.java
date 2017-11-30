package com.hexaenna.drchella.animation_file;

/**
 * Created by admin on 11/30/2017.
 */
import android.animation.TimeInterpolator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Group;
import android.support.transition.Fade;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.transition.TransitionSet;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import com.hexaenna.drchella.animation_file.Preconditions;



import static com.hexaenna.drchella.animation_file.Utils.getConstraintLayoutParent;
import static com.hexaenna.drchella.animation_file.Utils.notNull;

public class StaggeredAnimationGroup extends Group {

    @VisibleForTesting static final int DEFAULT_PARTIAL_DURATION = 250;
    @VisibleForTesting static final int DEFAULT_PARTIAL_TRANSITION_DELAY = 50;
    @VisibleForTesting static final TimeInterpolator DEFAULT_PARTIAL_INTERPOLATOR = new FastOutSlowInInterpolator();
    @VisibleForTesting static PartialTransitionFactory defaultPartialTransitionFactory =
            new PartialTransitionFactory() {
                @NonNull
                @Override
                public Transition createPartialTransition(boolean isShowing,
                                                          int viewId,
                                                          int indexInTransition) {
                    return new Fade();
                }
            };
    @VisibleForTesting static OnTransitionPreparedListener defaultOnPreparedListener =
            new OnTransitionPreparedListener() {
                @NonNull
                @Override
                public TransitionSet onStaggeredTransitionPrepared(@NonNull TransitionSet transitionSet,
                                                                   boolean isShowing,
                                                                   boolean inReversedOrder) {
                    return transitionSet;
                }
            };

    @VisibleForTesting int partialDelay = DEFAULT_PARTIAL_TRANSITION_DELAY;
    @VisibleForTesting int partialDuration = DEFAULT_PARTIAL_DURATION;
    @VisibleForTesting TimeInterpolator partialInterpolator = DEFAULT_PARTIAL_INTERPOLATOR;
    @VisibleForTesting PartialTransitionFactory partialTransitionFactory = defaultPartialTransitionFactory;
    @VisibleForTesting OnTransitionPreparedListener onPreparedListener = defaultOnPreparedListener;

    public StaggeredAnimationGroup(Context context) {
        super(context);
    }

    public StaggeredAnimationGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StaggeredAnimationGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @VisibleForTesting
    final int[] filterNonZeroIds(int[] allIds) {
        int nonZeroIdsCount = 0;
        for (int id : allIds) {
            if (id != 0) {
                nonZeroIdsCount++;
            }
        }
        if (nonZeroIdsCount == allIds.length) {
            return allIds;
        }
        int[] groupIds = new int[nonZeroIdsCount];
        for (int i = 0; i < nonZeroIdsCount; i++) {
            groupIds[i] = allIds[i];
        }
        return groupIds;
    }

    @VisibleForTesting
    final Transition prepareStaggeredTransition(boolean isShowing, boolean inReversedOrder) {
        TransitionSet staggeredTransition = new TransitionSet();
        int[] nonZeroIds = filterNonZeroIds(mIds);
        if (inReversedOrder) {
            for (int i = nonZeroIds.length - 1, iteration = 0; i >= 0; i--, iteration++) {
                int id = nonZeroIds[i];
                Transition basePartialTransition = preparePartialTransition(isShowing, id, iteration);
                addTransitionToStaggeredTransition(
                        basePartialTransition, staggeredTransition, id, iteration);
            }
        } else {
            for (int iteration = 0; iteration < nonZeroIds.length; iteration++) {
                int id = nonZeroIds[iteration];
                Transition basePartialTransition = preparePartialTransition(isShowing, id, iteration);
                addTransitionToStaggeredTransition(
                        basePartialTransition, staggeredTransition, id, iteration);
            }
        }
        return onStaggeredTransitionReady(staggeredTransition, isShowing, inReversedOrder);
    }

    @VisibleForTesting
    final void addTransitionToStaggeredTransition(Transition basePartialTransition,
                                                  TransitionSet staggeredTransition,
                                                  int viewId, int indexInTransition) {
        Transition partialTransition =
                applyStaggeredTransitionParams(basePartialTransition, viewId, indexInTransition);
        staggeredTransition.addTransition(partialTransition);
    }

    @VisibleForTesting
    final Transition applyStaggeredTransitionParams(Transition partialTransition,
                                                    int viewId, int indexInTransition) {
        partialTransition.setStartDelay(indexInTransition * partialDelay);
        partialTransition.addTarget(viewId);
        return partialTransition;
    }

    @VisibleForTesting
    final Transition preparePartialTransition(boolean isShowing, int id, int indexInTransition) {
        return partialTransitionFactory.createPartialTransition(isShowing, id, indexInTransition)
                .setDuration(partialDuration)
                .setInterpolator(partialInterpolator);
    }

    @VisibleForTesting
    final TransitionSet onStaggeredTransitionReady(TransitionSet transition, boolean isShowing, boolean inReversedOrder) {
        return onPreparedListener.onStaggeredTransitionPrepared(transition, isShowing, inReversedOrder);
    }

    public final void show() {
        show(false);
    }

    public final void show(boolean inReversedOrder) {
        ConstraintLayout parent = getConstraintLayoutParent(this);
        if (notNull(parent)) {
            Transition transition = prepareStaggeredTransition(true, inReversedOrder);
            TransitionManager.beginDelayedTransition(parent, transition);
            setVisibility(View.VISIBLE);
        }
    }

    public final void hide() {
        hide(false);
    }

    public final void hide(boolean inReversedOrder) {
        ConstraintLayout parent = getConstraintLayoutParent(this);
        if (notNull(parent)) {
            Transition transition = prepareStaggeredTransition(false, inReversedOrder);
            TransitionManager.beginDelayedTransition(parent, transition);
            setVisibility(View.GONE);
        }
    }

    public final void setPartialTransitionFactory(@NonNull PartialTransitionFactory factory) {
        Preconditions.checkNotNull(factory, "factory==null");
        partialTransitionFactory = factory;
    }

    public final void clearPartialTransitionFactory() {
        partialTransitionFactory = defaultPartialTransitionFactory;
    }

    public final void setOnTransitionPreparedListener(@NonNull OnTransitionPreparedListener listener) {
        Preconditions.checkNotNull(listener, "listener==null");
        onPreparedListener = listener;
    }

    public final void clearOnTransitionPreparedListener() {
        onPreparedListener = defaultOnPreparedListener;
    }

    public final void setPartialDuration(int partialDuration) {
        this.partialDuration = partialDuration;
    }

    public final void setPartialDelay(int partialDelay) {
        this.partialDelay = partialDelay;
    }

    public final void setPartialInterpolator(TimeInterpolator partialInterpolator) {
        this.partialInterpolator = partialInterpolator;
    }

    public interface PartialTransitionFactory {
        @NonNull
        Transition createPartialTransition(boolean show,
                                           int viewId,
                                           int indexInTransition);
    }

    public interface OnTransitionPreparedListener {
        @NonNull
        TransitionSet onStaggeredTransitionPrepared(@NonNull TransitionSet transitionSet,
                                                    boolean show,
                                                    boolean inReversedOrder);
    }
}
