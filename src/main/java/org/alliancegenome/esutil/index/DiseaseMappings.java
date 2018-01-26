package org.alliancegenome.esutil.index;

import java.io.IOException;

import org.alliancegenome.esutil.schema.Mappings;

public class DiseaseMappings extends Mappings {

	public DiseaseMappings(Boolean pretty) {
		super(pretty);
	}

	public void buildMappings() {
		try {

			builder.startObject();


			builder.startObject("properties");

			buildSharedSearchableDocumentMappings();

			buildGenericField("geneDocument.symbol", "string", null, false, false, true, false);
			buildGenericField("disease_species.orderID", "long", null, false, false, true, false);
			buildGenericField("diseaseID", "string", null, false, false, true, false);
			buildGenericField("diseaseName", "string", null, false, false, true, false);
			buildGenericField("parentDiseaseIDs", "string", null, false, false, true, false);

			builder.endObject();

			builder.endObject();


		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
