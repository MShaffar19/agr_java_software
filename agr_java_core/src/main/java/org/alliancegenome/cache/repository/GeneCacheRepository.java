package org.alliancegenome.cache.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.alliancegenome.cache.manager.OrthologyAllianceCacheManager;
import org.alliancegenome.neo4j.entity.node.Gene;
import org.alliancegenome.neo4j.repository.GeneRepository;
import org.alliancegenome.neo4j.view.OrthologView;
import org.alliancegenome.neo4j.view.OrthologyFilter;
import org.alliancegenome.neo4j.view.View;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class GeneCacheRepository {

    private static GeneRepository geneRepository = new GeneRepository();

    // cached value
    private static List<Gene> allGenes = null;
    // Map<gene ID, Gene>
    private static Map<String, Gene> allGeneMap = new HashMap<>();
    private static boolean caching;

    private static boolean orthologyCaching;
    private static LocalDateTime startOrthology;
    private static LocalDateTime endOrthology;

    private static Map<String, Set<OrthologView>> orthologViewMap = new HashMap<>();

    public List<Gene> getAllGenes() {
        checkCache();
        if (caching)
            return null;

        return allGenes;
    }

    public Gene getGene(String geneID) {
        if (geneID == null)
            return null;
        checkCache();
        if (caching)
            return null;

        return allGeneMap.get(geneID);
    }

    private synchronized void checkCache() {
        if (allGenes == null && !caching) {
            caching = true;
            cacheAllGenes();
            caching = false;
        }
    }

    private void cacheAllGenes() {
        long start = System.currentTimeMillis();
        allGenes = geneRepository.getAllGenes();
        if (allGenes == null)
            return;

        allGenes.forEach(gene -> allGeneMap.put(gene.getPrimaryKey(), gene));
        log.info("Retrieved " + allGenes.size() + " genes");
        log.info("Time to retrieve genes " + ((System.currentTimeMillis() - start) / 1000) + " s");
    }

    public List<OrthologView> getAllOrthologyGenes(List<String> geneIDs) {

        OrthologyAllianceCacheManager manager = new OrthologyAllianceCacheManager();

        List<OrthologView> fullOrthologyList = new ArrayList<>();
        geneIDs.forEach(id -> fullOrthologyList.addAll(manager.getOrthology(id, View.Orthology.class)));

        return fullOrthologyList;

    }

}