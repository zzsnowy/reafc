package com.example.refac.dep;

import com.google.common.io.Files;
import com.google.gson.Gson;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: lilingj
 * @CreateTime: 2023-04-15  18:35
 * @Description: TODO
 * @Version: 1.0
 */

public class DepSniffer {

    public double TH_UDN = 0.1;

    public double TH_ADN_FAC = 1 / 2.0;
    public double TH_EDN_FAC = 1 / 2.0;

    public static class StatsInfo {
        public List<Node> loopDepNodes;
        public List<Node> unstableNodes;
        public List<Node> hublikeNodes;
        public List<Node> allNodes;
        public int colorCount = 0;
        public Map<Integer, Set<Integer>> colorGraph = new HashMap<>();
        public int[] nodeIdColorMap;
        public Map<Integer, Set<Node>> colorNodeMap;
        public TarjanAlgorithm.RadialData radialData;
        public double TH_ADN;
        public double TH_EDN;
    }

    private static class _JsonClass {
        private static class Cell {
            private static class Values {
                double Call;
                double Cast;
                double Contain;
                double Create;
                double Extend;
                double Implement;
                double Import;
                double Include;
                double Mixin;
                double Parameter;
                double Return;
                double Throw;
                double Use;
                double ImplLink;
                public double getSum() {
                    return Call+
                    Cast+
                    Contain+
                    Create+
                    Extend+
                    Implement+
                    Import+
                    Include+
                    Mixin+
                    Parameter+
                    Return+
                    Throw+
                    Use+
                    ImplLink;
                }
            }
            int src;
            int dest;
            Values values;
            public Cell(){}
            public Cell(int src, int dest, int val) {
                this.src = src;
                this.dest = dest;
                this.values = new Values();
                this.values.Call = val;
            }
        }
        List<String> variables;
        List<Cell> cells;
    }

    public DepSniffer(){}
    public DepSniffer(double thAdnFactory, double thEdnFactory, double thUdn) {
        this.TH_ADN_FAC = thAdnFactory;
        this.TH_EDN_FAC = thEdnFactory;
        this.TH_UDN = thUdn;
    }

    public StatsInfo getStatsInfo(File file) {
        _JsonClass rawData = getRawData(file);
        Map<Integer, Set<Pair<Integer, Double>>> graph = getGraph(rawData);
        StatsInfo info = genStatsInfo(graph, rawData.variables);
        return info;
    }

    public static void main(String[] args) {
        _JsonClass rawData = new _JsonClass();
        rawData.variables = Arrays.asList("1c", "2c", "3c", "4c");
        rawData.cells = Arrays.asList(
                new _JsonClass.Cell(0, 1, 1),
                new _JsonClass.Cell(1, 2, 1),
                new _JsonClass.Cell(2, 3, 1),
                new _JsonClass.Cell(3, 1, 1)
        );
        var info = new DepSniffer().genStatsInfo(getGraph(rawData), rawData.variables);
        System.out.println(info);

    }


    public static _JsonClass getRawData(File file) {
        String jsonStr = null;
        try {
            jsonStr = String.join("", Files.readLines(file, Charset.defaultCharset()));
        } catch (IOException e) {
            System.out.println("文件读取失败" + file.toString());
            throw new RuntimeException(e);
        }
        return new Gson().fromJson(jsonStr, _JsonClass.class);
    }

    private static Map<Integer, Set<Pair<Integer, Double>>> getGraph(_JsonClass rawData) {
        Map<Integer, Set<Pair<Integer, Double>>> g = new HashMap<>();
        for (_JsonClass.Cell cell : rawData.cells) {
            Set<Pair<Integer, Double>> nxtNodes = g.getOrDefault(cell.src, new HashSet<>());
            nxtNodes.add(Pair.of(cell.dest, cell.values.getSum()));
            g.put(cell.src, nxtNodes);
        }
        return g;
    }

    private StatsInfo genStatsInfo(Map<Integer, Set<Pair<Integer, Double>>> g, List<String> nameTable) {
        // in and out
        double[] adns = new double[nameTable.size()];
        double[] edns = new double[nameTable.size()];
        List<Node> nodes = new ArrayList<>();
        g.forEach((id, nxtNodes) -> {
            edns[id] = nxtNodes.size();
            nxtNodes.forEach(pair -> {
                adns[pair.getLeft()] += 1;
            });
        });
        // init node
        for (int id = 0; id < nameTable.size(); id++) {
            Node node = new Node(id, nameTable.get(id), adns[id], edns[id], TH_UDN);
            nodes.add(node);
        }
        // init2 node
        Arrays.sort(adns);
        Arrays.sort(edns);
        double thAdn = adns[(int)(adns.length * TH_ADN_FAC)];
        double thEdn = edns[(int)(edns.length * TH_EDN_FAC)];
        for (Node node : nodes) {
            double udn = 0.0;
            Set<Node> nxtNodes = new HashSet<>();
            for (Pair<Integer, Double> nxtPair : g.getOrDefault(node.id, new HashSet<>())) {
                int nxtId = nxtPair.getLeft();
                Node nxtNode = nodes.get(nxtId);
                nxtNodes.add(nxtNode);
                if(node.I < nxtNode.I) {
                    udn += 1;
                }
            }
            node.init2(udn, thAdn, thEdn, nxtNodes);
        }

        // loop node
        var tarjanAlgorithm = new TarjanAlgorithm(g, nameTable.size());
        List<Node> loopDepNodes = tarjanAlgorithm
                .getLoopNodes().stream()
                .map(id -> nodes.get(id))
                .collect(Collectors.toList());
        int colorCount = tarjanAlgorithm.getColorCnt();
        var colorG = tarjanAlgorithm.getColorGraph();
        var nodeIdColorMap =tarjanAlgorithm.getNodeIdColorMap();
        var colorNodeMap = new HashMap<Integer, Set<Node>>();
        tarjanAlgorithm.getColorNodeIdMap().forEach((color, nodeIds) -> {
            colorNodeMap.put(color, nodeIds.stream().map(id -> nodes.get(id)).collect(Collectors.toSet()));
        });

        StatsInfo info = new StatsInfo();
        info.TH_ADN = thAdn;
        info.TH_EDN = thEdn;
        info.hublikeNodes = nodes.stream().filter(node -> node.hasHubDep).collect(Collectors.toList());
        info.unstableNodes = nodes.stream().filter(node -> node.hasNonStableDep).collect(Collectors.toList());
        info.loopDepNodes = loopDepNodes;
        info.allNodes = nodes;
        info.colorCount = colorCount;
        info.colorGraph = colorG;
        info.nodeIdColorMap = nodeIdColorMap;
        info.colorNodeMap = colorNodeMap;
        info.radialData = tarjanAlgorithm.getRadialData();
        info.radialData.nodes.forEach(picNode -> {
            String[] classPath = info.allNodes.get(Integer.parseInt(picNode.id)).className.split("/");
            picNode.name = classPath[classPath.length - 1];
        });
//        if (info.colorDag.size() > 1) {
//            System.out.println("生成DAG图有多个（" + info.colorDag.size() + "）树！");
//        }
        return info;
    }


}
