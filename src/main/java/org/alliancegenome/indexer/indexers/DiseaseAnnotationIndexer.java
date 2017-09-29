package org.alliancegenome.indexer.indexers;


import org.alliancegenome.indexer.config.IndexerConfig;
import org.alliancegenome.indexer.document.DiseaseAnnotationDocument;
import org.alliancegenome.indexer.entity.node.DOTerm;
import org.alliancegenome.indexer.repository.DiseaseRepository;
import org.alliancegenome.indexer.translators.DiseaseTranslator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class DiseaseAnnotationIndexer extends Indexer<DiseaseAnnotationDocument> {

    private final Logger log = LogManager.getLogger(getClass());

    private final DiseaseRepository diseaseRepository = new DiseaseRepository();
    private final DiseaseTranslator diseaseTrans = new DiseaseTranslator();

    public DiseaseAnnotationIndexer(String currentIndex, IndexerConfig config) {
        super(currentIndex, config);
    }

    @Override
    public void index() {

        try {
            LinkedBlockingDeque<String> queue = new LinkedBlockingDeque<>();
            List<String> allDiseaseIDs = diseaseRepository.getAllDiseaseKeys();
            queue.addAll(allDiseaseIDs);
            diseaseRepository.clearCache();

            Integer numberOfThreads = indexerConfig.getThreadCount();
            ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
            int index = 0;
            while (index++ < numberOfThreads) {
                executor.submit(() -> startThread(queue));
            }


            int total = queue.size();
            startProcess(total);
            while (!queue.isEmpty()) {
                TimeUnit.SECONDS.sleep(30);
                progress(queue.size(), total);
            }
            finishProcess(total);
            executor.shutdown();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void startThread(LinkedBlockingDeque<String> queue) {
        ArrayList<DOTerm> list = new ArrayList<DOTerm>();
        DiseaseRepository repo = new DiseaseRepository();
        while (true) {
            try {
                if (list.size() >= indexerConfig.getBufferSize()) {
                    addDocuments(diseaseTrans.translateAnnotationEntities(list, 1));
                    repo.clearCache();
                    list.clear();
                    list = new ArrayList<>();
                }
                if (queue.isEmpty()) {
                    if (list.size() > 0) {
                        addDocuments(diseaseTrans.translateAnnotationEntities(list, 1));
                        list.clear();
                        repo.clearCache();
                    }
                    return;
                }

                String key = queue.takeFirst();
                DOTerm disease = repo.getDiseaseTermWithAnnotations(key);
                if (disease != null) {
                    list.add(disease);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
