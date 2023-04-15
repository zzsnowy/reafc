package com.example.refac.util;

import com.example.refac.domain.RefClassNode;
import com.example.refac.domain.RefMethodNode;

import java.io.*;
import java.util.List;
import java.util.Map;

public class CommitUtil {
    public static String getLastCommitId(String commitId) throws IOException {

        String lastCommitId = "";
        String pathname = "commit/litemall-coarse.txt";
        File filename = new File(pathname);
        InputStreamReader reader = new InputStreamReader(
                new FileInputStream(filename));
        BufferedReader br = new BufferedReader(reader);
        String line = br.readLine();
        while (line != null) {
            if (line.equals(commitId)){
                lastCommitId = br.readLine();
                break;
            }
            line = br.readLine();
        }
        return lastCommitId;
    }

    public static boolean isExist(String commitId) throws IOException {
        String lastCommitId = "";
        String pathname = "commit/litemall-coarse.txt";
        File filename = new File(pathname);
        InputStreamReader reader = new InputStreamReader(
                new FileInputStream(filename));
        BufferedReader br = new BufferedReader(reader);
        String line = br.readLine();
        while (line != null) {
            if (line.equals(commitId)){
                return true;
            }
            line = br.readLine();
        }
        return false;
    }

    public static void getFineCommitIdForEs(Map<String, List<RefClassNode>> map) throws IOException {

        for (Map.Entry<String, List<RefClassNode>> entry : map.entrySet()) {
            String commitId = entry.getKey();
//            System.out.println("commitID = " + commitId);
            String pathname = "commit/litemall.txt";
            File filename = new File(pathname);
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();
            while (line != null) {
                boolean f = false;
                if (line.split(" ")[0].equals(commitId)){
                    f = true;
                }
                line = br.readLine();
                if(f){
                    System.out.println(line.split(" ")[1]);
                }
            }
        }
    }

    public static String getFineCommitId(String commitId) throws IOException {
        String pathname = "commit/litemall.txt";
        File filename = new File(pathname);
        InputStreamReader reader = new InputStreamReader(
                new FileInputStream(filename));
        BufferedReader br = new BufferedReader(reader);
        String line = br.readLine();
        while (line != null) {
            if (line.split(" ")[0].equals(commitId)){
                return line.split(" ")[1];
            }
            line = br.readLine();
        }
        return "";
    }

    public static void getFineCommitIdForEs_M(Map<String, List<RefMethodNode>> map) throws IOException {

        for (Map.Entry<String, List<RefMethodNode>> entry : map.entrySet()) {
            String commitId = entry.getKey();
//            System.out.println("commitID = " + commitId);
            String pathname = "commit/litemall.txt";
            File filename = new File(pathname);
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();
            while (line != null) {
                boolean f = false;
                if (line.split(" ")[0].equals(commitId)){
                    f = true;
                }
                line = br.readLine();
                if(f){
                    System.out.println(line.split(" ")[1]);
                }
            }
        }
    }
}
