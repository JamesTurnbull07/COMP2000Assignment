# COMP2000 Worksheet 2 — Final Submission

**Student name:** James Turnbull

**GitHub repo URL:** https://github.com/JamesTurnbull07/COMP2000Assignment 

---

## 1. Design Patterns

Don't forget to submit a pdf file which shows a diagram of your pattern implemenation to supplement this text description.

**1.1.** Which design pattern(s) did you implement? Name them and state where each appears in your codebase.

I implemented the Strategy Pattern for the animals movement. the strategy is the Movement strategy from the interface in MovementStrategy.java. It has 1 method and 3 strategies. These strategies are the King, Rook and Bishop. They were all written as lambdas. 




**1.2.** For each pattern, paste a code snippet showing the key interaction between participants.
``` java 
// Animal.java: the animal uses whatever strategy it was given
protected int[] stepToward(int dx, int dy) {
    return movement.step(dx, dy, speed);
}

// Hawk.java: the hawk chooses its strategy instead of overriding a method
public Hawk(int x, int y) {
    super(x, y, 150, 3, 5, MovementStrategy.BISHOP);
}
```
**1.3.** For each pattern, explain why you chose it over an alternative. What trade-off did you make?
In the week 7 submission, some animals had to override stepToward() to get their desired movement pattern. that meant that a subclass was being used to swap out a single method. and a new movement would have needed another override. Using strategy, you are able to give an animal a movement rather than it just being something that it is. This means that any animal can use any movement without a different subclass. I also considered using the State pattern for the hawk since it rests on every 4th tick. However this is just a counter rather than a behaviour that changes so it was not really needed. 




---

## 2. Streams / Lambdas / Parallelism

**2.1.** List every file in your project that uses lambda expressions or the Streams API.
MovementStrategy.java - King, Rook and Bishop are lambdas
Animal.java - findNearest() and nearestMate() use streams with filter() and min()
World.java - allEntities() uses Stream.of().flatMap(), removeDead() uses groupingBy() and counting(), plus removeIf() lambdas
Ablation.java - runs the 6 trials in a parallel stream
Predator.java - removeIf() lambda to ignore prey in the safe zone
Grid.java - lambdas passed in to search nearby cells
Main.java - lambda for the Swing Timer that runs each tick
Render.java, GridTest.java - lambdas in the test harnesses




**2.2.** For the most sophisticated use, paste the code and explain: what does it do, and why was a stream/lambda better than a loop?
``` java
public Map<String, DoubleSummaryStatistics> fedLevelBySpecies() {
    return Stream.of(hawks, foxes, rabbits, mice)
        .flatMap(List::stream)
        .filter(Entity::isAlive)
        .collect(Collectors.groupingBy(
            a -> a.getClass().getSimpleName(),
            LinkedHashMap::new,
            Collectors.summarizingDouble(a -> 100 * a.getHealth() / a.getMaxHealth())));
}

```
This method works out how well fed each species is and joins all 4 animal lists into 1 stream. It also keeps only the living animals and groups them by species. for each species, it also works out he count, min, max and average of how well fed they are. This is all shown in the fed column. If this was done with a loop it would require a different total min and count for each species. stream does this all in one go. 


**2.3.** Did you use parallel streams, custom collectors, or the Fork/Join framework? If yes, how did you ensure thread safety? If no, where could parallelism be introduced?

I used a parallel stream in ablation.java. Ablation runs the simulation 6 times. these runs all happen at the same time instead of one after each other. To make sure this was ok, i ensured the runs dont share anything. each run has it own world. One problem i found was printing. All of the results came up in a random order. to get around this, i needed to get each run to return their own result. 




---

## 3. Uniqueness and Creativity

**3.1.** What did you add to your simulation that went beyond the in-class activities? Describe the feature and what inspired it.
The main outside of class idea added was the safe zone, which I came up with in week 3. This adds a new element into the picture as predators can’t enter it and it acts as a safe haven for the prey. Previously I also added SuperFood, which is worth 3x normal food, as well as death by old age so the animals are not around forever. In this new submission I added a fed column. This shows how well fed each species is and how hungry the hungriest animal is. I did this because a species starts starving before its numbers actually drop, and I wanted to show this.



**3.2.** Paste a code snippet or describe a design decision that demonstrates emergent behaviour, non-trivial complexity, or a genuinely surprising outcome.

refuge ON  seed 918273645  survived  419 ticks  peakPrey=102 rebounds=0  births=184
refuge OFF seed 918273645  survived  792 ticks  peakPrey=106 rebounds=1  births=402
refuge ON  seed 555000111  survived  428 ticks  peakPrey=104 rebounds=0  births=159
refuge OFF seed 555000111  survived  981 ticks  peakPrey=97  rebounds=1  births=475
refuge ON  seed 777222333  survived  908 ticks  peakPrey=99  rebounds=0  births=479
refuge OFF seed 777222333  survived  861 ticks  peakPrey=95  rebounds=1  births=410




**3.3.** If you had another 6 weeks, what would you add or change?

If I had another 6 weeks I would add the observer pattern onto the display. Right now the main class tells the other panels to redraw after every tick. If observer was implemented, the world would announce each tick and the panels would respond. I also wanted to add a sort of evolution where babies would inherit their parents’ stats with some small changes. This would add adaptation and show elements of survival of the fittest. I also want to look at the safe zone and further investigate its effect on the experiment. 



---

## 4. Log Book and Final Reflection

**4.1.** Attach or link your log book entries for Weeks 7–13.

added into vs code



**4.2.** Reflect on the semester:

- What was the most important concept you learned?
The most important thing I learned this semester was that inheritance isntt always the best tool. In week 7 the hawk and fox each overrode a method just to change how they move. Moving that into a strategy meant any animal could be given any movement without needing a new class.

- What design trade-off did you face, and how did you resolve it?
The main trade off I faced was strategy versus subclasses. Strategy made movement more flexible, but not everything fit. The hawk’s resting depends on its own counter, so it stayed in the Hawk class. I resolved this by only using the strategy for behaviour that every animal has.

- If you started over, what would you do differently?
If I started over I would go to more of the classes and keep up with the project each week instead of doing a lot at the end. I would also use branches from the start instead of working straight on main.

- What feedback from the Week 7 submission did you act on?
The feedback I acted on was that my log book was too descriptive, so this time I tried to explain why I made each decision and what the trade offs were, like choosing Strategy over State.