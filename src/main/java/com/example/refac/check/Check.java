package com.example.refac.check;




import com.example.refac.domain.RefClassNode;
import com.example.refac.domain.RefMethodNode;
import com.example.refac.util.CommitUtil;
import gr.uom.java.xmi.UMLType;
import gr.uom.java.xmi.diff.*;
import org.refactoringminer.api.GitHistoryRefactoringMiner;
import org.refactoringminer.api.GitService;
import org.refactoringminer.api.Refactoring;
import org.refactoringminer.api.RefactoringHandler;
import org.refactoringminer.rm1.GitHistoryRefactoringMinerImpl;
import org.refactoringminer.util.GitServiceImpl;
import gr.uom.java.xmi.UMLType;
import gr.uom.java.xmi.diff.MoveAttributeRefactoring;
import gr.uom.java.xmi.diff.MoveOperationRefactoring;
import org.eclipse.jgit.lib.Repository;
import org.refactoringminer.api.GitHistoryRefactoringMiner;
import org.refactoringminer.api.GitService;
import org.refactoringminer.api.Refactoring;
import org.refactoringminer.api.RefactoringHandler;
import org.refactoringminer.rm1.GitHistoryRefactoringMinerImpl;
import org.refactoringminer.util.GitServiceImpl;


import java.io.*;
import java.util.*;

import static org.refactoringminer.api.RefactoringType.*;

