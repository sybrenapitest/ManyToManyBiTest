package org.shboland.domain.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(value = Include.NON_NULL)
public class JsonSearchResult<T> {

    @JsonProperty("grandTotal")
    private Integer grandTotalNumberOfResults;

    @JsonProperty("numberOfResults")
    private Integer numberOfResults;

    @JsonProperty("results")
    private List<T> results;

}
