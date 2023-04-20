package com.example.refac.util;

import com.example.refac.dep.DepSniffer;
import com.example.refac.dep.Node;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CountEdClassNumberHub {
    public static void main(String[] args) throws Exception {
        String pathname = "/Users/zzsnowy/Desktop/fighting/depends-0.9.7/lit/pro的副本.txt" ;
        File filename = new File(pathname);
        InputStreamReader reader = new InputStreamReader(
                new FileInputStream(filename));
        BufferedReader br = new BufferedReader(reader);
        String line = br.readLine();
        int sum = 0;
        int max = 0;
        Set<String> set = new HashSet<>();
        while (line != null) {
            sum += countDe(line, set);
            max = Math.max(countDe(line, set), max);
            System.out.println(countDe(line, set));
            line = br.readLine();
        }
        System.out.println(sum);
        System.out.println("max" + max);

        int total = 0;
        String lcpath = "/Users/zzsnowy/Desktop/fighting/all_class.txt";
        Set<String> lfc = read(lcpath);
        System.out.println(lfc.size());
        for(String s : lfc){
            String tmp = s.replaceAll("\\.", "/");
            for(String c : set){
                if(c.matches("[\\s\\S]*" + tmp + "[\\s\\S]*")){
                    total++;
                }
            }
        }
        System.out.println(total);
    }

    public static Set<String> countCombine(Set<String> lfc) throws IOException {

        String pathname = "/Users/zzsnowy/Desktop/fighting/depends-0.9.7/lit/pro的副本.txt" ;
        File filename = new File(pathname);
        InputStreamReader reader = new InputStreamReader(
                new FileInputStream(filename));
        BufferedReader br = new BufferedReader(reader);
        String line = br.readLine();
        int sum = 0;
        int max = 0;
        Set<String> set = new HashSet<>();
        while (line != null) {
            sum += countDe(line, set);
            max = Math.max(countDe(line, set), max);
            System.out.println("每个集合：" + countDe(line, set));
            line = br.readLine();
        }
//        System.out.println(sum);
//        System.out.println("max" + max);

        int total = 0;

        Set<String> set1 = new HashSet<>();
        System.out.println("lfc:" + lfc.size());
//        System.out.println(set.size() + " " + lfc.size());
        for(String s : lfc){
            String tmp = s.replaceAll("\\.", "/") + ".java";
            int f = 0;
            for(String c : set){
                if(c.matches("[\\s\\S]*" + tmp + "[\\s\\S]*")){
                    System.out.println(s + " " + tmp);
                    total++;
                    f = 1;
                }

            }
            if(f == 0){
//                System.out.println();
                set1.add(s);
            }
        }
        System.out.println(total);
        return set1;
    }

    private static Set<String> read(String lcpath) throws IOException {
        File filename = new File(lcpath);
        try(InputStreamReader reader = new InputStreamReader(
                new FileInputStream(filename));){
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();
            int sum = 0;
            Set<String> set = new HashSet<>();
            while (line != null) {
                set.add(line);
                line = br.readLine();
            }
            return set;
        }
    }
    private static int countDe(String pro, Set<String> set) throws IOException {
        String path = "/Users/zzsnowy/Desktop/fighting/depends-0.9.7/lit/" + pro + "-file.json";
        DepSniffer.StatsInfo info = DepSniffer.getStatsInfo(new File(path));
        List<Node> cd = info.hublikeNodes;
//        return info.allNodes.size();
        for(Node node : cd){
            set.add(node.className);
        }
        return set.size();
    }
}
