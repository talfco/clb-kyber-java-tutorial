package net.cloudburo.kyber.tutorial;

import java.io.FileReader;
import java.math.BigDecimal;
import java.util.Properties;

import net.cloudburo.kyber.tutorial.protocol.Kyber3j;
import net.cloudburo.kyber.tutorial.protocol.KyberService;
import net.cloudburo.kyber.tutorial.methods.response.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;


/**
 * A simple web3j application that demonstrates a number of core features of the Kyber network:
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

    public static void main(String[] args) throws Exception {
        new Application().run1();
    }


    private void run() throws Exception {

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
        Credentials credentials =
                WalletUtils.loadCredentials(
                        pw,
                        "secret/wallet.json");
        log.info("Credentials loaded");

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

    private void run1() {
        KyberService srv = new KyberService(KyberService.KYBER_ROPSTEN);
        Kyber3j kyber3j = Kyber3j.build(srv);
        try {
            Request req  = kyber3j.currencies();
            Currencies obj = (Currencies)req.send();
            log.info("Exists Currency KNC: " + obj.existsCurreny("KNC"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
