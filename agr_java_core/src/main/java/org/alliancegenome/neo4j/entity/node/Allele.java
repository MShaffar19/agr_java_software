package org.alliancegenome.neo4j.entity.node;

import java.util.ArrayList;
import java.util.List;

import org.alliancegenome.api.entity.PresentationEntity;
import org.alliancegenome.neo4j.view.View;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.Setter;

@NodeEntity(label = "Feature")
@Getter
@Setter
public class Allele extends GeneticEntity implements Comparable<Allele>, PresentationEntity {

    public Allele() {
        this.crossReferenceType = CrossReferenceType.ALLELE;
    }

    private String release;
    private String localId;
    private String globalId;
    private String modCrossRefCompleteUrl;
    @JsonView({View.Default.class})
    private String symbolText;
    private String symbolTextWithSpecies;
    @JsonView({View.AlleleAPI.class})
    private String description;

    @JsonView({View.AlleleAPI.class})
    @Relationship(type = "IS_ALLELE_OF")
    private Gene gene;

    @JsonView({View.GeneAllelesAPI.class})
    @Relationship(type = "IS_IMPLICATED_IN", direction = Relationship.INCOMING)
    private List<DOTerm> diseases = new ArrayList<>();

    @JsonView({View.GeneAllelesAPI.class})
    @Relationship(type = "VARIATION", direction = Relationship.INCOMING)
    private List<Variant> variants = new ArrayList<>();

    @Relationship(type = "ASSOCIATION", direction = Relationship.UNDIRECTED)
    private List<DiseaseEntityJoin> diseaseEntityJoins = new ArrayList<>();

    @JsonView({View.GeneAllelesAPI.class})
    @Relationship(type = "HAS_PHENOTYPE")
    private List<Phenotype> phenotypes = new ArrayList<>();

    @JsonView({View.AlleleAPI.class})
    @Relationship(type = "CONTAINS")
    private List<Construct> constructs;

    @Override
    public int compareTo(Allele o) {
        return 0;
    }

    @Override
    public String toString() {
        return primaryKey + ":" + symbolText;
    }
}
