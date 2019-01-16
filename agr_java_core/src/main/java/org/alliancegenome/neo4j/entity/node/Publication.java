package org.alliancegenome.neo4j.entity.node;

import java.util.List;

import org.alliancegenome.neo4j.entity.Neo4jEntity;
import org.alliancegenome.neo4j.view.View;
import org.apache.commons.lang3.StringUtils;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.Setter;

@NodeEntity
@Getter
@Setter
public class Publication extends Neo4jEntity implements Comparable<Publication> {

    @JsonView({View.Interaction.class, View.Expression.class})
    private String primaryKey;
    @JsonView({View.Interaction.class, View.Expression.class})
    private String pubMedId;
    @JsonView({View.Interaction.class, View.Expression.class})
    private String pubMedUrl;
    @JsonView({View.Interaction.class, View.Expression.class})
    private String pubModId;
    @JsonView({View.Interaction.class, View.Expression.class})
    private String pubModUrl;
    @JsonView({View.Expression.class})
    private String pubId;

    @Relationship(type = "ANNOTATED_TO")
    private List<EvidenceCode> evidence;

    public void setPubIdFromId() {
        if (StringUtils.isNotEmpty(pubMedId)) {
            pubId = pubMedId;
        } else {
            pubId = pubModId;
        }
    }

    @JsonView({View.Phenotype.class, View.Expression.class})
    @JsonProperty("id")
    private String getPublicationId() {
        if (StringUtils.isNotEmpty(pubMedId)) {
            return pubMedId;
        } else {
            return pubModId;
        }
    }

    @JsonView({View.Phenotype.class, View.Expression.class})
    @JsonProperty("url")
    private String getPublicationUrl() {
        if (StringUtils.isNotEmpty(pubMedId)) {
            return pubMedUrl;
        } else {
            return pubModUrl;
        }
    }

    @Override
    public String toString() {
        return getPublicationId() + " : " + getPublicationUrl();
    }

    @Override
    public int compareTo(Publication o) {
        return pubId.compareTo(o.getPubId());
    }
}
