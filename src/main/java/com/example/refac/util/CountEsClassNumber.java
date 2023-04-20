package com.example.refac.util;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class CountEsClassNumber {
    public static void main(String[] args) throws Exception {
        String pathname = "/Users/zzsnowy/Desktop/fighting/lt.txt" ;
        File filename = new File(pathname);

        Set<String> set = new HashSet<>();
        try (InputStreamReader reader = new InputStreamReader(
                new FileInputStream(filename));
             BufferedReader br = new BufferedReader(reader);
        ){
            String line = br.readLine();
            int sum = 0;

            while (line != null) {
                sum += countEC(line, set);
                System.out.println(countEC(line, set));
                line = br.readLine();
            }
            System.out.println(sum);
        }
        Set<String> set1 = new HashSet<>();
        int total = 0;
        String lcpath = "/Users/zzsnowy/Desktop/fighting/all_class.txt";
        Set<String> lfc = read(lcpath);
        System.out.println(lfc.size());
        for(String s : lfc){
            String tmp = s.replaceAll("\\.", "_");
            int f = 0;
            for(String c : set){
//                System.out.println(c + "  " + tmp);
                if(c.matches("[\\s\\S]*" + tmp + "[\\s\\S]*")){
                    total++;
                    f = 1;
                }
            }
            if(f == 0){
                System.out.println(s);
                set1.add(s);
            }
        }

        System.out.println(total);
        System.out.println("size:" + set1.size());
        set1 = CountEdClassNumber.countCombine(set1);
        System.out.println(set1.size());
        set1 = CountEdClassNumberLoop.countCombine(set1);
        System.out.println(set1.size());
        set1 = CountEdClassNumberHub.countCombine(set1);
        System.out.println(set1.size());

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

    private static int countEC(String pro, Set<String> set) throws IOException {
        String path = "/Users/zzsnowy/Desktop/fighting/es3/litemall/litemall_" + pro + ".txt";
        File filename = new File(path);
        InputStreamReader reader = new InputStreamReader(
                new FileInputStream(filename));
        BufferedReader br = new BufferedReader(reader);
        String line = br.readLine();
        int sum = 0;

        while (line != null) {
            if((Integer.parseInt(line.split("\t")[2]) >= 1) && (Double.parseDouble(line.split("\t")[3]) >= 0.4)){
                set.add(line.split("\\.")[0]);
            }
            line = br.readLine();
        }
        return set.size();
    }




}
