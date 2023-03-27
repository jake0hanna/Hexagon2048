package SlideGame;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Random;

public class AliasMethod {
    private int n;
    private int[] alias;
    private double[] prob;
    private Random random;

    public AliasMethod(double[] probabilities) {
        this.n = probabilities.length;
        this.alias = new int[n];
        this.prob = new double[n];
        this.random = new Random();

        double avgProb = 1.0 / n;
        Deque<Integer> smaller = new ArrayDeque<>();
        Deque<Integer> larger = new ArrayDeque<>();

        for (int idx = 0; idx < n; ++idx) {
            if (probabilities[idx] > avgProb) {
                larger.add(idx);
            } else {
                smaller.add(idx);
            }
        }

        while (!smaller.isEmpty() && !larger.isEmpty()) {
            int small = smaller.poll(), large = larger.poll();
            prob[small] = probabilities[small] * n;
            alias[small] = large;
            probabilities[large] = probabilities[large] - (avgProb - probabilities[small]);
            if (probabilities[large] > avgProb) {
                larger.add(large);
            } else {
                smaller.add(large);
            }
        }

        for (int idx = 0; idx < n; ++idx) {
            prob[idx] = Math.min(1.0, prob[idx]);
        }
    }

    public int sample() {
        int idx = random.nextInt(n);
        return random.nextDouble() < prob[idx] ? idx : alias[idx];
    }
}
