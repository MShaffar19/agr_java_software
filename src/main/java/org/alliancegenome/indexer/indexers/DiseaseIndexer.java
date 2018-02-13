package org.alliancegenome.indexer.indexers;

import org.alliancegenome.indexer.config.IndexerConfig;
import org.alliancegenome.shared.es.document.site_index.DiseaseDocument;
import org.alliancegenome.shared.neo4j.entity.node.DOTerm;
import org.alliancegenome.shared.neo4j.repository.DiseaseRepository;
import org.alliancegenome.shared.translators.DiseaseTranslator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

public class DiseaseIndexer extends Indexer<DiseaseDocument> {

    private final Logger log = LogManager.getLogger(getClass());

    public DiseaseIndexer(IndexerConfig config) {
        super(config);
    }

    @Override
    public void index() {
        DiseaseRepository diseaseRepository = new DiseaseRepository();

            LinkedBlockingDeque<String> queue = new LinkedBlockingDeque<>();
            List<String> allDiseaseIDs = diseaseRepository.getAllDiseaseKeys();
            queue.addAll(allDiseaseIDs);
            diseaseRepository.clearCache();
            startSingleThread(queue);

    }

    protected void startSingleThread(LinkedBlockingDeque<String> queue) {
        DiseaseTranslator diseaseTrans = new DiseaseTranslator();
        List<DOTerm> list = new ArrayList<>();
        DiseaseRepository repo = new DiseaseRepository(); // Due to repo not being thread safe
        while (true) {
            try {
                if (list.size() >= indexerConfig.getBufferSize()) {
                    saveDocuments(diseaseTrans.translateEntities(list));
                    list.clear();
                    repo.clearCache();
                }
                if (queue.isEmpty()) {
                    if (list.size() > 0) {
                        saveDocuments(diseaseTrans.translateEntities(list));
                        list.clear();
                        repo.clearCache();
                    }
                    return;
                }

                String key = queue.takeFirst();
                DOTerm disease = repo.getDiseaseTerm(key);
                if (disease != null) {
                    list.add(disease);
                } else {
                    log.debug("No disease found for " + key);
                }
            } catch (Exception e) {
                log.error("Error while indexing...", e);
                return;
            }
        }
    }


}
