package com.example.refac.dep;

import java.util.Objects;

/**
 * @Author: lilingj
 * @CreateTime: 2023-04-15  18:12
 * @Description: TODO
 * @Version: 1.0
 */

public class Node {

    public int id;
    public String className;
    public double ADN;
    public double EDN;
    public double I;
    public double UDN;
    // default: 0.3
    public double TH_UDN;
    public double TH_BALANCE;
    public boolean hasNonStableDep;
    public boolean hasHubDep;
    public Node(int id, String className, double ADN, double EDN, double TH_UDN) {
        this.id = id;
        this.className = className;
        this.ADN = ADN;
        this.EDN = EDN;
        this.TH_BALANCE = (ADN + EDN) / 2.0;
        this.TH_UDN = TH_UDN;
        this.I = (double) EDN / (ADN + EDN);
    }

    public void init2(double UDN, double TH_ADN, double TH_EDN) {
//        double udn = 0;
//        for (Node nxtNode : nxtNodes) {
//            if (this.I > nxtNode.I) {
//                udn++;
//            }
//        }
        this.UDN = UDN;
        this.hasNonStableDep = ((double) this.UDN / this.EDN) > this.TH_UDN;
        this.hasHubDep = (this.ADN > TH_ADN && this.EDN > TH_EDN && Math.abs(this.ADN - this.EDN) < this.TH_BALANCE);
    }











    // ===========================================================================
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return id == node.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
