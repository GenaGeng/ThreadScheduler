package scheduler.aser.scheduling.filtering;

//import edu.tamu.aser.scheduling.strategy.ChoiceType;

import scheduler.aser.scheduling.strategy.ChoiceType;

import java.util.SortedSet;

/**
 * Default {@link SchedulingFilter} that does not perform any filtering.
 */
public class DefaultFilter implements SchedulingFilter {

    @Override
    public SortedSet<? extends Object> filterChoices(SortedSet<? extends Object> choices, ChoiceType choiceType) {
        return choices;
    }

}
