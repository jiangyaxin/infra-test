package com.jyx.feature.test.jdk.algorithm;

import com.jyx.feature.test.jdk.algorithm.trace.*;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author jiangyaxin
 * @since 2022/4/12 20:15
 */
public class BackTraceTests {

    @Test
    public void fullPermuteTest() {
        assertThat(new L46FullPermute().permute(new int[]{1, 2, 3}))
                .as("fullPermuteTest")
                .isEqualTo(Lists.list(
                        Lists.list(1, 2, 3),
                        Lists.list(1, 3, 2),
                        Lists.list(2, 1, 3),
                        Lists.list(2, 3, 1),
                        Lists.list(3, 1, 2),
                        Lists.list(3, 2, 1)
                ));
    }

    @Test
    public void fullPermuteUniqueTest() {
        assertThat(new L47FullPermuteUnique().permuteUnique(new int[]{1, 1, 2, 2}))
                .as("fullPermuteUniqueTest")
                .isEqualTo(Lists.list(
                        Lists.list(1, 1, 2, 2),
                        Lists.list(1, 2, 1, 2),
                        Lists.list(1, 2, 2, 1),
                        Lists.list(2, 1, 1, 2),
                        Lists.list(2, 1, 2, 1),
                        Lists.list(2, 2, 1, 1)
                ));
    }

    @Test
    public void combineTest() {
        assertThat(new L77Combine().combine(4, 2))
                .as("combineTest")
                .isEqualTo(Lists.list(
                        Lists.list(1, 2),
                        Lists.list(1, 3),
                        Lists.list(1, 4),
                        Lists.list(2, 3),
                        Lists.list(2, 4),
                        Lists.list(3, 4)
                ));
    }

    @Test
    public void combinationSumTest() {
        assertThat(new L39_40_216CombinationSum().combinationSum(new int[]{2, 3, 6, 7}, 7))
                .as("combinationSumTest")
                .isEqualTo(Lists.list(
                        Lists.list(2, 2, 3),
                        Lists.list(7)
                ));
    }

    @Test
    public void combinationSum2Test() {
        assertThat(new L39_40_216CombinationSum().combinationSum2(new int[]{10, 1, 2, 7, 6, 1, 5}, 8))
                .as("combinationSum2Test")
                .isEqualTo(Lists.list(
                        Lists.list(1, 1, 6),
                        Lists.list(1, 2, 5),
                        Lists.list(1, 7),
                        Lists.list(2, 6)
                ));
    }

    @Test
    public void combinationSum3Test() {
        assertThat(new L39_40_216CombinationSum().combinationSum3(3, 7))
                .as("combinationSum3Test")
                .isEqualTo(Lists.list(
                        Lists.list(1, 2, 4)
                ));
    }

    @Test
    public void letterCombinationsTest() {
        assertThat(new L17LetterCombination().letterCombinations("23"))
                .as("letterCombinationsTest")
                .isEqualTo(Lists.list("ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"));
    }

    @Test
    public void partitionTest() {
        assertThat(new L131Partition().partition("aab"))
                .as("partitionTest")
                .isEqualTo(Lists.list(
                        Lists.list("a", "a", "b"),
                        Lists.list("aa", "b")
                ));
        assertThat(new L131Partition().partition("bb"))
                .as("partitionTest")
                .isEqualTo(Lists.list(
                        Lists.list("b", "b"),
                        Lists.list("bb")
                ));

    }

    @Test
    public void restoreIpAddressTest() {
        assertThat(new L93RestoreIpAddress().restoreIpAddresses("101023"))
                .as("partitionTest")
                .isEqualTo(Lists.list(
                        "1.0.10.23", "1.0.102.3", "10.1.0.23", "10.10.2.3", "101.0.2.3"
                ));
        assertThat(new L93RestoreIpAddress().restoreIpAddresses("0000"))
                .as("partitionTest")
                .isEqualTo(Lists.list(
                        "0.0.0.0"
                ));
    }

