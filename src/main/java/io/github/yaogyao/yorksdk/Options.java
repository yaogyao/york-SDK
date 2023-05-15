package io.github.yaogyao.yorksdk;

import java.util.HashMap;
import java.util.Map;

/**
 *  Options contains pagination, sort and filtering options.
 *  It has an internal Builder class to build the option in fluent style
 */
public class Options {
    private Map<String, Object> queryMap = new HashMap<>();
    private String filter="";

    private Options (Map<String, Object> queryMap, String filter) {
        this.queryMap = queryMap;
        this.filter = filter;
    }

    public Map<String, Object> getQueryMap() {
        return queryMap;
    }

    public String getFilter() {
        return filter;
    }


    public static class Builder {
        private Map<String, Object> queryMap = new HashMap<>();
        private String filter="";

        public Builder limit(int limit) {
            if (limit < 0) throw new IllegalArgumentException(Constants.INVALID_LIMIT);
            queryMap.put("limit", limit);
            return this;
        }

        public Builder page(int page) {
            if (page <= 0) throw new IllegalArgumentException(Constants.INVALID_PAGE);
            queryMap.put("page", page);
            return this;
        }

        public Builder offset(int offset) {
            if (offset < 0) throw new IllegalArgumentException(Constants.INVALID_OFFSET);
            queryMap.put("offset", offset);
            return this;
        }

        public Builder sort(String sortName) {
            return sort(sortName, SortOrder.ASC);
        }

        public Builder sort(String sortName, SortOrder order) {
            if (sortName == null || sortName.isEmpty()) {
                throw new IllegalArgumentException(Constants.INVALID_SORT_NAME);
            }
            if (order == null) order = SortOrder.ASC;
            queryMap.put("sort", sortName + ":" + (order.equals(SortOrder.ASC) ? "asc" : "desc"));
            return this;
        }

        public Builder filter(String pattern) {
            this.filter = pattern;
            return this;
        }

        public Options build(){
           return new Options(this.queryMap, this.filter);
        }
    }
}
