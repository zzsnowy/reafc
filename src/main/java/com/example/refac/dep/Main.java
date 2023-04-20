package com.example.refac.dep;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        DepSniffer.StatsInfo info = new DepSniffer().getStatsInfo(new File("/Users/zzsnowy/Desktop/fighting/depends-0.9.7/tmp-file.json"));
        System.out.println(info);
    }
}