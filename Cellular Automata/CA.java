public class CA {

    private int iterations;
    private Pattern[] patterns = {
            new Pattern(true, true, true),    // * * * -> ?
            new Pattern(true, true, false),   // * *   -> ?
            new Pattern(true, false, true),   // *   * -> ?
            new Pattern(true, false, false),  // *     -> ?
            new Pattern(false, true, true),   //   * * -> ?
            new Pattern(false, true, false),  //   *   -> ?
            new Pattern(false, false, true),  //     * -> ?
            new Pattern(false, false, false), //       -> ?
    };

    public CA(int iterations, int ruleNumber) {
        this.iterations = iterations;
        updatePatterns(ruleNumber);
    }

    private void updatePatterns(int ruleNumber) {
        String binary = Integer.toBinaryString(ruleNumber);
        while (binary.length() < 8) {
            binary = "0" + binary;
        }
        char[] chars = binary.toCharArray();
        for (int i = 0; i < patterns.length; i++) {
            if (chars[i] == '1') {
                patterns[i].setOutput(true);
            } else {
                patterns[i].setOutput(false);
            }
        }
    }

    public char[][] makeGrid() {
        char[][] grid = new char[iterations][iterations * 2 + 1];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = ' ';
            }
        }

        int left = iterations;
        int right = iterations;
        grid[0][iterations] = '*';
        for (int i = 1; i < iterations; i++) {
            left--;
            right++;
            for (int j = left; j <= right; j++) {
                boolean topLeft = grid[i - 1][j - 1] == '*';
                boolean top = grid[i - 1][j] == '*';
                boolean topRight = grid[i - 1][j + 1] == '*';
                for (Pattern pattern : patterns) {
                    if (pattern.match(topLeft, top, topRight)) {
                        if (pattern.getOutput()) {
                            grid[i][j] = '*';
                        }
                        break;
                    }
                }
            }
        }
        return grid;
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            exit("Wrong argument number, should be 2.");
        }

        int iterations = Integer.parseInt(args[0]);
        int ruleNumber = Integer.parseInt(args[1]);
        if (ruleNumber < 0 || ruleNumber > 255) {
            exit("Invalid rule number, should be 0-255.");
        }

        CA ca = new CA(iterations, ruleNumber);
        char[][] grid = ca.makeGrid();
        for (char[] row : grid) {
            for (char ch : row) {
                System.out.print(ch);
            }
            System.out.println();
        }
    }

    public static void exit(String message) {
        System.err.println(message);
        System.exit(-1);
    }
}

class Pattern {

    private boolean left, top, right, output;

    public Pattern(boolean left, boolean top, boolean right) {
        this.left = left;
        this.top = top;
        this.right = right;
    }

    public boolean match(boolean left, boolean top, boolean right) {
        return this.left == left && this.top == top && this.right == right;
    }

    public boolean getOutput() {
        return output;
    }

    public void setOutput(boolean output) {
        this.output = output;
    }
}