package org.xry.churchmodule.pojo;

public class Omen {
    private String omenEn;

    private String omenCn;

    private String description;

    public Omen(String omenEn, String omenCn, String description) {
        this.omenEn = omenEn;
        this.omenCn = omenCn;
        this.description = description;
    }

    public Omen() {}

    public String getOmenEn() {
        return omenEn;
    }

    public void setOmenEn(String omenEn) {
        this.omenEn = omenEn;
    }

    public String getOmenCn() {
        return omenCn;
    }

    public void setOmenCn(String omenCn) {
        this.omenCn = omenCn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
