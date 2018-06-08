package org.alliancegenome.api.controller;

import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import org.alliancegenome.api.rest.interfaces.GeneRESTInterface;
import org.alliancegenome.api.service.GeneService;
import org.alliancegenome.es.model.search.SearchResult;

@RequestScoped
public class GeneController extends BaseController implements GeneRESTInterface {

    @Inject
    private GeneService geneService;

    @Override
    public Map<String, Object> getGene(String id) {
        Map<String, Object> ret = geneService.getById(id);
        if(ret == null) {
            throw new NotFoundException();
        } else {
            return ret;
        }
    }

    @Override
    public SearchResult getAllelesPerGene(String id) {
        return geneService.getAllelesByGene(id);
    }

}