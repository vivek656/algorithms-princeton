/* *****************************************************************************
 *  Name: Vivek Latwal
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    
    private WeightedQuickUnionUF ufTop;
    private WeightedQuickUnionUF ufBottom;
    private int n;
    private boolean[] sites;
    private int totalOpenSites = 0;
    private int topSpotIdx;
    private int bottomSpotIdx;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 1) throw new IllegalArgumentException("n must be >= 1");
        ufTop = new WeightedQuickUnionUF((n * n) + 2);
        ufBottom = new WeightedQuickUnionUF((n * n) + 2);
        sites = new boolean[(n * n) + 2];
        sites[(n * n) + 1] = true;
        topSpotIdx = (n * n + 1);
        bottomSpotIdx = (n * n) + 2;
        sites[bottomSpotIdx - 1] = true;
        sites[topSpotIdx - 1] = true;
        this.n = n;
    }

    private int indx(int row, int col) {
        return (row - 1) * n + (col);
    }

    private boolean getSiteAt(int idx) {
        return sites[idx - 1];
    }

    private boolean isOpen(int idx) {
        return getSiteAt(idx);
    }

    private void setSiteOpen(int idx) {
        boolean curr = getSiteAt(idx);
        sites[idx - 1] = true;
        if (!curr) {
            totalOpenSites++;
        }
    }


    private int checkOrGet(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) throw new IllegalArgumentException();
        return indx(row, col);
    }

    private int getorNullOrElseWithTopAndBottom(int row, int col) {
        if (row < 1) return topSpotIdx;
        if (row > n) return bottomSpotIdx;
        if (col < 1 || col > n) return -1;
        return indx(row, col);
    }

    private int[] getNeighbour(int row, int col) {
        return new int[] {
                getorNullOrElseWithTopAndBottom(row - 1, col),
                getorNullOrElseWithTopAndBottom(row, col + 1),
                getorNullOrElseWithTopAndBottom(row + 1, col),
                getorNullOrElseWithTopAndBottom(row, col - 1)
        };
    }

    private void checkAndUnion(int idx1, int unionWith) {
        if (unionWith == -1) return;
        if (!isOpen(unionWith) || isConnected(idx1, unionWith)) return;
        union(idx1, unionWith);
    }

    private void union(int idx1, int idx2) {
        ufBottom.union(idx1 - 1, idx2 - 1);
        if (idx1 != bottomSpotIdx && idx2 != bottomSpotIdx) {
            ufTop.union(idx1 - 1, idx2 - 1);
        }
    }

    private boolean isConnected(int idx1, int idx2) {
        int uf1 = ufTop.find(idx1 - 1);
        int uf2 = ufTop.find(idx2 - 1);
        return uf1 == uf2;
    }

    private boolean isConnectedBootom(int idx1, int idx2) {
        int uf1 = ufBottom.find(idx1 - 1);
        int uf2 = ufBottom.find(idx2 - 1);
        return uf1 == uf2;
    }


    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        int idx = checkOrGet(row, col);
        int[] neighbours = getNeighbour(row, col);
        setSiteOpen(idx);
        checkAndUnion(idx, neighbours[0]);
        checkAndUnion(idx, neighbours[1]);
        checkAndUnion(idx, neighbours[2]);
        checkAndUnion(idx, neighbours[3]);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return isOpen(checkOrGet(row, col));
    }

    public boolean isFull(int row, int col) {
        return isConnected(checkOrGet(row, col), topSpotIdx);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return totalOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return isConnectedBootom(topSpotIdx, bottomSpotIdx);
    }

    // test client (optional)
    public static void main(String[] args) {

    }
}
