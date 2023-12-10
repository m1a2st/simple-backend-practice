package com.m1a2st.simplebackendpractice;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * @Author m1a2st
 * @Date 2023/3/24
 * @Version v1.0
 */
/*
{
    "content": [
        {
            "createBy": null,
            "createDate": "2023-03-23T17:16:21.251+00:00",
            "lastModifiedDate": "2023-03-23T17:16:21.251+00:00",
            "loginTime": "2023-03-23T17:16:21.336+00:00"
        },
        {
            "createBy": null,
            "createDate": "2023-03-23T17:16:21.666+00:00",
            "lastModifiedDate": "2023-03-23T17:16:21.666+00:00",
            "loginTime": "2023-03-23T17:16:21.753+00:00"
        }
    ],
    "pageable": {
        "sort": {
            "empty": true,
            "unsorted": true,
            "sorted": false
        },
        "offset": 0,
        "pageNumber": 0,
        "pageSize": 10,
        "paged": true,
        "unpaged": false
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 9,
    "size": 10,
    "sort": {
        "empty": true,
        "unsorted": true,
        "sorted": false
    },
    "first": true,
    "number": 0,
    "numberOfElements": 9,
    "empty": false
}
 */
public class CustomPageImpl<T> extends PageImpl<T> {

    public JsonNode pageable;
    public boolean last;
    public int totalPages;
    public boolean first;
    public JsonNode sort;
    public int numberOfElements;
    public boolean empty;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public CustomPageImpl(@JsonProperty("content") List<T> content,
                          @JsonProperty("pageable") JsonNode pageable,
                          @JsonProperty("last") boolean last,
                          @JsonProperty("totalPages") int totalPages,
                          @JsonProperty("totalElements") Long totalElements,
                          @JsonProperty("size") int size,
                          @JsonProperty("number") int number,
                          @JsonProperty("first") boolean first,
                          @JsonProperty("sort") JsonNode sort,
                          @JsonProperty("numberOfElements") int numberOfElements,
                          @JsonProperty("empty") boolean empty) {
        super(content, PageRequest.of(number, size), totalElements);
        this.pageable = pageable;
        this.last = last;
        this.totalPages = totalPages;
        this.first = first;
        this.sort = sort;
        this.numberOfElements = numberOfElements;
        this.empty = empty;
    }

    public CustomPageImpl(List<T> content, PageRequest pageRequest, long total) {
        super(content, pageRequest, total);
    }

    public CustomPageImpl(List<T> content) {
        super(content);
    }
}
