package com.example.refac.check;

import com.example.refac.domain.RefClassNode;
import com.example.refac.domain.RefMethodNode;
import com.example.refac.util.CommitUtil;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CheckEs {


    public static void main(String[] args) throws Exception {

        InputStream in =  new FileInputStream( "io" + File.separator + "eCmap.txt" );
        ObjectInputStream os =  new  ObjectInputStream(in);
        Map<String, List<RefClassNode>> eCmap = (Map<String, List<RefClassNode>>) os.readObject();

//        CommitUtil.getFineCommitIdForEs(eCmap);
//        printCrMap(eCmap);

        countCrCor(eCmap);

        in =  new FileInputStream( "io" + File.separator + "mCmap.txt" );
        os =  new  ObjectInputStream(in);
        Map<String, List<RefClassNode>> mCmap = (Map<String, List<RefClassNode>>) os.readObject();
//        CommitUtil.getFineCommitIdForEs(mCmap);
//        printCrMap(mCmap);
        countCrCor(mCmap);

        in =  new FileInputStream( "io" + File.separator + "eOmap.txt" );
        os =  new  ObjectInputStream(in);
        Map<String, List<RefMethodNode>> eOmap = (Map<String, List<RefMethodNode>>) os.readObject();
//        CommitUtil.getFineCommitIdForEs_M(eOmap);
//        printMrMap(eOmap);
        countMrCor_C(eOmap);
//        countMrCor_O(eOmap);

        in =  new FileInputStream( "io" + File.separator + "mOmap.txt" );
        os =  new  ObjectInputStream(in);
        Map<String, List<RefMethodNode>> mOmap = (Map<String, List<RefMethodNode>>) os.readObject();
//        CommitUtil.getFineCommitIdForEs_M(mOmap);
//        printMrMap(mOmap);
        countMrCor_C(mOmap);
//        countMrCor_O(mOmap);

    }

    private static void countMrCor_O(Map<String, List<RefMethodNode>> map) throws IOException {
        int sum = 0;
        int total = 0;
        for (Map.Entry<String, List<RefMethodNode>> entry : map.entrySet()) {
            String commitId = entry.getKey();
            List<RefMethodNode> list = entry.getValue();
            for (RefMethodNode refMethodNode: list){
                total ++;
                String src = refMethodNode.getSrc();
                String dest = refMethodNode.getDest();
                if(coverMethod(commitId, src) || coverMethod(commitId, dest)){
                    sum ++;
//                    System.out.println(CommitUtil.getFineCommitId(CommitUtil.getLastCommitId(commitId)) + ":"  + src + " " + dest);

                } else {
                    System.out.println(CommitUtil.getFineCommitId(CommitUtil.getLastCommitId(commitId)) + ":"  + src + " " + dest);
                }
            }
        }
        System.out.println(total + " " + sum);
    }

    private static boolean coverMethod(String commitId, String m) throws IOException {
        String lastCommitId = CommitUtil.getLastCommitId(commitId);
        String lFineCommidId = CommitUtil.getFineCommitId(lastCommitId);
        String c = m.split("_")[0];
        String c_ = c.replaceAll("\\.","_");
        String inter_c_ = c.split("\\.")[c.split("\\.").length - 2] + "/\\[CN\\]/" + c.split("\\.")[c.split("\\.").length - 1];
        String inter_c_short = c.split("\\.")[c.split("\\.").length - 2] + "\\.java/\\[CN\\]/" + c.split("\\.")[c.split("\\.").length - 1];
//        System.out.println(c_);
//        System.out.println(inter_c_);
//        System.out.println(inter_c_short);
        String method =  m.split("_")[1];
        //        System.out.println(c_);
//        System.out.println(inter_c_);
        String pathname = "/Users/zzsnowy/Desktop/fighting/es1-0.4/litemall/litemall_" + lFineCommidId + ".txt" ;
        File filename = new File(pathname);
        InputStreamReader reader = new InputStreamReader(
                new FileInputStream(filename));
        BufferedReader br = new BufferedReader(reader);
        String line = br.readLine();
        while (line != null) {
            if (line.matches("[\\s\\S]*" + c_ + "[\\s\\S]*" + method + "[\\s\\S]*")
                    || line.matches("[\\s\\S]*" + inter_c_ + "[\\s\\S]*" + method + "[\\s\\S]*")
                    || line.matches("[\\s\\S]*" + inter_c_short + "[\\s\\S]*" + method + "[\\s\\S]*")){
                return true;
            }
            line = br.readLine();
        }

        return false;
    }

    private static void countMrCor_C(Map<String, List<RefMethodNode>> map) throws IOException {
        int sum = 0;
        int total = 0;
        for (Map.Entry<String, List<RefMethodNode>> entry : map.entrySet()) {
            String commitId = entry.getKey();
            List<RefMethodNode> list = entry.getValue();
//            System.out.println("commitID = " + commitId);
            for (RefMethodNode refMethodNode: list){
                total ++;
                String src = refMethodNode.getSrc().split("_")[0];
                String dest = refMethodNode.getDest().split("_")[0];
                if(coverClass(commitId, src) || coverClass(commitId, dest)){
                    sum ++;
//                    System.out.println(CommitUtil.getFineCommitId(CommitUtil.getLastCommitId(commitId)) + ":"  + src + " " + dest);

                } else {
//                    System.out.println(CommitUtil.getFineCommitId(CommitUtil.getLastCommitId(commitId)) + ":"  + src + " " + dest);
                    System.out.println(CommitUtil.getLastCommitId(commitId) + ":"  + src + " " + dest);

                }
            }
        }
        System.out.println(total + " " + sum);
    }

    private static void printMrMap(Map<String, List<RefMethodNode>> map) {
        for (Map.Entry<String, List<RefMethodNode>> entry : map.entrySet()) {
            List<RefMethodNode> list = entry.getValue();
            System.out.println("commitID = " + entry.getKey());
            for (RefMethodNode refMethodNode : list){
                System.out.println(refMethodNode.getSrc() + " " + refMethodNode.getDest());
            }
        }
    }

    private static void countCrCor(Map<String, List<RefClassNode>> map) throws IOException {
        int sum = 0;
        int total = 0;
        for (Map.Entry<String, List<RefClassNode>> entry : map.entrySet()) {
            String commitId = entry.getKey();
            List<RefClassNode> list = entry.getValue();
//            System.out.println("commitID = " + commitId);
            for (RefClassNode refClassNode : list){
                total ++;
                String src = refClassNode.getSrc();
                String dest = refClassNode.getDest();
                if(coverClass(commitId, src) || coverClass(commitId, dest)){
                    sum ++;
                } else {
//                    System.out.println(CommitUtil.getFineCommitId(CommitUtil.getLastCommitId(commitId)) + ":"  + src + " " + dest);
                    System.out.println(CommitUtil.getLastCommitId(commitId) + ":"  + src + " " + dest);
                }
            }
        }
        System.out.println(total + " " + sum);
    }


    private static boolean coverClass(String commitId, String c) throws IOException {
        String lastCommitId = CommitUtil.getLastCommitId(commitId);
        String lFineCommidId = CommitUtil.getFineCommitId(lastCommitId);
        String c_ = c.replaceAll("\\.","_");
        String inter_c_ = c.split("\\.")[c.split("\\.").length - 2] + "/\\[CN\\]/" + c.split("\\.")[c.split("\\.").length - 1];
        String inter_c_short = c.split("\\.")[c.split("\\.").length - 2] + "\\.java/\\[CN\\]/" + c.split("\\.")[c.split("\\.").length - 1];

        //        System.out.println(c_);
//        System.out.println(inter_c_);
        String pathname = "/Users/zzsnowy/Desktop/fighting/es1-0.4/litemall/litemall_" + lFineCommidId + ".txt" ;
        File filename = new File(pathname);
        InputStreamReader reader = new InputStreamReader(
                new FileInputStream(filename));
        BufferedReader br = new BufferedReader(reader);
        String line = br.readLine();
        while (line != null) {
            if (line.matches("[\\s\\S]*" + c_ + "[\\s\\S]*")
            || line.matches("[\\s\\S]*" + inter_c_ + "[\\s\\S]*")
            || line.matches("[\\s\\S]*" + inter_c_short + "[\\s\\S]*")){
//                if((Integer.parseInt(line.split("\t")[2]) >= 1 && (Double.parseDouble(line.split("\t")[3]) >= 1))){
                    return true;
//                }

            }
            line = br.readLine();
        }

        return false;
    }

    private static void printCrMap(Map<String, List<RefClassNode>> map) {
        for (Map.Entry<String, List<RefClassNode>> entry : map.entrySet()) {
            List<RefClassNode> list = entry.getValue();
            System.out.println("commitID = " + entry.getKey());
            for (RefClassNode refClassNode : list){
                System.out.println(refClassNode.getSrc() + " " + refClassNode.getDest());
            }
        }
    }
}
