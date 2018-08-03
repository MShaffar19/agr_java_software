package org.alliancegenome.neo4j.entity.relationship;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;
import org.alliancegenome.neo4j.entity.Neo4jEntity;
import org.alliancegenome.neo4j.entity.node.Gene;
import org.alliancegenome.neo4j.view.OrthologyFilter;
import org.alliancegenome.neo4j.view.View;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@Getter
@Setter
@RelationshipEntity(type = "ORTHOLOGOUS")
public class Orthologous extends Neo4jEntity {

    @JsonView(View.OrthologyView.class)
    @StartNode
    private Gene gene1;
    @JsonView(View.OrthologyView.class)
    @EndNode
    private Gene gene2;

    private String primaryKey;
    @JsonView(View.OrthologyView.class)
    private boolean isBestRevScore;
    @JsonView(View.OrthologyView.class)
    private boolean isBestScore;
    private String confidence;
    private boolean moderateFilter;
    private boolean strictFilter;

    public boolean hasFilter(OrthologyFilter filter) {
        OrthologyFilter.Stringency stringency = filter.getStringency();
        if (stringency.equals(OrthologyFilter.Stringency.ALL))
            return true;
        if (stringency.equals(OrthologyFilter.Stringency.MODERATE) && moderateFilter)
            return true;
        if (stringency.equals(OrthologyFilter.Stringency.STRINGENT) && strictFilter)
            return true;
        return false;
    }
}
