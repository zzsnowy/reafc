package com.example.refac.check;




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
        Map<String, List<String>> mCmap = new HashMap<>();
        Map<String, List<String>> eCmap = new HashMap<>();
        Map<String, List<String>> mOmap = new HashMap<>();
        Map<String, List<String>> eOmap = new HashMap<>();

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

                            put(mCmap, commitId, c1);
                            put(mCmap, commitId, c2);

                            System.out.println(c1);
                            System.out.println(c2);

                            System.out.println(commitId + " " + ref);
                        }
                        if((ref.getRefactoringType() == EXTRACT_CLASS)){


                            ExtractClassRefactoring tmp = (ExtractClassRefactoring) ref;

                            String c1 = tmp.getOriginalClass().getName();
                            String c2 = tmp.getExtractedClass().getName();

                            put(eCmap, commitId, c1);
                            put(eCmap, commitId, c2);

                            System.out.println(c1);
                            System.out.println(c2);

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
                            put(mOmap, commitId, c1);
                            put(mOmap, commitId, c2);
                            put(mOmap, commitId, m1);
                            put(mOmap, commitId, m2);

                            System.out.println(commitId + " " + ref);
                        }
                        if((ref.getRefactoringType() == EXTRACT_OPERATION)){

                            ExtractOperationRefactoring tmp = (ExtractOperationRefactoring) ref;

                            String c1 = tmp.getSourceOperationBeforeExtraction().getClassName();
                            String c2 = tmp.getExtractedOperation().getClassName();

                            String m1 = tmp.getSourceOperationBeforeExtraction().getName();
                            String m2 = tmp.getExtractedOperation().getName();

                            System.out.println(c1);
                            System.out.println(c2);
                            System.out.println(m1);
                            System.out.println(m2);

                            put(eOmap, commitId, c1);
                            put(eOmap, commitId, c2);
                            put(eOmap, commitId, m1);
                            put(eOmap, commitId, m2);

                            System.out.println(commitId + " " + ref);
                        }

                    }
                }
            });
        } catch (Exception e) {
        }

        System.out.println("移动类：");
        printMap(mCmap);
        System.out.println("提取类：");
        printMap(eCmap);
        System.out.println("移动方法：");
        printMap(mOmap);
        System.out.println("提取方法：");
        printMap(eOmap);




    }

    private static void printMap(Map<String, List<String>> map) {
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
    }

    private static void put(Map<String, List<String>> map, String commitId, String c) {

        if(map.containsKey(commitId)){
            List<String> list = map.get(commitId);
            list.add(c);
            map.put(commitId, list);
        } else {
            List<String> list = new ArrayList<>();
            list.add(c);
            map.put(commitId, list);
        }

    }
}

