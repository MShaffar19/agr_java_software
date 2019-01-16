package org.alliancegenome.es.model.query;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import org.alliancegenome.neo4j.view.BaseFilter;
import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Pagination {

    private int page;
    private int limit;
    private String sortBy;
    private FieldFilter sortByField;
    private Boolean asc;
    private BaseFilter fieldFilterValueMap = new BaseFilter();
    private List<String> errorList = new ArrayList<>();

    private boolean isCount = false;
    public Pagination(int page, int limit, String sortBy, String asc) {
        this.page = page;
        this.limit = limit;
        this.sortBy = sortBy;
        sortByField = FieldFilter.getFieldFilterByName(sortBy);
        init(asc);
    }

    public Pagination() {
        isCount = true;
    }

    public boolean isCountPagination(){
        return isCount;
    }

    private void init(String asc) {
        if (page < 1)
            errorList.add("Invalid 'page' value. Needs to be greater or equal than 1");
        if (limit < 1)
            errorList.add("Invalid 'limit' value. Needs to be greater or equal than 1");

        if (asc == null) {
            this.asc = true;
        } else {
            if (!AscendingValues.isValidValue(asc)) {
                String message = "Invalid 'asc' value. Needs to have the following values: [";
                message = message + AscendingValues.getAllValues() + "]";
                errorList.add(message);
            }
            this.asc = AscendingValues.getValue(asc);
        }
    }

    public void addFieldFilter(FieldFilter fieldFilter, String value) {
        fieldFilterValueMap.put(fieldFilter, value);
    }

    public void makeSingleFieldFilter(FieldFilter fieldFilter, String value) {
        fieldFilterValueMap.clear();
        fieldFilterValueMap.put(fieldFilter, value);
    }

    public void removeFieldFilter(FieldFilter fieldFilter) {
        fieldFilterValueMap.remove(fieldFilter);
    }

    public boolean hasErrors() {
        return !errorList.isEmpty();
    }

    public List<String> getErrors() {
        return errorList;
    }

    public boolean sortByDefault() {
        if (StringUtils.isEmpty(sortBy))
            return true;
        if (sortBy.equalsIgnoreCase("default"))
            return true;
        return false;
    }

    public int getStart() {
        return (page - 1) * limit;
    }

    public int getEnd() {
        return page * limit;
    }

    enum AscendingValues {
        TRUE(true), FALSE(false), YES(true), NO(false), UP(true), DOWN(false);

        private Boolean val;

        AscendingValues(Boolean val) {
            this.val = val;
        }

        public static boolean isValidValue(String name) {
            for (AscendingValues val : values()) {
                if (val.name().equalsIgnoreCase(name))
                    return true;
            }
            return false;
        }

        public static String getAllValues() {
            StringJoiner values = new StringJoiner(",");
            for (AscendingValues sorting : values())
                values.add(sorting.name());
            return values.toString();
        }

        public static Boolean getValue(String asc) {
            for (AscendingValues val : values()) {
                if (val.name().equalsIgnoreCase(asc))
                    return val.val;
            }
            return null;
        }
    }

    public int getIndexOfFirstElement() {
        return (page - 1) * limit;
    }

    public static Pagination getDownloadPagination() {
        return new Pagination(1, Integer.MAX_VALUE, null, null);
    }
}
