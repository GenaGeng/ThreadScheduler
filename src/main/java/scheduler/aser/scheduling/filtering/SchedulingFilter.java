package scheduler.aser.scheduling.filtering;

//import edu.tamu.aser.scheduling.strategy.ChoiceType;

import scheduler.aser.scheduling.strategy.ChoiceType;

import java.util.SortedSet;

/**
 * Interface that can be implemented to specify a scheduling filter that
 * restricts scheduling choices.
 */
public interface SchedulingFilter {

    /**
     * Computes and returns a filtered {@link SortedSet} of choices.
     * 
     * @param choices
     *            the original {@link SortedSet} of choices.
     * @param choiceType
     *            the {@link ChoiceType}.
     * @return a filtered {@link SortedSet} of choices.
     */
    public SortedSet<? extends Object> filterChoices(SortedSet<? extends Object> choices, ChoiceType choiceType);
}
