package org.alliancegenome.core.service;

import org.alliancegenome.es.model.query.FieldFilter;
import org.alliancegenome.neo4j.entity.PrimaryAnnotatedEntity;

import java.util.Set;
import java.util.stream.Collectors;

public class PrimaryAnnotatedEntityFiltering extends AnnotationFiltering<PrimaryAnnotatedEntity> {


    public FilterFunction<PrimaryAnnotatedEntity, String> modelNameFilter =
            (annotedEntity, value) -> FilterFunction.contains(annotedEntity.getName(), value);

    public FilterFunction<PrimaryAnnotatedEntity, String> termNameFilter =
            (annotatedEntity, value) -> {
                Set<Boolean> filteringPassed = annotatedEntity.getDiseases().stream()
                        .map(disease -> FilterFunction.contains(disease.getName(), value))
                        .collect(Collectors.toSet());
                return filteringPassed.contains(true);
            };

    /*

    public FilterFunction<PrimaryAnnotatedEntity, String> sourceFilter =
            (annotation, value) -> FilterFunction.contains(annotation.getSource().getName(), value);

    public FilterFunction<PrimaryAnnotatedEntity, String> geneNameFilter =
            (annotation, value) -> FilterFunction.contains(annotation.getGene().getSymbol(), value);

*/
    public FilterFunction<PrimaryAnnotatedEntity, String> geneSpeciesFilter =
            (annotation, value) -> FilterFunction.fullMatchMultiValueOR(annotation.getSpecies().getName(), value);

    public PrimaryAnnotatedEntityFiltering() {
/*
        filterFieldMap.put(FieldFilter.EVIDENCE_CODE, evidenceCodeFilter);
        filterFieldMap.put(FieldFilter.FREFERENCE, referenceFilter);
        filterFieldMap.put(FieldFilter.SOURCE, sourceFilter);
        filterFieldMap.put(FieldFilter.GENE_NAME, geneNameFilter);
*/
        filterFieldMap.put(FieldFilter.DISEASE, termNameFilter);
        filterFieldMap.put(FieldFilter.MODEL_NAME, modelNameFilter);
        filterFieldMap.put(FieldFilter.SPECIES, geneSpeciesFilter);
    }

}
