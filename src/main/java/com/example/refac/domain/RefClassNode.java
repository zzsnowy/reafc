package com.example.refac.domain;

import java.io.Serializable;

public class RefClassNode implements Serializable {
    private String src;
    private String dest;

    public RefClassNode(String src, String dest) {
        this.src = src;
        this.dest = dest;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

}