    @Test
    public void subsetTest() {
        assertThat(new L78_90Subset().subsets(new int[]{1, 2, 3}))
                .as("subsetTest")
                .isEqualTo(Lists.list(
                        Lists.emptyList(),
                        Lists.list(1),
                        Lists.list(1, 2),
                        Lists.list(1, 2, 3),
                        Lists.list(1, 3),
                        Lists.list(2),
                        Lists.list(2, 3),
                        Lists.list(3)
                ));
    }

    @Test
    public void subsetsWithDupTest() {
        assertThat(new L78_90Subset().subsetsWithDup(new int[]{1, 2, 2}))
                .as("subsetsWithDupTest")
                .isEqualTo(Lists.list(
                        Lists.emptyList(),
                        Lists.list(1),
                        Lists.list(1, 2),
                        Lists.list(1, 2, 2),
                        Lists.list(2),
                        Lists.list(2, 2)
                ));
        assertThat(new L78_90Subset().subsetsWithDup(new int[]{4, 4, 4, 1, 4}))
                .as("subsetsWithDupTest")
                .isEqualTo(Lists.list(
                        Lists.emptyList(),
                        Lists.list(1),
                        Lists.list(1, 4),
                        Lists.list(1, 4, 4),
                        Lists.list(1, 4, 4, 4),
                        Lists.list(1, 4, 4, 4, 4),
                        Lists.list(4),
                        Lists.list(4, 4),
                        Lists.list(4, 4, 4),
                        Lists.list(4, 4, 4, 4)
                ));
    }

    @Test
    public void solveNQueenTest() {
        assertThat(new L51SolveNQueen().solveNQueens(4))
                .as("solveNQueenTest")
                .isEqualTo(Lists.list(
                        Lists.list(".Q..", "...Q", "Q...", "..Q."),
                        Lists.list("..Q.", "Q...", "...Q", ".Q..")
                ));
    }

    @Test
    public void solveSudokuTest() {
        char[] char1 = new char[]{'5', '3', '.', '.', '7', '.', '.', '.', '.'};
        char[] char2 = new char[]{'6', '.', '.', '1', '9', '5', '.', '.', '.'};
        char[] char3 = new char[]{'.', '9', '8', '.', '.', '.', '.', '6', '.'};
        char[] char4 = new char[]{'8', '.', '.', '.', '6', '.', '.', '.', '3'};
        char[] char5 = new char[]{'4', '.', '.', '8', '.', '3', '.', '.', '1'};
        char[] char6 = new char[]{'7', '.', '.', '.', '2', '.', '.', '.', '6'};
        char[] char7 = new char[]{'.', '6', '.', '.', '.', '.', '2', '8', '.'};
        char[] char8 = new char[]{'.', '.', '.', '4', '1', '9', '.', '.', '5'};
        char[] char9 = new char[]{'.', '.', '.', '.', '8', '.', '.', '7', '9'};
        char[][] params = {char1, char2, char3, char4, char5, char6, char7, char8, char9};
        new L37SolveSudoku().solveSudoku(params);

        char[] result1 = new char[]{'5', '3', '4', '6', '7', '8', '9', '1', '2'};
        char[] result2 = new char[]{'6', '7', '2', '1', '9', '5', '3', '4', '8'};
        char[] result3 = new char[]{'1', '9', '8', '3', '4', '2', '5', '6', '7'};
        char[] result4 = new char[]{'8', '5', '9', '7', '6', '1', '4', '2', '3'};
        char[] result5 = new char[]{'4', '2', '6', '8', '5', '3', '7', '9', '1'};
        char[] result6 = new char[]{'7', '1', '3', '9', '2', '4', '8', '5', '6'};
        char[] result7 = new char[]{'9', '6', '1', '5', '3', '7', '2', '8', '4'};
        char[] result8 = new char[]{'2', '8', '7', '4', '1', '9', '6', '3', '5'};
        char[] result9 = new char[]{'3', '4', '5', '2', '8', '6', '1', '7', '9'};
        char[][] result = {result1, result2, result3, result4, result5, result6, result7, result8, result9};

        assertThat(params).as("solveSudokuTest").isEqualTo(result);
    }

