/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        if (n == 0) return;
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        int i = 1;
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            if (i <= n) {
                rq.enqueue(s);
            }
            else {
                double k = StdRandom.uniformInt(1, i + 1);
                if (k <= n) {
                    rq.dequeue();
                    rq.enqueue(s);
                }
            }
            i++;
        }

        for (int i1 = 0; i1 < n; i1++) {
            System.out.println(rq.dequeue());
        }
    }
}
