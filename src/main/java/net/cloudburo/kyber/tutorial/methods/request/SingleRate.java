package net.cloudburo.kyber.tutorial.methods.request;

public class SingleRate {

    private String src_id;
    private String dst_id;
    private Float src_qty;
    private Float dst_qty;

    public SingleRate(String srcId, String dstId, Float srcQty, Float dstQty) {
        this.src_id = srcId;
        this.dst_id = dstId;
        this.src_qty = srcQty;
        this.dst_qty = dstQty;
    }
    public String getSrc_id() {
        return src_id;
    }

    public void approximateReceivableToken(double adjustment) {
        dst_qty = (float)adjustment*dst_qty;
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

    public Float getSrc_qty() {
        return src_qty;
    }

    public void setSrc_qty(Float src_qty) {
        this.src_qty = src_qty;
    }

    public Float getDst_qty() {
        return dst_qty;
    }

    public void setDst_qty(Float dst_qty) {
        this.dst_qty = dst_qty;
    }
}