    @Test
    public void findSubsequencesTest() {
        assertThat(new L491FindSubsequences().findSubsequences(new int[]{4, 6, 7, 7}))
                .as("findSubsequencesTest")
                .isEqualTo(Lists.list(
                        Lists.list(4, 6),
                        Lists.list(4, 6, 7),
                        Lists.list(4, 6, 7, 7),
                        Lists.list(4, 7),
                        Lists.list(4, 7, 7),
                        Lists.list(6, 7),
                        Lists.list(6, 7, 7),
                        Lists.list(7, 7)
                ));
    }

    @Test
    public void findItineraryTest() {
        assertThat(new L332FindItinerary().findItinerary(Lists.list(
                Lists.list("AXA", "AUA"), Lists.list("BNE", "ANU"), Lists.list("EZE", "ANU"), Lists.list("TIA", "JFK"), Lists.list("TIA", "BNE"), Lists.list("ANU", "BNE"), Lists.list("BNE", "AUA"),
                Lists.list("BNE", "ADL"), Lists.list("AXA", "ADL"), Lists.list("EZE", "AUA"), Lists.list("AUA", "AXA"), Lists.list("ADL", "AXA"), Lists.list("ADL", "TIA"), Lists.list("JFK", "ANU"),
                Lists.list("EZE", "JFK"), Lists.list("JFK", "AUA"), Lists.list("BNE", "EZE"), Lists.list("TIA", "ANU"), Lists.list("TIA", "AUA"), Lists.list("JFK", "TIA"), Lists.list("EZE", "ANU"),
                Lists.list("AXA", "JFK"), Lists.list("AUA", "OOL"), Lists.list("AUA", "AXA"), Lists.list("ANU", "BNE"), Lists.list("ANU", "EZE"), Lists.list("ANU", "TIA"), Lists.list("JFK", "EZE"),
                Lists.list("ADL", "ANU"), Lists.list("AXA", "BNE"), Lists.list("BNE", "ADL"), Lists.list("ANU", "EZE"), Lists.list("ANU", "JFK"), Lists.list("BNE", "AUA"), Lists.list("ANU", "AUA"),
                Lists.list("ANU", "AXA"), Lists.list("TIA", "BNE"), Lists.list("AUA", "EZE"), Lists.list("JFK", "ANU"), Lists.list("AXA", "TIA"), Lists.list("EZE", "ANU"), Lists.list("AUA", "BNE"),
                Lists.list("AUA", "AXA"), Lists.list("AUA", "TIA")
        ))).as("findItineraryTest")
                .isEqualTo(Lists.list("JFK", "ANU", "AUA", "AXA", "ADL", "ANU", "AXA", "AUA", "AXA", "BNE", "ADL", "AXA", "JFK", "ANU", "BNE", "ADL", "TIA", "ANU", "BNE", "ANU", "EZE", "ANU", "EZE", "ANU", "JFK", "AUA", "AXA", "TIA", "AUA", "BNE", "AUA", "EZE", "ANU", "TIA", "BNE", "AUA", "TIA", "BNE", "EZE", "JFK", "TIA", "JFK", "EZE", "AUA", "OOL"));
        assertThat(new L332FindItinerary().findItinerary(Lists.list(
                Lists.list("JFK", "SFO"),
                Lists.list("JFK", "ATL"),
                Lists.list("SFO", "ATL"),
                Lists.list("ATL", "JFK"),
                Lists.list("ATL", "SFO")
        ))).as("findItineraryTest")
                .isEqualTo(Lists.list("JFK", "ATL", "JFK", "SFO", "ATL", "SFO"));
        assertThat(new L332FindItinerary().findItinerary(Lists.list(
                Lists.list("MUC", "LHR"),
                Lists.list("JFK", "MUC"),
                Lists.list("SFO", "SJC"),
                Lists.list("LHR", "SFO")
        ))).as("findItineraryTest")
                .isEqualTo(Lists.list("JFK", "MUC", "LHR", "SFO", "SJC"));


    }

