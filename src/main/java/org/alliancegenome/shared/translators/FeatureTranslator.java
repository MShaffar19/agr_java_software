package org.alliancegenome.shared.translators;

import java.util.ArrayList;

import org.alliancegenome.shared.es.document.site_index.FeatureDocument;
import org.alliancegenome.shared.neo4j.entity.node.Feature;
import org.alliancegenome.shared.neo4j.entity.node.SecondaryId;
import org.alliancegenome.shared.neo4j.entity.node.Synonym;

public class FeatureTranslator extends EntityDocumentTranslator<Feature, FeatureDocument> {

	@Override
	protected FeatureDocument entityToDocument(Feature entity, int translationDepth) {

		FeatureDocument featureDocument = new FeatureDocument();

		//allele.setDataProvider(entity.getDataProvider());
		featureDocument.setDateProduced(entity.getDateProduced());
		featureDocument.setGlobalId(entity.getGlobalId());
		featureDocument.setLocalId(entity.getLocalId());
		featureDocument.setPrimaryKey(entity.getPrimaryKey());
		featureDocument.setRelease(entity.getRelease());
		featureDocument.setSymbol(entity.getSymbol());

		if(translationDepth > 0) {

			// This code is duplicated in Gene and Feature should be pulled out into its own translator
			ArrayList<String> secondaryIds = new ArrayList<>();
			if (entity.getSecondaryIds() != null) {
				for (SecondaryId secondaryId : entity.getSecondaryIds()) {
					secondaryIds.add(secondaryId.getName());
				}
			}
			featureDocument.setSecondaryIds(secondaryIds);

			// This code is duplicated in Gene and Feature should be pulled out into its own translator
			ArrayList<String> synonyms = new ArrayList<>();
			if (entity.getSynonyms() != null) {
				for (Synonym synonym : entity.getSynonyms()) {
					if (synonym.getPrimaryKey() != null) {
						synonyms.add(synonym.getPrimaryKey());
					} else {
						synonyms.add(synonym.getName());
					}
				}
			}
			featureDocument.setSynonyms(synonyms);

		}

		return featureDocument;
	}

	@Override
	protected Feature documentToEntity(FeatureDocument doument, int translationDepth) {
		// We are not going to the database yet so will implement this when we need to
		return null;
	}

}
