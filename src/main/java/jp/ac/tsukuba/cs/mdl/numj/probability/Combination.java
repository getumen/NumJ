package jp.ac.tsukuba.cs.mdl.numj.probability;

public class Combination {

    public static long combination(int n, int k) {
        if (n == k) {
            return 1;
        }
        if (k == 0) {
            return 1;
        }
        long[][] memo = new long[n][k];
        return combination(n - 1, k, memo) + combination(n - 1, k - 1, memo);
    }

    private static long combination(int n, int k, long[][] memo) {
        if (n == k) {
            return 1;
        }
        if (k == 0) {
            return 1;
        }
        if (memo[n - 1][k - 1] > 0) {
            return memo[n - 1][k - 1];
        }
        memo[n - 1][k - 1] = combination(n - 1, k, memo) + combination(n - 1, k - 1, memo);
        return memo[n - 1][k - 1];
    }

    public static long permutation(int n, int k) {
        long result = 1;
        for (int i = 0; i < k; i++) {
            result *= (n - i);
        }
        return result;
    }
}
