import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

	private final int n;
	private boolean[][] grid;
	private int numberOfOpenSites;
	private final WeightedQuickUnionUF wquf;

	public Percolation(int n) { // create n-by-n grid, with all sites blocked
		if (n < 1) {
			throw new IllegalArgumentException("Number of sites mustbe greater than 0.");
		}

		this.n = n;
		this.numberOfOpenSites = 0;
		this.wquf = new WeightedQuickUnionUF(n * n + 2);
		this.grid = new boolean[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				this.grid[i][j] = false;
			}
		}
	}

	public void open(int row, int col) { // open site (row, col) if it is not open already
		validateIndices(row, col);

		if (!isOpen(row, col)) {
			this.grid[row - 1][col - 1] = true;
			this.numberOfOpenSites++;

			int p = this.xyTo1D(row, col);

			if (row == 1) {
				this.wquf.union(p, 0);
			}

			if (row == this.n) {
				this.wquf.union(p, (this.n * this.n) + 1);
			}

			if (row - 1 > 0 && isOpen(row - 1, col)) {
				int q = this.xyTo1D(row - 1, col);
				this.wquf.union(p, q);
			}
			if (row + 1 <= n && isOpen(row + 1, col)) {
				int q = this.xyTo1D(row + 1, col);
				this.wquf.union(p, q);
			}
			if (col - 1 > 0 && isOpen(row, col - 1)) {
				int q = this.xyTo1D(row, col - 1);
				this.wquf.union(p, q);
			}
			if (col + 1 <= n && isOpen(row, col + 1)) {
				int q = this.xyTo1D(row, col + 1);
				this.wquf.union(p, q);
			}
		}
	}

	public boolean isOpen(int row, int col) {// is site (row, col) open?
		validateIndices(row, col);
		return grid[row - 1][col - 1];
	}

	public boolean isFull(int row, int col) { // is site (row, col) full?
		validateIndices(row, col);
		int p = this.xyTo1D(row, col);
		return this.wquf.connected(p, 0);
	}

	public int numberOfOpenSites() { // number of open sites
		return this.numberOfOpenSites;
	}

	public boolean percolates() { // does the system percolate?
		return this.wquf.connected(0, n * n + 1);
	}

	private void validateIndices(int row, int col) {
		if (row < 1 || col < 1) {
			throw new IllegalArgumentException("Site must have a location.");
		}

		if (row > this.n || col > this.n) {
			throw new IllegalArgumentException("Site location must be inside grid.");
		}
	}

	private int xyTo1D(int row, int col) {
		return this.n * (row - 1) + col;
	}

	public static void main(String[] args) { // test client (optional)
		int n = 5;
		Percolation p = new Percolation(n);

		while (!p.percolates()) {
			int row = StdRandom.uniform(1, n + 1);
			int col = StdRandom.uniform(1, n + 1);
			p.open(row, col);
		}
		boolean[][] g = p.grid;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				System.out.print(g[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("open sites = " + p.numberOfOpenSites());
	}
}