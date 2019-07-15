package net.cloudburo.kyber.tutorial.methods.response;

import org.web3j.protocol.core.Response;

import java.util.List;


public class Currencies extends Response<Currencies> {

    public Currencies getCurrencies() { return getResult(); }
    private List<Currencies.Currency> data;
    private boolean error;

    public Currency getCurrency(String symbol) {
        return data.stream()
                .filter(currency -> symbol.equals(currency.getSymbol()))
                .findAny()
                .orElse(null);
    }

    public boolean existsCurreny(String symbol){
        return getCurrency(symbol) != null;
    }

    public List<Currency> getData() {
        return data;
    }

    public void setData(List<Currency> data) {
        this.data = data;
    }

    public boolean hasError() {
       return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public static class Currency {
        public Currency() {}
        private String symbol;
        private String name;
        private String address;
        private String decimals;
        private String id;
        private List<String> reserves_src;
        private List<String> reserves_dest;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDecimals() {
            return decimals;
        }

        public void setDecimals(String decimals) {
            this.decimals = decimals;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<String> getReserves_src() {
            return reserves_src;
        }

        public void setReserves_src(List<String> reserves_src) {
            this.reserves_src = reserves_src;
        }

        public List<String> getReserves_dest() {
            return reserves_dest;
        }

        public void setReserves_dest(List<String> reserves_dest) {
            this.reserves_dest = reserves_dest;
        }
    }

}
