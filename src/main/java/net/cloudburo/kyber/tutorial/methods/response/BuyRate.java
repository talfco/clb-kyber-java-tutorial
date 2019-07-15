package net.cloudburo.kyber.tutorial.methods.response;

import org.web3j.protocol.core.Response;

import java.util.List;

public class BuyRate extends Response<BuyRate> {

    private List<Rate> data;
    private boolean error;

    public List<Rate> getData() {
        return data;
    }

    public void setData(List<Rate> data) {
        this.data = data;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public static class Rate {

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
    }

}
