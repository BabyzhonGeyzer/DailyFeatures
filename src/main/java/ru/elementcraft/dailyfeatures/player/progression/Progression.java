package ru.elementcraft.dailyfeatures.player.progression;

import lombok.Getter;
import lombok.Setter;
@Getter
public class Progression {

    private int progression;
    @Setter
    private boolean isAchieved;

    /**
     * Progression constructor.
     *
     * @param progression progression of quest.
     * @param isAchieved  status of quest.
     */
    public Progression(int progression, boolean isAchieved) {
        this.progression = progression;
        this.isAchieved = isAchieved;
    }

    /**
     * Increment the progression of quest.
     */
    public void increaseProgression() {
        this.progression++;
    }

}
