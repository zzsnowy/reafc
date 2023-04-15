package com.example.refac.util;



import gr.uom.java.xmi.UMLType;
import gr.uom.java.xmi.diff.MoveAttributeRefactoring;
import gr.uom.java.xmi.diff.MoveOperationRefactoring;
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


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.refactoringminer.api.RefactoringType.*;

public class GetAllComitId {
    public static void main(String[] args) throws Exception {


        GitService gitService = new GitServiceImpl();
        GitHistoryRefactoringMiner miner = new GitHistoryRefactoringMinerImpl();

        Repository repo = gitService.cloneIfNotExists(
                "tmp/litemall",
                "https://github.com/linlinjava/litemall.git");

        Set<String> set = new HashSet<>();

        try {
            miner.detectAll(repo, "master", new RefactoringHandler() {
                @Override
                public void handle(String commitId, List<Refactoring> refactorings) {
//                    System.out.println("Refactorings at " + commitId);
                    boolean flag = false;
                    for (Refactoring ref : refactorings) {
                        if((ref.getRefactoringType() == MOVE_CLASS) || (ref.getRefactoringType() == EXTRACT_CLASS)
                                || (ref.getRefactoringType() == MOVE_OPERATION) || (ref.getRefactoringType() == EXTRACT_OPERATION)){
                            set.add(commitId);
                        }

                    }
                }
            });
        } catch (Exception e) {
        }
        for (String str : set) {
            System.out.println(str);
        }
    }
}
