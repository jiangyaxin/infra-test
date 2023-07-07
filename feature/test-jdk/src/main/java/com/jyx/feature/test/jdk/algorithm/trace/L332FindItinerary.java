package com.jyx.feature.test.jdk.algorithm.trace;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * 力扣 332. 重新安排行程
 * 给你一份航线列表 tickets ，其中 tickets[i] = [fromi, toi] 表示飞机出发和降落的机场地点。请你对该行程进行重新规划排序。
 *
 * 所有这些机票都属于一个从 JFK（肯尼迪国际机场）出发的先生，所以该行程必须从 JFK 开始。如果存在多种有效的行程，请你按字典排序返回最小的行程组合。
 *
 * 例如，行程 ["JFK", "LGA"] 与 ["JFK", "LGB"] 相比就更小，排序更靠前。
 * 假定所有机票至少存在一种合理的行程。且所有的机票 必须都用一次 且 只能用一次。
 *
 * 示例 1：
 * 输入：tickets = [["MUC","LHR"],["JFK","MUC"],["SFO","SJC"],["LHR","SFO"]]
 * 输出：["JFK","MUC","LHR","SFO","SJC"]
 *
 * 示例 2：
 * 输入：tickets = [["JFK","SFO"],["JFK","ATL"],["SFO","ATL"],["ATL","JFK"],["ATL","SFO"]]
 * 输出：["JFK","ATL","JFK","SFO","ATL","SFO"]
 * 解释：另一种有效的行程是 ["JFK","SFO","ATL","JFK","ATL","SFO"] ，但是它字典排序更大更靠后。
 *
 * 提示：
 * 1 <= tickets.length <= 300
 * tickets[i].length == 2
 * fromi.length == 3
 * toi.length == 3
 * fromi 和 toi 由大写英文字母组成
 * fromi != toi
 *
 * @author jiangyaxin
 * @since 2022/4/16 21:17
 */
public class L332FindItinerary {

    private static final String START = "JFK";

    public List<String> result = new ArrayList<>();

    public List<String> findItinerary(List<List<String>> tickets) {
        tickets.sort(Comparator.comparing((List<String> ticket) -> ticket.get(0)).thenComparing(ticket -> ticket.get(1)));

        List<String> path = new ArrayList<>();
        boolean[] used = new boolean[tickets.size()];
        path.add(START);
        findItinerary(START,path,0,used,tickets);

        return result;
    }

    public void findItinerary(String currentPosition, List<String> path, int usedCount, boolean[] used, List<List<String>> tickets) {
        if(usedCount == tickets.size()){
            result = new ArrayList<>(path);
            return;
        }

        for(int i = 0 ; i < tickets.size() ; i++){
            List<String> ticket = tickets.get(i);
            if(used[i] || !Objects.equals(ticket.get(0),currentPosition)){
                continue;
            }

            String nextPosition = ticket.get(1);

            path.add(nextPosition);
            used[i] = true;

            findItinerary(nextPosition,path,usedCount + 1,used,tickets);

            if(result.size() > 0){
                return;
            }
            path.remove(path.size()-1);
            used[i] = false;
        }
    }

}
