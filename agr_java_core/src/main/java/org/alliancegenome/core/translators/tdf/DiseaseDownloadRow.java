package org.alliancegenome.core.translators.tdf;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DiseaseDownloadRow {

    private String alleleID;
    private String alleleSymbol;
    private String geneticEntityID;
    private String geneticEntityName;
    private String geneticEntityType;
    private String speciesID;
    private String speciesName;
    private String association;
    private String diseaseID;
    private String diseaseName;
    private String evidenceCode;
    private String evidenceCodeName;
    private String source;
    private String reference;
}
