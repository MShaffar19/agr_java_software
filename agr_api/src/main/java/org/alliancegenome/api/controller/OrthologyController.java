package org.alliancegenome.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.alliancegenome.api.rest.interfaces.OrthologyRESTInterface;
import org.alliancegenome.core.service.JsonResultResponse;
import org.alliancegenome.neo4j.entity.node.OrthoAlgorithm;
import org.alliancegenome.neo4j.repository.OrthologousRepository;
import org.alliancegenome.neo4j.view.OrthologView;
import org.alliancegenome.neo4j.view.OrthologyFilter;
import org.alliancegenome.neo4j.view.View;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrthologyController implements OrthologyRESTInterface {

    public static final String API_VERSION = "0.9";

    @Context
    private HttpServletRequest request;

    @Override
    public String getDoubleSpeciesOrthology(String taxonIDOne,
                                            String taxonIDTwo,
                                            String stringencyFilter,
                                            String methods,
                                            Integer rows,
                                            Integer start) throws IOException {

        LocalDateTime startDate = LocalDateTime.now();
        OrthologousRepository orthoRepo = new OrthologousRepository();
        List<String> methodList = new ArrayList<>();
        methodList.add(methods);
        OrthologyFilter orthologyFilter = new OrthologyFilter(stringencyFilter, null, methodList);

        if (rows != null)
            orthologyFilter.setRows(rows);
        if (start != null)
            orthologyFilter.setStart(start);

        JsonResultResponse<OrthologView> response;
        response = orthoRepo.getOrthologyByTwoSpecies(taxonIDOne, taxonIDTwo, orthologyFilter);

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        response.calculateRequestDuration(startDate);
        response.setApiVersion(API_VERSION);
        response.setHttpServletRequest(request);
        return mapper.writerWithView(View.OrthologyView.class).writeValueAsString(response);
    }

    @Override
    public String getSingleSpeciesOrthology(String species,
                                            String stringencyFilter,
                                            String methods,
                                            Integer rows,
                                            Integer start) throws IOException {
        return getDoubleSpeciesOrthology(species, null, stringencyFilter, methods, rows, start);
    }

    @Override
    public String getMultiSpeciesOrthology(List<String> taxonID, String taxonIdList, String stringencyFilter, String methods, Integer rows, Integer start) throws IOException {
        LocalDateTime startDate = LocalDateTime.now();
        JsonResultResponse response = new JsonResultResponse();
        response.setNote("Not yet implemented");
        response.calculateRequestDuration(startDate);
        response.setApiVersion(API_VERSION);
        response.setHttpServletRequest(request);
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        response.calculateRequestDuration(startDate);
        return mapper.writeValueAsString(response);
    }

    @Override
    public String getMultiGeneOrthology(List<String> geneIDs,
                                        String geneList,
                                        String stringencyFilter,
                                        List<String> methods,
                                        Integer rows,
                                        Integer start) throws IOException {
        GeneController controller = new GeneController();
        controller.setRequest(request);
        return controller.getGeneOrthology(null, geneIDs, geneList, stringencyFilter, null, methods, rows, start);
    }

    @Override
    public String getAllMethodsCalculations() throws JsonProcessingException {
        LocalDateTime startDate = LocalDateTime.now();
        OrthologousRepository orthoRepo = new OrthologousRepository();
        JsonResultResponse<OrthoAlgorithm> response = new JsonResultResponse<>();
        List<OrthoAlgorithm> methodList = orthoRepo.getAllMethods();
        response.setResults(methodList);
        response.setTotal(methodList.size());
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        response.calculateRequestDuration(startDate);
        response.setApiVersion(API_VERSION);
        response.setHttpServletRequest(request);
        return mapper.writerWithView(View.OrthologyMethodView.class).writeValueAsString(response);
    }
}
