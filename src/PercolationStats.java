import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

	private final int trials;
	private final double thresholds[];
	private final double conf = 1.96;

	public PercolationStats(int n, int trials) { // perform trials independent experiments on an n-by-n grid
		if (n <= 0) {
			throw new IllegalArgumentException();
		}
		if (trials <= 0) {
			throw new IllegalArgumentException();
		}

		this.trials = trials;
		this.thresholds = new double[this.trials];
		for (int t = 0; t < this.trials; t++) {
			Percolation p = new Percolation(n);
			while (!p.percolates()) {
				int row = StdRandom.uniform(1, n + 1);
				int col = StdRandom.uniform(1, n + 1);
				p.open(row, col);
			}
			this.thresholds[t] = ((double) p.numberOfOpenSites()) / ((double) n * n);
		}
	}

	public double mean() { // sample mean of percolation threshold
		return StdStats.mean(thresholds);
	}

	public double stddev() { // sample standard deviation of percolation threshold
		if (this.trials == 1) {
			return Double.NaN;
		}

		return StdStats.stddev(thresholds);
	}

	public double confidenceLo() { // low endpoint of 95% confidence interval
		return this.mean() - (this.conf * this.stddev() / Math.sqrt(this.trials));
	}

	public double confidenceHi() { // high endpoint of 95% confidence interval
		return this.mean() + (this.conf * this.stddev() / Math.sqrt(this.trials));
	}

	public static void main(String[] args) { // test client (described below)
		int n = Integer.parseInt(args[0]);
		int trials = Integer.parseInt(args[1]);
		PercolationStats ps = new PercolationStats(n, trials);
		System.out.println("mean                    = " + ps.mean());
		System.out.println("stddev                  = " + ps.stddev());
		System.out.println("95% confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
	}
}