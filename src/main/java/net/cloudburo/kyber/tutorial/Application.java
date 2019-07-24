package net.cloudburo.kyber.tutorial;

import java.io.FileReader;
import java.util.Properties;

import net.cloudburo.kyber.tutorial.methods.request.GasPriceRange;
import net.cloudburo.kyber.tutorial.methods.request.Rates;
import net.cloudburo.kyber.tutorial.methods.request.SingleRate;
import net.cloudburo.kyber.tutorial.protocol.Kyber3j;
import net.cloudburo.kyber.tutorial.protocol.KyberService;
import net.cloudburo.kyber.tutorial.methods.response.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;


/**
 * A simple web3j application that demonstrates a number of core features of the KyberAPI network:
 *
 * <ol>
 *     <li>Connecting to a node on the Ethereum network</li>
 *     <li>Loading an Ethereum wallet file</li>
 *     <li>Connecting to a node on the Ethereum network</li>
 *     <li>Sending Ether from one address to another</li>
 * </ol>
 *
 * <p>To run this demo, you will need to provide:
 *
 * <ol>
 *     <li>Ethereum client (or node) endpoint. The simplest thing to do is
 *     <a href="https://infura.io/register.html">request a free access token from Infura</a></li>
 *     <li>A wallet file. This can be generated using the web3j
 *     <a href="https://docs.web3j.io/command_line.html">command line tools</a></li>
 *     <li>Some Ether. This can be requested from the
 *     <a href=" https://faucet.ropsten.be">Ropsten Faucet</a></li>
 * </ol>
 *
 * <p>For further background information, refer to the project README.
 */
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);
    private Web3j web3j;
    private Credentials credentials;

    public Application() throws Exception {
        // We are connecting to Ethereum
        Properties props = new Properties();
        FileReader fi = new FileReader("secret/secret.properties");
        props.load(fi);

        // We start by creating a new web3j instance to connect to remote nodes on the network.
        web3j = Web3j.build(new HttpService(
                "https://ropsten.infura.io/v3/"+props.get("infura-token")));  // FIXME: Enter your Infura token here;
        log.info("Connected to Ethereum client version: "
                + web3j.web3ClientVersion().send().getWeb3ClientVersion());

        String pw = (String)props.get("wallet-password");
        // We then need to load our Ethereum wallet file
        // FIXME: Generate a new wallet file using the web3j command line tools https://docs.web3j.io/command_line.html
        credentials =
                WalletUtils.loadCredentials(
                        pw,
                        "secret/wallet.json");
        log.info("Credentials loaded");
    }

    private boolean checkForError(Response resp){
        if (resp.hasError()) {
            log.info("Error " + resp.getError().getMessage());
            return true;
        }
        return false;
    }


    private void token2token(String tokenSymbolFrom, String tokenSymbolTo, String tokenQuantity) {
        //Suppose we want to convert 100 BAT to DAI tokens, which is a token to token conversion.
        // Note that ETH is used as the base pair i.e. BAT -> ETH -> DAI.
        Kyber3j kyber3j = Kyber3j.build(new KyberService(KyberService.KYBER_ROPSTEN));
        log.info("Connected to Kyber Network: "+KyberService.KYBER_ROPSTEN);
        try {
            Currencies currencies = kyber3j.currencies().send();
            if (!checkForError(currencies) && currencies.existsCurreny(tokenSymbolFrom)
                    && currencies.existsCurreny(tokenSymbolTo) ) {
                EnabledTokensForWallet tokens = kyber3j.enabledTokensForWallet(credentials.getAddress()).send();
                if (!checkForError(tokens)) {
                    // Check if wallet is enabled for tokens
                    String tokenId = currencies.getCurrency(tokenSymbolFrom).getId();

                    EnabledTokensForWallet.EnabledTokenStatus tokenStatus = tokens.getEnabledTokenStatus(tokenId);
                    if ( tokenStatus.isEnabled()) {
                        // Check if the sell token is already enabled to be sold by the network on behalf of this user
                        if (tokenStatus.getTxs_required() == 1) {
                            EnableTokenTransfer tokenData = kyber3j.enableTokenTransfer(credentials.getAddress(), tokenId,
                                    GasPriceRange.medium).send();
                            executeEthereumTransaction(tokenData.getData());
                        } else if (tokenStatus.getTxs_required() == 2) {
                            // TODO Implement validation
                            log.error("Not implemented for getTxs_required = 2");
                        }
                    } else {
                        log.error("Curreny not supported");
                        return;
                    }

                    // Get Sell Rate in ETH: <fromTokenQuantity> -> ETH ?
                    SellRate sellRate = kyber3j.sellRate(currencies.getCurrency(tokenSymbolFrom).getId(), tokenQuantity,
                            false).send();
                    if (!checkForError(sellRate)) {
                        Rates rates = sellRate.getData().get(0);
                        SingleRate singleRateFromToken = rates.getSingleRate(0);
                        Float sellQty = singleRateFromToken.getDst_qty();
                        log.info(tokenSymbolFrom+" Sell Rate: " + singleRateFromToken.getSrc_qty());
                        // Get Buy Rate for 1 toToken:  ETH ? -> 1 <toToken>
                        BuyRate buyRate = kyber3j.buyRate(currencies.getCurrency(tokenSymbolTo).getId(),"1",
                                false).send();
                        if (!checkForError(buyRate)) {
                            rates = buyRate.getData().get(0);
                            SingleRate singleRateToToken = rates.getSingleRate(0);
                            Float buyQty = singleRateToToken.getSrc_qty();
                            Float expectedAmountWithoutSlippage = sellQty / buyQty; // * Float.valueOf(tokenQuantity);
                            Float expectedAmountWithSlippage = expectedAmountWithoutSlippage * 0.97f;
                            singleRateFromToken.setDst_id(singleRateToToken.getDst_id());
                            singleRateFromToken.setDst_qty(expectedAmountWithSlippage);
                            TradeData tradeData = kyber3j.tradeData(credentials.getAddress(), singleRateFromToken, GasPriceRange.medium).send();
                            if (!checkForError(tradeData)) {
                                executeEthereumTransaction(tradeData.getData().get(0));
                            }
                        }
                    }
                }
            }
        } catch (Exception e){e.printStackTrace();}
    }

    private void token2eth(String tokenSymbol, String tokenQuantity){
        Kyber3j kyber3j = Kyber3j.build(new KyberService(KyberService.KYBER_ROPSTEN));
        log.info("Connected to Kyber Network: "+KyberService.KYBER_ROPSTEN);
        try {
            // Check if token is supported
            Currencies currencies = kyber3j.currencies().send();
            if (!checkForError(currencies) && currencies.existsCurreny(tokenSymbol)) {
                EnabledTokensForWallet tokens = kyber3j.enabledTokensForWallet(credentials.getAddress()).send();
                if (!checkForError(tokens)){
                    // Check if wallet is enabled for token
                    String tokenId = currencies.getCurrency(tokenSymbol).getId();
                    EnabledTokensForWallet.EnabledTokenStatus tokenStatus = tokens.getEnabledTokenStatus(tokenId);
                    if ( tokenStatus.isEnabled()) {
                        if (tokenStatus.getTxs_required() == 1) {
                            // Enable Token Transfer
                            // https://developer.kyber.network/docs/Integrations-RESTfulAPIGuide/#check-and-approve-bat-token-contract
                            EnableTokenTransfer tokenData = kyber3j.enableTokenTransfer(credentials.getAddress(), tokenId,
                                    GasPriceRange.medium).send();
                            executeEthereumTransaction(tokenData.getData());
                        } else if (tokenStatus.getTxs_required() == 2) {
                            // TODO Implement validation
                            log.error("Not implemented for getTxs_required = 2");
                        }
                        SellRate sellRate = kyber3j.sellRate(currencies.getCurrency(tokenSymbol).getId(), tokenQuantity,
                                false).send();
                        if (!checkForError(sellRate)) {
                            Rates rates = sellRate.getData().get(0);
                            SingleRate singleRate = rates.getSingleRate(0);
                            log.info("Conversion Rate: " + singleRate.getSrc_qty());
                            singleRate.approximateReceivableToken(0.97);
                            TradeData tradeData = kyber3j.tradeData(credentials.getAddress(), singleRate, GasPriceRange.medium).send();
                            if (!checkForError(tradeData)) {
                                executeEthereumTransaction(tradeData.getData().get(0));
                            }
                        }
                    }
                }
            }
        } catch (Exception e){ e.printStackTrace(); }
    }

    private void eth2token(String tokenSymbol, String tokenQuantity) {
        Kyber3j kyber3j = Kyber3j.build(new KyberService(KyberService.KYBER_ROPSTEN));
        log.info("Connected to Kyber Network: "+KyberService.KYBER_ROPSTEN);
        // ETH2<Token> Swap
        try {
            // Check if token is supported
            Currencies currencies = kyber3j.currencies().send();
            log.info("Exists Currency"+tokenSymbol+": " + currencies.existsCurreny(tokenSymbol));
            if (!checkForError(currencies) && currencies.existsCurreny(tokenSymbol)) {
                // Get buy rates
                BuyRate buyRate = kyber3j.buyRate(currencies.getCurrency(tokenSymbol).getId(),tokenQuantity,
                        false).send();
                if (!checkForError(buyRate)) {
                    Rates rates = buyRate.getData().get(0);
                    SingleRate singleRate = rates.getSingleRate(0);
                    log.info("Conversion Rate: " + singleRate.getSrc_qty());
                    // Get tradeData
                    // Adjust conversion rates to 97%
                    singleRate.approximateReceivableToken(0.97);
                    TradeData tradeData = kyber3j.tradeData(credentials.getAddress(), singleRate, GasPriceRange.medium).send();
                    if (!checkForError(tradeData)) {
                        executeEthereumTransaction(tradeData.getData().get(0));
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void executeEthereumTransaction(EtherRecord rec) throws Exception {
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                rec.getNonceAsBI(), rec.getGasPriceAsBI(), rec.getGasLimitAsBI(),
                rec.getTo(), rec.getValueAsBI(), rec.getData());
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        log.info("Signe Message with Hex Value: " + hexValue);
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        log.info("Executed transaction " + ethSendTransaction.getTransactionHash());
    }

    public static void main(String[] args) throws Exception {
        //new Application().token2eth("DAI","1");
        //new Application().eth2token("BAT","1");
        new Application().token2token("DAI","BAT","0.5");
    }


}
