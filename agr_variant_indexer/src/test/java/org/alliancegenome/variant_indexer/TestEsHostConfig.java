package org.alliancegenome.variant_indexer;

import org.alliancegenome.core.config.ConfigHelper;
import org.alliancegenome.es.util.EsClientFactory;

public class TestEsHostConfig {
    
    public static void main(String[] args) {
        ConfigHelper.init();
        
        System.out.println(ConfigHelper.getEsHostMap());
        
        EsClientFactory.getDefaultEsClient();
        
        
    }
}