    @Test
    public void canPartitionSubsetTest() {
        assertThat(new L698CanPartitionSubset().canPartitionKSubsets(new int[]{1, 1, 1, 1, 2, 2, 2, 2}, 2))
                .as("canPartitionSubsetTest")
                .isEqualTo(true);
        assertThat(new L698CanPartitionSubset().canPartitionKSubsets(new int[]{4, 3, 2, 3, 5, 2, 1}, 4))
                .as("canPartitionSubsetTest")
                .isEqualTo(true);
    }

    @Test
    public void numIslandsTest() {
        char[] char1 = new char[]{'1', '1', '1', '1', '0'};
        char[] char2 = new char[]{'1', '1', '0', '1', '0'};
        char[] char3 = new char[]{'1', '1', '0', '0', '0'};
        char[] char4 = new char[]{'0', '0', '0', '0', '0'};
        char[][] params = {char1, char2, char3, char4};
        assertThat(new L200NumIslands().numIslands(params))
                .as("numIslandsTest")
                .isEqualTo(1);
    }

    @Test
    public void closedIslandTest() {
        assertThat(new L1254ClosedIsland().closedIsland(new int[][]{
                {1, 1, 1, 1, 1, 1, 1, 0},
                {1, 0, 0, 0, 0, 1, 1, 0},
                {1, 0, 1, 0, 1, 1, 1, 0},
                {1, 0, 0, 0, 0, 1, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 0}
        }))
                .as("closedIslandTest")
                .isEqualTo(2);

        assertThat(new L1254ClosedIsland().closedIsland(new int[][]{
                {0, 0, 1, 1, 0, 1, 0, 0, 1, 0},
                {1, 1, 0, 1, 1, 0, 1, 1, 1, 0},
                {1, 0, 1, 1, 1, 0, 0, 1, 1, 0},
                {0, 1, 1, 0, 0, 0, 0, 1, 0, 1},
                {0, 0, 0, 0, 0, 0, 1, 1, 1, 0},
                {0, 1, 0, 1, 0, 1, 0, 1, 1, 1},
                {1, 0, 1, 0, 1, 1, 0, 0, 0, 1},
                {1, 1, 1, 1, 1, 1, 0, 0, 0, 0},
                {1, 1, 1, 0, 0, 1, 0, 1, 0, 1},
                {1, 1, 1, 0, 1, 1, 0, 1, 1, 0}
        }))
                .as("closedIslandTest")
                .isEqualTo(5);
    }

    @Test
    public void numEnclavesTest() {
        assertThat(new L1020NumEnclaves().numEnclaves(new int[][]{
                {0, 1, 1, 0},
                {0, 0, 1, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 0}
        }))
                .as("numEnclavesTest")
                .isEqualTo(0);
    }

    @Test
    public void maxAreaOfIslandTest() {
        assertThat(new L695MaxAreaOfIsland().maxAreaOfIsland(new int[][]{
                {0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0},
                {0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0},
                {0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0}
        }))
                .as("maxAreaOfIslandTest")
                .isEqualTo(6);
    }

    @Test
    public void countSubIslandsTest() {
        assertThat(new L1905CountSubIslands().countSubIslands(
                new int[][]{
                        {1, 1, 1, 0, 0},
                        {0, 1, 1, 1, 1},
                        {0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                        {1, 1, 0, 1, 1}},
                new int[][]{
                        {1, 1, 1, 0, 0},
                        {0, 0, 1, 1, 1},
                        {0, 1, 0, 0, 0},
                        {1, 0, 1, 1, 0},
                        {0, 1, 0, 1, 0}}
        ))
                .as("countSubIslandsTest")
                .isEqualTo(3);
    }

    @Test
    public void generateParenthesisTest() {
        assertThat(new L22GenerateParenthesis().generateParenthesis(3))
                .as("generateParenthesisTest")
                .isEqualTo(Lists.list(
                        "((()))", "(()())", "(())()", "()(())", "()()()"
                ));
    }
}
