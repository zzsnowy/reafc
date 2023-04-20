package com.example.refac.dep;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        DepSniffer.StatsInfo info = new DepSniffer().getStatsInfo(new File("/Users/lilingj/work/depends-0.9.7/tmp-file.json"));
//        DepSniffer.StatsInfo info = new DepSniffer().getStatsInfo(new File("/Users/zzsnowy/Desktop/fighting/depends-0.9.7/tmp-file.json"));
        new Gson().toJson(info.radialData, new FileWriter("./tmp-1.json"));
        // System.out.println(info);
        System.out.println(new Gson().toJson(info.radialData));
    }
}