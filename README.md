# clb-kyber-java-tutorial
Repository used for the Kyber Network tutorial, which will be delivered as part of the following KyberDAO proposal:
* DaoStack Ethereum Contract Proposal: https://alchemy.daostack.io/dao/0x6bee9b81e434f7afce72a43a4016719315069539/proposal/0x25ea4cdb14c844bcf44691e588305b2d157fae0d696c5a7440c360691243c955

# Progress Report: Expected Delivery 31.07.2019

* 01.07.2019: Initial kick off blog article:  https://dev.cloudburo.net/2019/07/01/dao-project-work-announcement-kyberdao-proposal.html
* 15.07.2019: Integration approach into web3j library completed and implemented two api methods `currencies` and `buyRate`

# Introduction

The aim of this tutorial is to showcase an initial implementation of a Kyber Java Client API.

As an implementation strategy an enhancement of the well-known [web3j](https://web3j.readthedocs.io/en/latest/) library was chosen.

web3j is a highly modular, reactive, type safe Java and Android library for working with Smart Contracts and integrating with clients (nodes) on the Ethereum network.

        KyberService srv = new KyberService(KyberService.KYBER_ROPSTEN);
        Kyber3j kyber3j = Kyber3j.build(srv);
        try {
            Currencies currencies = kyber3j.currencies().send();
            log.info("Exists Currency KNC: " + currencies.existsCurreny("KNC"));
            if (currencies.existsCurreny("KNC")) {
                BuyRate rate = kyber3j.buyRate(currencies.getCurrency("KNC").getId(),"300", false).send();
                Float price = rate.getData().get(0).getSrc_qty().get(0);
                log.info("Conversion Rate: "+price.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }