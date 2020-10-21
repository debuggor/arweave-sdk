
## arweave-sdk

### usage

Example: signing 

```java
// java8
public class TxSignTest {

    public static void main(String[] args) throws Exception {
        String seed = "01c4f3d1377cc4db62cc30b03af5f5528f50d2ca4fec75cd54999b1429514331";

        String to = "btYUJ5O_RmD8TP4uj42CKHfkLuC49otXF4MLttkYa3c";
        String amount = "100000000";
        String reward = "310296";
        String lastTx = "-irWDrhIynKEXnmeLh-8MRVaROLpgDWeS83FwFyZ58y1pUrXBfxkkzVjpfydaQpe";
        Transaction transaction = new Transaction(null, to, amount, reward, lastTx, "", null);
        transaction.signTX(seed);

        System.out.println(transaction.getId());
        System.out.println(transaction.toJson());
    }

}
```
