package com.example.refac.check;

import com.example.refac.dep.DepSniffer;
import com.example.refac.dep.Node;
import com.example.refac.domain.RefClassNode;
import com.example.refac.domain.RefMethodNode;
import com.example.refac.util.CommitUtil;

import java.io.*;
import java.util.List;
import java.util.Map;

public class CheckCD {
    public static void main(String[] args) throws Exception {

        InputStream in =  new FileInputStream( "io" + File.separator + "eCmap.txt" );
        ObjectInputStream os =  new  ObjectInputStream(in);
        Map<String, List<RefClassNode>> eCmap = (Map<String, List<RefClassNode>>) os.readObject();

//        printCrMap(eCmap);

        countCrCor(eCmap);

        in =  new FileInputStream( "io" + File.separator + "mCmap.txt" );
        os =  new  ObjectInputStream(in);
        Map<String, List<RefClassNode>> mCmap = (Map<String, List<RefClassNode>>) os.readObject();

//        printCrMap(mCmap);
        countCrCor(mCmap);

        in =  new FileInputStream( "io" + File.separator + "eOmap.txt" );
        os =  new  ObjectInputStream(in);
        Map<String, List<RefMethodNode>> eOmap = (Map<String, List<RefMethodNode>>) os.readObject();

//        printMrMap(eOmap);

        countMrCor_C(eOmap);
//        countMrCor_O(eOmap);

        in =  new FileInputStream( "io" + File.separator + "mOmap.txt" );
        os =  new  ObjectInputStream(in);
        Map<String, List<RefMethodNode>> mOmap = (Map<String, List<RefMethodNode>>) os.readObject();

//        printMrMap(mOmap);
        countMrCor_C(mOmap);
//        countMrCor_O(mOmap);

    }

    private static void countMrCor_C(Map<String, List<RefMethodNode>> map) throws IOException {

        int sum = 0;
        int total = 0;
        for (Map.Entry<String, List<RefMethodNode>> entry : map.entrySet()) {
            String commitId = entry.getKey();
            String lastCommitId = CommitUtil.getLastCommitId(commitId);
            DepSniffer.StatsInfo info = DepSniffer.getStatsInfo(new File("/Users/zzsnowy/Desktop/fighting/depends-0.9.7/lit/" + lastCommitId + "-file.json"));
            List<Node> cd = info.unstableNodes;
            List<RefMethodNode> list = entry.getValue();
//            System.out.println("commitID = " + commitId);
            for (RefMethodNode refMethodNode : list){
                total ++;
                String src = refMethodNode.getSrc().split("_")[0];
                String dest = refMethodNode.getDest().split("_")[0];
                if(coverClass(lastCommitId, src, cd) || coverClass(lastCommitId, dest, cd)){
                    System.out.println(CommitUtil.getFineCommitId(CommitUtil.getLastCommitId(commitId)) + ":"  + src + " " + dest + " " + cd.size());
                    sum ++;
                } else {
//                    System.out.println(CommitUtil.getFineCommitId(CommitUtil.getLastCommitId(commitId)) + ":"  + src + " " + dest);
//                    System.out.println(lastCommitId + " " + commitId + ":"  + src + " " + dest);
                }
            }
        }
        System.out.println(total + " " + sum);
    }

    private static void countCrCor(Map<String, List<RefClassNode>> map) throws IOException {
        int sum = 0;
        int total = 0;
                for (Map.Entry<String, List<RefClassNode>> entry : map.entrySet()) {
            String commitId = entry.getKey();
            String lastCommitId = CommitUtil.getLastCommitId(commitId);
            DepSniffer.StatsInfo info = DepSniffer.getStatsInfo(new File("/Users/zzsnowy/Desktop/fighting/depends-0.9.7/lit/" + lastCommitId + "-file.json"));
            List<Node> cd = info.unstableNodes;
            List<RefClassNode> list = entry.getValue();
//            System.out.println("commitID = " + commitId);
            for (RefClassNode refClassNode : list){
                total ++;
                String src = refClassNode.getSrc();
                String dest = refClassNode.getDest();
                if(coverClass(lastCommitId, src, cd) || coverClass(lastCommitId, dest, cd)){
                    sum ++;
                    System.out.println(CommitUtil.getFineCommitId(CommitUtil.getLastCommitId(commitId)) + ":"  + src + " " + dest + " " + cd.size());
                } else {
//                    System.out.println(CommitUtil.getFineCommitId(CommitUtil.getLastCommitId(commitId)) + ":"  + src + " " + dest);
//                    System.out.println(lastCommitId + " " + commitId + ":"  + src + " " + dest);
                }
            }

        }
        System.out.println(total + " " + sum);
    }

    private static boolean coverClass(String commitId, String c, List<Node> cd) {
        String c_ = c.replaceAll("\\.","/");
//        System.out.println(c_);
        for(Node node : cd){
            if (node.className.matches("[\\s\\S]*" + c_ + "[\\s\\S]*")){
                return true;
            }
        }
        return  false;
    }

    private static void printMrMap(Map<String, List<RefMethodNode>> map) throws IOException {
        for (Map.Entry<String, List<RefMethodNode>> entry : map.entrySet()) {
            List<RefMethodNode> list = entry.getValue();
            System.out.println(CommitUtil.getLastCommitId(entry.getKey()));
            for (RefMethodNode refMethodNode : list){
                System.out.println(refMethodNode.getSrc() + " " + refMethodNode.getDest());
            }
        }
    }
    private static void printCrMap(Map<String, List<RefClassNode>> map) throws IOException {
        for (Map.Entry<String, List<RefClassNode>> entry : map.entrySet()) {
            List<RefClassNode> list = entry.getValue();
            System.out.println(CommitUtil.getLastCommitId(entry.getKey()));
            for (RefClassNode refClassNode : list){
                System.out.println(refClassNode.getSrc() + " " + refClassNode.getDest());
            }
        }
    }
}