public class Check {
    public static void main(String[] args) throws Exception {


        GitService gitService = new GitServiceImpl();
        GitHistoryRefactoringMiner miner = new GitHistoryRefactoringMinerImpl();

        Repository repo = gitService.cloneIfNotExists(
                "tmp/litemall",
                "https://github.com/linlinjava/litemall.git");

        Set<String> set = new HashSet<>();
        Map<String, List<RefClassNode>> mCmap = new HashMap<>();
        Map<String, List<RefClassNode>> eCmap = new HashMap<>();
        Map<String, List<RefMethodNode>> mOmap = new HashMap<>();
        Map<String, List<RefMethodNode>> eOmap = new HashMap<>();

        try {
            miner.detectAll(repo, "master", new RefactoringHandler() {
                @Override
                public void handle(String commitId, List<Refactoring> refactorings) {
                    boolean flag = false;
                    for (Refactoring ref : refactorings) {
                        if((ref.getRefactoringType() == MOVE_CLASS)){

                            MoveClassRefactoring tmp = (MoveClassRefactoring) ref;

                            String c1 = tmp.getOriginalClassName();
                            String c2 = tmp.getMovedClassName();

                            RefClassNode node = new RefClassNode(c1, c2);

                            try {
                                put(mCmap, commitId, node);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

//                            System.out.println(c1);
//                            System.out.println(c2);

                            System.out.println(commitId + " " + ref);
                        }
                        if((ref.getRefactoringType() == EXTRACT_CLASS)){


                            ExtractClassRefactoring tmp = (ExtractClassRefactoring) ref;

                            String c1 = tmp.getOriginalClass().getName();
                            String c2 = tmp.getExtractedClass().getName();

                            RefClassNode node = new RefClassNode(c1, c2);


                            try {
                                put(eCmap, commitId, node);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

//                            System.out.println(c1);
//                            System.out.println(c2);

                            System.out.println(commitId + " " + ref);
                        }
                        if((ref.getRefactoringType() == MOVE_OPERATION)){

                            MoveOperationRefactoring tmp = (MoveOperationRefactoring) ref;

                            String c1 = tmp.getOriginalOperation().getClassName();
                            String c2 = tmp.getMovedOperation().getClassName();


                            String m1 = tmp.getOriginalOperation().getName();
                            String m2 = tmp.getMovedOperation().getName();

////                            System.out.println(c1);
////                            System.out.println(c2);
////
////                            System.out.println(m1);
////                            System.out.println(m2);
                            m1 = c1 + "_" + m1;
                            m2 = c2 + "_" + m2;

                            RefMethodNode node = new RefMethodNode(m1, m2);
                            try {
                                put(mOmap, commitId, node);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            System.out.println(commitId + " " + ref);
                        }
                        if((ref.getRefactoringType() == EXTRACT_OPERATION)){

                            ExtractOperationRefactoring tmp = (ExtractOperationRefactoring) ref;

                            String c1 = tmp.getSourceOperationBeforeExtraction().getClassName();
                            String c2 = tmp.getExtractedOperation().getClassName();

                            String m1 = tmp.getSourceOperationBeforeExtraction().getName();
                            String m2 = tmp.getExtractedOperation().getName();

//                            System.out.println(c1);
//                            System.out.println(c2);
//                            System.out.println(m1);
//                            System.out.println(m2);

                            m1 = c1 + "_" + m1;
                            m2 = c2 + "_" + m2;

                            RefMethodNode node = new RefMethodNode(m1, m2);

                            try {
                                put(eOmap, commitId, node);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            System.out.println(commitId + " " + ref);
                        }

                    }
                }
            });
        } catch (Exception e) {
        }

        System.out.println("移动类：");
        printCrMap(mCmap);
        System.out.println("提取类：");
        printCrMap(eCmap);
        System.out.println("移动方法：");
        printMrMap(mOmap);
        System.out.println("提取方法：");
        printMrMap(eOmap);

        serialCrMap("mC", mCmap);
        serialCrMap("eC", eCmap);
        serialMrMap("mO", mOmap);
        serialMrMap("eO", eOmap);


    }

    private static void serialCrMap(String name, Map<String, List<RefClassNode>> map) throws IOException {
        createFile("io" + File.separator  + name + "map.txt" );
        try (OutputStream op =  new FileOutputStream( "io" + File.separator  + name + "map.txt" );
             ObjectOutputStream ops =  new  ObjectOutputStream(op);) {
            ops.writeObject(map);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void serialMrMap(String name, Map<String, List<RefMethodNode>> map) throws IOException {
        createFile("io" + File.separator  + name + "map.txt" );
        try (OutputStream op =  new FileOutputStream( "io" + File.separator  + name + "map.txt" );
             ObjectOutputStream ops =  new  ObjectOutputStream(op);) {
            ops.writeObject(map);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createFile(String filePath) throws IOException {
        // 创建File对象
        File file = new File(filePath);

        // 检查文件或目录是否存在
        if (file.exists()) {
            // 如果存在，删除文件或目录
            if (file.delete()) {
                System.out.println(filePath + "已成功删除！");
            } else {
                System.out.println(filePath + "删除失败。");
            }
        } else {
            // 如果不存在，创建文件或目录
            if (file.createNewFile()) {
                System.out.println(filePath + "已成功创建！");
            } else {
                System.out.println(filePath + "创建失败。");
            }
        }
    }


    private static void printCrMap(Map<String, List<RefClassNode>> map) {
        for (Map.Entry<String, List<RefClassNode>> entry : map.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
    }

    private static void printMrMap(Map<String, List<RefMethodNode>> map) {
        for (Map.Entry<String, List<RefMethodNode>> entry : map.entrySet()) {
            System.out.println("commitID = " + entry.getKey() + ", 方法级别重构 = " + entry.getValue());
        }
    }

    private static void put(Map<String, List<RefClassNode>> map, String commitId, RefClassNode c) throws IOException {

        if (!CommitUtil.isExist(commitId)){
            return;
        }

        if(map.containsKey(commitId)){
            List<RefClassNode> list = map.get(commitId);
            list.add(c);
            map.put(commitId, list);
        } else {
            List<RefClassNode> list = new ArrayList<>();
            list.add(c);
            map.put(commitId, list);
        }

    }
    private static void put(Map<String, List<RefMethodNode>> map, String commitId, RefMethodNode c) throws IOException {

        if (!CommitUtil.isExist(commitId)){
            return;
        }

        if(map.containsKey(commitId)){
            List<RefMethodNode> list = map.get(commitId);
            list.add(c);
            map.put(commitId, list);
        } else {
            List<RefMethodNode> list = new ArrayList<>();
            list.add(c);
            map.put(commitId, list);
        }

    }
}

