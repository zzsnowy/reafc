package com.example.refac.refactor;


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


import java.util.List;

import static org.refactoringminer.api.RefactoringType.*;

public class ExtractClass {
    public static void main(String[] args) throws Exception {


        GitService gitService = new GitServiceImpl();
        GitHistoryRefactoringMiner miner = new GitHistoryRefactoringMinerImpl();

        Repository repo = gitService.cloneIfNotExists(
                "tmp/litemall",
                "https://github.com/linlinjava/litemall.git");

        try {
            miner.detectAll(repo, "master", new RefactoringHandler() {
                @Override
                public void handle(String commitId, List<Refactoring> refactorings) {
//                    System.out.println("Refactorings at " + commitId);
                    boolean flag = false;
                    for (Refactoring ref : refactorings) {
                        if(ref.getRefactoringType() == MOVE_CLASS){
                            //System.out.println(commitId + "\t" +ref.toString());
                            if (!flag){
                                System.out.println(commitId);
                                System.out.println(ref.toString());
                                flag = true;
                            } else {
                                System.out.println(ref);
                            }
                        }

                    }
                }
            });
        } catch (Exception e) {
        }


    }
}
