package org.shboland.domain.entities;

import javax.ws.rs.QueryParam;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class JsonShopSearchCriteria {

    private static final int DEFAULT_MAX_RESULTS = 10;

    @QueryParam("maxResults")
    private int maxResults = DEFAULT_MAX_RESULTS;

    @QueryParam("start")
    private int start;

    @QueryParam("id")
    private Long id;
    
    @QueryParam("number")
    private Long number;
    
    // @Input

}