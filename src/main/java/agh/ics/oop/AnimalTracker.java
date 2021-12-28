package agh.ics.oop;

import java.util.Arrays;

public class AnimalTracker {
    private Animal animal;
    private int childrenCounterWhenChosen;
    private boolean isDead;
    private Integer deathDay;
    private Integer offspringCounter;

    public AnimalTracker() {
        this.animal = null;
    }

    public void addAnimal(Animal animal) {
        this.animal = animal;
        animal.setIsTracked(true);
        childrenCounterWhenChosen = animal.getChildrenCounter();
        this.isDead = false;
        this.offspringCounter = 0;
    }

    public String getChildrenCounter() {
        if (animal == null) {
            return "No animal tracked";
        }
        return "Children amount: " + (animal.getChildrenCounter() - childrenCounterWhenChosen);
    }

    public String getPosition() {
        if (animal == null) {
            return "No animal tracked";
        }
        return "Position: " + animal.getPosition().toString();
    }

    public String getGenotype() {
        if (animal == null) {
            return "No animal tracked";
        }
        return "Genotype: " + Arrays.toString(animal.getGenotype());
    }

    public Animal getAnimal() {
        return animal;
    }

    public String getDeathDay() {
        if (animal == null) {
            return "No animal tracked";
        }
        if (!isDead) {
            return "still alive";
        }
        return "Death day: " + deathDay.toString();
    }

    public void justDied(int deathDay) {
        isDead = true;
        this.deathDay = deathDay;
    }

    public void addToOffspringCounter() {
        offspringCounter++;
    }

    public String getOffspringCounter() {
        if (animal == null) {
            return "No animal tracked";
        }
        return "Offsprings amount: " + offspringCounter.toString();
    }
}
