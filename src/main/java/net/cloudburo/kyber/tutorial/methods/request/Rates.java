package net.cloudburo.kyber.tutorial.methods.request;

import java.util.List;

public class Rates {
    private String src_id;
    private String dst_id;
    private List<Float> src_qty;
    private List<Float> dst_qty;

    public String getSrc_id() {
        return src_id;
    }

    public void setSrc_id(String src_id) {
        this.src_id = src_id;
    }

    public String getDst_id() {
        return dst_id;
    }

    public void setDst_id(String dst_id) {
        this.dst_id = dst_id;
    }

    public List<Float> getSrc_qty() {
        return src_qty;
    }

    public void setSrc_qty(List<Float> src_qty) {
        this.src_qty = src_qty;
    }

    public List<Float> getDst_qty() {
        return dst_qty;
    }

    public void setDst_qty(List<Float> dst_qty) {
        this.dst_qty = dst_qty;
    }

    public SingleRate getSingleRate(int index) {
        return  new SingleRate(src_id,dst_id,src_qty.get(index), dst_qty.get(index));
    }
}
