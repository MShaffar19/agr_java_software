package org.alliancegenome.api.entity;

import java.io.Serializable;
import java.util.*;

import org.alliancegenome.neo4j.view.View;

import com.fasterxml.jackson.annotation.*;

import lombok.*;

@Getter
@Setter
public class RibbonSection implements Serializable {

    @JsonView({View.DiseaseAnnotation.class,View.Expression.class})
    private String id;
    @JsonView({View.DiseaseAnnotation.class,View.Expression.class})
    private String label;
    @JsonView({View.DiseaseAnnotation.class,View.Expression.class})
    private String description;
    @JsonProperty("class_label")
    private String classLabel;
    @JsonProperty("annotation_label")
    private String annotationLabel;

    @JsonView({View.DiseaseAnnotation.class,View.Expression.class})
    @JsonProperty("groups")
    private List<SectionSlim> slims = new ArrayList<>();

    public void addDiseaseSlim(SectionSlim slim) {
        slims.add(slim);
    }
}
