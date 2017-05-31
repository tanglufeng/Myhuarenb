package com.abcs.haiwaigou.model;

import com.google.gson.annotations.SerializedName;

/**
 * 项目名称：com.abcs.haiwaigou.model
 * 作者：zds
 * 时间：2017/4/11 16:33
 */

public class TaxArrEntry {
    /**
     * netto_a : 0.00
     * netto_b : 7.70
     * mwst_a : 0.00
     * mwst_b : 0.77
     * brutto_a : 0
     * brutto_b : 8.47
     */

    @SerializedName("netto_a")
    public double nettoA;
    @SerializedName("netto_b")
    public double nettoB;
    @SerializedName("mwst_a")
    public double mwstA;
    @SerializedName("mwst_b")
    public double mwstB;
    @SerializedName("brutto_a")
    public double bruttoA;
    @SerializedName("brutto_b")
    public double bruttoB;
}
