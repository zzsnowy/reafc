package com.example.refac.dep;

import org.apache.commons.lang3.tuple.Pair;
import org.checkerframework.checker.units.qual.C;

import java.util.*;

/**
 * @Author: lilingj
 * @CreateTime: 2023-04-15  19:58
 * @Description: TODO
 * @Version: 1.0
 */

public class TarjanAlgorithm {
    static class RadialData {
        // edgeSymbol: ['arrow'],
        //         edgeSymbolSize: 5,
        List<ColorNode> nodes;
        List<ColorEdge> links;

        List<Category> categories;

        public RadialData(List<ColorNode> nodes, List<ColorEdge> links, List<Category> categories) {
            this.nodes = nodes;
            this.links = links;
            this.categories = categories;
        }
    }

    static class Category {
        String name;

        public Category(String name) {
            this.name = name;
            // this.base = base;
        }

        // String base;
    }

    static class ColorNode {
        String id;
        String name;
        Integer value;
        Integer category;
        // ItemStyle itemStyle = new ItemStyle();

        static class ItemStyle {
            String color = "red";
        }
    }

    static class ColorEdge {
        String source;
        String target;

        public ColorEdge(Integer source, Integer target) {
            this.source = String.valueOf(source);
            this.target = String.valueOf(target);
        }
    }
    int[] dfn;
    int[] low;
    boolean[] vis;
    int cnt;
    int n;
    Set<Integer> ans = new HashSet<>();
    Map<Integer, Set<Pair<Integer, Double>>> g;
    int colorCount = 0;
    Map<Integer, Set<Integer>> colorG = new HashMap<>();
    int[] nodeIdColorMap;
    Map<Integer, List<Integer>> colorNodeIdMap = new HashMap<>();

    RadialData radialData;
    public TarjanAlgorithm(Map<Integer, Set<Pair<Integer, Double>>> g, int n) {
        this.g = g;
        this.n = n;
        this.nodeIdColorMap = new int[n];
        this.dfn = new int[n];
        this.low = new int[n];
        this.vis = new boolean[n];
        this.cnt = 1;
        for (int id = 0; id < this.dfn.length; id++) {
            if (dfn[id] == 0) {
                tarjan(id, new ArrayDeque<>());
            }
        }
        genColorMap();
        genRadialData();
    }

    public Set<Integer> getLoopNodes() {
        return ans;
    }

    public Map<Integer, List<Integer>> getColorNodeIdMap() {
        return colorNodeIdMap;
    }

    public int getColorCnt() {
        return colorCount;
    }

    public int[] getNodeIdColorMap() {
        return nodeIdColorMap;
    }

    public Map<Integer, Set<Integer>> getColorGraph() {
        return colorG;
    }

    public RadialData getRadialData() {
        return radialData;
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
            for (Integer node : staNodes) {
                nodeIdColorMap[node] = colorCount;
            }
            this.colorNodeIdMap.put(this.colorCount, staNodes);
            this.colorCount++;
            if (staNodes.size() > 1) {
                ans.addAll(staNodes);
            }
        }
    }

    private void genColorMap() {
        for (int nodeId = 0; nodeId < n; nodeId++) {
            Set<Pair<Integer, Double>> nxtPairs = this.g.getOrDefault(nodeId, new HashSet<>());
            Integer curNodeColor = nodeIdColorMap[nodeId];
            for (Pair<Integer, Double> nxtPair : nxtPairs) {
                Integer nxtNodeId = nxtPair.getLeft();
                Integer nxtNodeColor = nodeIdColorMap[nxtNodeId];
                if (curNodeColor.equals(nxtNodeColor)) {
                    continue;
                }

                var nxtColors = this.colorG.getOrDefault(curNodeColor, new HashSet<>());
                nxtColors.add(nxtNodeColor);
                this.colorG.put(curNodeColor, nxtColors);
            }
        }
    }

    private void genRadialData() {
        boolean[] vis = new boolean[colorCount];
        List<Category> categories = new ArrayList<>();
        List<ColorNode> colorNodes = new ArrayList<>();
        List<ColorEdge> colorEdges = new ArrayList<>();
        Set<Integer> allNode = getLoopNodes();
        categories.add(new Category("single"));
        for (int color = 0; color < colorCount; color++) {
            var nodeIds = colorNodeIdMap.getOrDefault(color, new ArrayList<>());
            if (nodeIds.size() == 1) {
                continue;
            }
            for (Integer nodeId : nodeIds) {
                var colorNode = new ColorNode();
                colorNode.name = "cycle-" + color + "node-" + nodeId;
                colorNode.value = 1;
                colorNode.id = String.valueOf(nodeId);
                colorNode.category = categories.size();
                colorNodes.add(colorNode);
            }
            categories.add(new Category("cycle-" + color));
        }
        allNode.forEach(nodeId -> {
            var nxtNodes = this.g.getOrDefault(nodeId, new HashSet<>());
            nxtNodes.forEach(nxtPair -> {
                var nxtNode = nxtPair.getLeft();
                if (allNode.contains(nxtNode) && this.nodeIdColorMap[nodeId] == this.nodeIdColorMap[nxtNode]) {
                    colorEdges.add(new ColorEdge(nodeId, nxtNode));
                }
            });
        });
//        this.colorG.forEach((color, nxtColors) -> {
//            nxtColors.forEach(nxtColor -> {
//                colorEdges.add(new ColorEdge(color, nxtColor));
//            });
//        });
        this.radialData = new RadialData(colorNodes, colorEdges, categories);
    }

//    private void genDagGraph(ColorNode[] colorNodes, boolean[] vis, int color) {
//        if (vis[color]) {
//            return;
//        }
//        vis[color] = true;
//        ColorNode colorNode = colorNodes[color];
//        Set<Integer> nxtColors = this.colorG.getOrDefault(color, new HashSet<>());
//        colorNode.children = nxtColors.stream().map(colorId -> colorNodes[colorId]).collect(Collectors.toList());
//        for (int child : nxtColors) {
//            genDagGraph(colorNodes, vis, child);
//        }
//    }
//
//    private List<Integer> topoSort() {
//        int[] in = new int[colorCount];
//        boolean[] vis = new boolean[colorCount];
//        for (int color = 0; color < colorCount; color++) {
//            var nxtColors = this.colorG.getOrDefault(color, new HashSet<>());
//            nxtColors.forEach(nxtColor -> in[nxtColor]++);
//        }
//        List<Integer> sortColor = new ArrayList<>();
//        boolean goon = true;
//        while (goon) {
//            goon = false;
//            for (int i = 0; i < in.length; i++) {
//                if (in[i] != 0) {
//                    goon = true;
//                    continue;
//                }
//                if (vis[i]) {
//                    continue;
//                }
//                sortColor.add(i);
//                vis[i] = true;
//                var nxtColors = this.colorG.getOrDefault(i, new HashSet<>());
//                nxtColors.forEach(nxtColor -> in[nxtColor]--);
//            }
//        }
//        return sortColor;
//    }



}
