package vr.com.apps.transactionLinking.service.banksTransactions;

import vr.com.apps.transactionLinking.service.ResourceList;
import vr.com.apps.utility.Utils;
import vr.com.apps.utility.resourceReader.ResourceLoader;
import vr.com.apps.utility.resourceReader.ResourceProperty;
import vr.com.apps.utility.resourceReader.ResourceReader;

import java.io.IOException;

public class PBResourceListBankStatement<T extends BankStatement> extends ResourceList<T> {

    public PBResourceListBankStatement(ResourceProperty resourceProperty) {
        super(resourceProperty);
    }

    @Override
    public void downloadResource() throws IOException {
        super.downloadResource();
        ResourceReader orderResourceReader = ResourceLoader.getResource(resourceProperty);
        parseResource(orderResourceReader);
    }

    private void parseResource(ResourceReader resourceReader){
        if (!resourceReader.exists()){
            return;
        }
        String resourceStr = resourceReader.getString("UTF-8");

        String[] arrayValue = resourceStr.split("\\r\\n");
        for (String benchmarkData : arrayValue) {
            PBBankStatement bankStatement = new PBBankStatement();
            parseBenchmarkDataToObject(bankStatement, benchmarkData);
            add((T) bankStatement);
        }

    }

    public void parseBenchmarkDataToObject (PBBankStatement bankStatement, String benchmarkData){
        bankStatement.setBenchmarkData(benchmarkData);

        String[] transactionStr = benchmarkData.split(",");

        bankStatement.setDate(Utils.parseDate(transactionStr[0], "dd/MM/yy"));
        bankStatement.setAmount((long) Math.round(new Float(transactionStr[1]) * 100));

        StringBuilder sb = new StringBuilder("");
        for (int i = 2; i < transactionStr.length; i++) {
            String descriptionPart = transactionStr[i];
            sb.append(descriptionPart);
        }
        bankStatement.setDescription(sb.toString());
    }

    @Override
    public int hashCode() {
        return "ResourceListBankStatementPB".hashCode();
    }
}
