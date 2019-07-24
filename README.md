# clb-kyber-java-tutorial
Repository used for the Kyber Network tutorial, which will be delivered as part of the following KyberDAO proposal:
* DaoStack Ethereum Contract Proposal: https://alchemy.daostack.io/dao/0x6bee9b81e434f7afce72a43a4016719315069539/proposal/0x25ea4cdb14c844bcf44691e588305b2d157fae0d696c5a7440c360691243c955

# Progress Report: Expected Delivery 31.07.2019

* 01.07.2019: Initial kick off blog article:  https://dev.cloudburo.net/2019/07/01/dao-project-work-announcement-kyberdao-proposal.html
* 15.07.2019: Integration approach into web3j library completed and implemented two api methods `currencies` and `buyRate`
* 24.07.2019: All three scenarios, eth2token, token2eth and token2token implemented, outstanding is the Medium Article

# Introduction

The aim of this tutorial is to showcase an initial implementation of a Kyber Java Client API.

As an implementation strategy an enhancement of the well-known [web3j](https://web3j.readthedocs.io/en/latest/) library was chosen.

web3j is a highly modular, reactive, type safe Java and Android library for working with Smart Contracts and integrating with clients (nodes) on the Ethereum network.



## Example Scenario: Perform ETH -> KNC (ERC20 token) conversion

The scenario consists of the following step

* check for the tokens supported on the Kyber Network, in our case the `KNC` token
* get the KNC/ETH buy rates
* get the trade data record
* sign and execute the transactions

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
                    BuyRate buyRate = kyber3j.buyRate(currencies.getCurrency("KNC").getId(),"1",
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
                            TradeData.TradeDataRecord rec = tradeData.getData().get(0);
                            RawTransaction rawTransaction = RawTransaction.createTransaction(
                                    rec.getNonceAsBI(),  rec.getGasPriceAsBI(), rec.getGasLimitAsBI(),
                                    rec.getTo(), rec.getValueAsBI(), rec.getData());
                            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
                            String hexValue = Numeric.toHexString(signedMessage);
                            log.info("Signe Message with Hex Value: "+hexValue);
                            //EthSendTransaction ethTrx = web3j.ethSendRawTransaction(hexValue).send();
                            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
                            log.info("Executed transaction "+ethSendTransaction.getTransactionHash());
                            // poll for transaction response via org.web3j.protocol.Web3j.ethGetTransactionReceipt(<txHash>)
                            return;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }