/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Test {
    public static void main(String[] args) {
        Deque<String> dq = new Deque<>();
        while (!StdIn.isEmpty()) {
            try {
                String str = StdIn.readString();
                char op = str.charAt(0);
                String arg = str.length() > 1 ? str.substring(1) : null;
                if (op == '+') {
                    dq.addFirst(arg);
                }
                else if (op == '-') {
                    dq.removeFirst();
                }
                else if (op == '.') {
                    dq.addLast(arg);
                }
                else if (op == ',') {
                    dq.removeLast();
                }
                else if (op == 'n') {
                    ncalls(Integer.parseInt(arg));
                }
                else if (op == 'r') {
                    randomcalls(Integer.parseInt(arg));
                }
                else if (op == 'q') {
                    dq = new Deque<>();
                }

                StringBuilder sb = new StringBuilder();
                for (String s : dq) {
                    sb.append(s).append(" ");
                }
                System.out.println(sb.toString().trim());
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    static void ncalls(int n) {
        Deque<String> dq = new Deque<>();
        try {
            for (int i = 0; i < n; i++) {
                dq.addFirst(Integer.toString(i));
            }
            for (int i = 0; i < n; i++) {
                System.out.println(dq.removeFirst());
            }
            StdOut.println("ncalls = " + n);
        }
        catch (Exception e) {
            StdOut.println(e.getMessage());
            e.printStackTrace();
        }

    }

    static void randomcalls(int n) {
        Deque<String> dq = new Deque<>();
        try {
            for (int i = 0; i < n; i++) {
                int k = (int) (StdRandom.uniformInt(98) % 2);
                StdOut.println("num" + i);
                if (k == 0) {
                    StdOut.println('+');
                    dq.addFirst(Integer.toString(i));
                }
                else if (!dq.isEmpty()) {
                    StdOut.println('-' + dq.removeLast());
                    StringBuilder sb = new StringBuilder();
                    for (String s : dq) {
                        sb.append(s).append(" ");
                    }
                    StdOut.println(sb);
                }
            }
            StdOut.println("ncalls = " + n);
        }
        catch (Exception e) {
            StdOut.println(e.getMessage());
            e.printStackTrace();
        }

    }
}
