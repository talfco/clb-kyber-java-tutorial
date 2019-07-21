package net.cloudburo.kyber.tutorial;

import java.io.FileReader;
import java.math.BigDecimal;
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
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Response;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;


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
        // We are connecting to Ethereu
        Properties props = new Properties();
        FileReader fi = new FileReader("secret/secret.properties");
        props.load(fi);

        // We start by creating a new web3j instance to connect to remote nodes on the network.
        Web3j web3j = Web3j.build(new HttpService(
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


    private void run() throws Exception {
        // FIXME: Request some Ether for the Ropsten test network at  https://faucet.ropsten.be
        log.info("Sending 1 Wei ("
                + Convert.fromWei("1", Convert.Unit.ETHER).toPlainString() + " Ether)");
        TransactionReceipt transferReceipt = Transfer.sendFunds(
                web3j, credentials,
                "0x8370DA48be315b1F73fdbF206a9A8678234a16a4",  // you can put any address here
                BigDecimal.ONE, Convert.Unit.WEI)  // 1 wei = 10^-18 Ether
                .send();
        log.info("Transaction complete, view it at https://ropsten.infura.io/v3/tx/"
                + transferReceipt.getTransactionHash());
    }

    private boolean checkForError(Response resp){
        if (resp.hasError()) {
            log.info("Error " + resp.getError().getMessage());
            return true;
        }
        return false;
    }

    private void eth2knc() {
        Kyber3j kyber3j = Kyber3j.build(new KyberService(KyberService.KYBER_ROPSTEN));
        log.info("Connected to Kyber Network: "+KyberService.KYBER_ROPSTEN);
        // Buy KNC from ETH
        try {
            // Check if token is supported
            Currencies currencies = kyber3j.currencies().send();
            log.info("Exists Currency KNC: " + currencies.existsCurreny("KNC"));
            if (!checkForError(currencies) && currencies.existsCurreny("KNC")) {
                // Get buy rates
                BuyRate buyRate = kyber3j.buyRate(currencies.getCurrency("KNC").getId(),"300",
                        false).send();
                if (!checkForError(buyRate)) {
                    Rates rates = buyRate.getData().get(0);
                    SingleRate singleRate = rates.getSingleRate(0);
                    log.info("Conversion Rates: " + singleRate.getSrc_qty());
                    // Get tradeData
                    // Adjust conversion rates to 97%
                    singleRate.approximateReceivableToken(0.97);
                    TradeData tradeData = kyber3j.tradeData(credentials.getAddress(), singleRate, GasPriceRange.medium).send();
                    if (!checkForError(tradeData)) {

                        return;
                    }
                    else
                        log.info("Received Trade Data " + tradeData.getData().getFrom());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        new Application().eth2knc();
    }
}
