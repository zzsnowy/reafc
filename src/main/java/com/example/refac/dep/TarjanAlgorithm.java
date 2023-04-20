package com.example.refac.dep;

import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

/**
 * @Author: lilingj
 * @CreateTime: 2023-04-15  19:58
 * @Description: TODO
 * @Version: 1.0
 */

public class TarjanAlgorithm {
    int[] dfn;
    int[] low;
    boolean[] vis;
    int cnt;
    int n;
    Set<Integer> ans = new HashSet<>();
    Map<Integer, Set<Pair<Integer, Double>>> g;
    public TarjanAlgorithm(Map<Integer, Set<Pair<Integer, Double>>> g, int n) {
        this.g = g;
        this.n = n;
        this.dfn = new int[n];
        this.low = new int[n];
        this.vis = new boolean[n];
        this.cnt = 1;
    }

    public Set<Integer> getLoopNodes() {
        for (int id = 0; id < this.dfn.length; id++) {
            if (dfn[id] == 0) {
                tarjan(id, new ArrayDeque<>());
            }
        }
        return ans;
    }

    private void tarjan(int curId, Deque<Integer> stack) {
        dfn[curId] = low[curId] = cnt++;
        vis[curId] = true;
        stack.push(curId);
        for (Pair<Integer, Double> nxtNode : g.getOrDefault(curId, new HashSet<>())) {
            int nxtId = nxtNode.getLeft();
            if (dfn[nxtId] == 0) {
                tarjan(nxtId, stack);
                low[curId] = Math.min(low[curId], low[nxtId]);
            } else if (vis[nxtId]) {
                low[curId] = Math.min(low[curId], dfn[nxtId]);
            }
        }
        if (dfn[curId] == low[curId]) {
            List<Integer> staNodes = new ArrayList<>();
            int topId;
            do {
                topId = stack.pop();
                vis[topId] = false;
                staNodes.add(topId);
            } while (curId != topId);
            if (staNodes.size() > 1) {
                ans.addAll(staNodes);
            }
        }
    }

}
