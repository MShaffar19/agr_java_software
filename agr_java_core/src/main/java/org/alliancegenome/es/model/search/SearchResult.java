package org.alliancegenome.es.model.search;

import java.util.*;

import lombok.*;

@Getter @Setter
public class SearchResult {

    private Map<String, Object> map;
    private List<String> relatedDataLinks;

}
