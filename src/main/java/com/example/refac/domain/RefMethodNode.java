package com.example.refac.domain;

import java.io.Serializable;

public class RefMethodNode  implements Serializable {
    public RefMethodNode(String src, String dest) {
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

    private String src;
    private String dest;
}
