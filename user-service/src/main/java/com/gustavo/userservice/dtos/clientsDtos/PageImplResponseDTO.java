package com.gustavo.userservice.dtos.clientsDtos;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class PageImplResponseDTO<T> extends PageImpl<T> {
	private static final long serialVersionUID = 1L;
	
	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PageImplResponseDTO(
            @JsonProperty("content") List<T> content,
            @JsonProperty("number") int number,
            @JsonProperty("size") int size,
            @JsonProperty("totalElements") Long totalElements,
            @JsonProperty("pageable") JsonNode pageable,
            @JsonProperty("last") boolean last,
            @JsonProperty("totalPages") int totalPages,
            @JsonProperty("sort") JsonNode sort,
            @JsonProperty("first") boolean first,
            @JsonProperty("numberOfElements") int numberOfElements,
            @JsonProperty("empty") boolean empty
    ) {
        super(content, PageRequest.of(number, size), totalElements);
    }
	
	public PageImplResponseDTO(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }
	
	public PageImplResponseDTO(List<T> content) {
		super(content);
	}

}
