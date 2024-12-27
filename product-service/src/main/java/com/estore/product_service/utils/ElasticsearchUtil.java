package com.estore.product_service.utils;

import java.util.function.Supplier;

import co.elastic.clients.elasticsearch._types.query_dsl.FuzzyQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;

public class ElasticsearchUtil {

    /*
     * supplier is a functional interface
     * the reason to use supplier is sometimes query objects can be expensive to
     * create hence
     * using supplier you can create object at the time of requirement rather than
     * storing it in the memory.
     */
    public static Supplier<Query> MatchAllQuerySupplier() {
        Supplier<Query> supplier = () -> Query.of(q -> q.matchAll(matchAllQuery()));
        return supplier;
    }

    public static Supplier<Query> MatchProdByNameSupplier(String partialProdName) {
        Supplier<Query> supplier = () -> Query.of(q -> q.fuzzy(matchProductByNameQuery(partialProdName)));
        return supplier;
    }

    /*
     * this method is creating a new match all query
     */
    private static MatchAllQuery matchAllQuery() {
        MatchAllQuery.Builder matchAllQuery = new MatchAllQuery.Builder();
        return matchAllQuery.build();
    }

    public static FuzzyQuery matchProductByNameQuery(String name) {
        FuzzyQuery.Builder fuzzyQuery = new FuzzyQuery.Builder();
        return fuzzyQuery.field("productName").value(name).fuzziness("auto").build();
    }
}
