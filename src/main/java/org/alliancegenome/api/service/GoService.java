package org.alliancegenome.api.service;

import java.util.Map;

import javax.enterprise.context.RequestScoped;

import org.alliancegenome.shared.es.dao.site_index.GoDAO;

@RequestScoped
public class GoService {

    private static GoDAO goDAO = new GoDAO();

    public Map<String, Object> getById(String id) {
        return goDAO.getById(id);
    }

    
}
