package net.cloudburo.kyber.tutorial.protocol;

import net.cloudburo.kyber.tutorial.methods.request.GasPriceRange;
import net.cloudburo.kyber.tutorial.methods.request.SingleRate;
import net.cloudburo.kyber.tutorial.methods.response.*;

import org.web3j.protocol.core.Request;

public interface KyberAPI {

    public Request<?, Currencies> currencies();
    public Request<?, BuyRate> buyRate(String id, String qty,boolean onlyOfficialReserve);
    public Request<?, SellRate> sellRate(String id, String qty, boolean onlyOfficialReserve);
    public Request<?, TradeData> tradeData(String userAddress, SingleRate rate, GasPriceRange gasPrice);
    public Request<?, TradeData> tradeData(String userAddress, String srcId, String dstId, Float srcQty,Float minDstQty,
                                           GasPriceRange gasPrice, String walletId, boolean onlyOfficialReserve);
    public Request<?,EnabledTokensForWallet> enabledTokensForWallet(String userAddress);
    public Request<?, EnableTokenTransfer> enableTokenTransfer(String userAddress, String id, GasPriceRange gasPrice);
}
