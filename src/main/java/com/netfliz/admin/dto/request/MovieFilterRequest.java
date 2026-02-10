package com.netfliz.admin.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class MovieFilterRequest extends BaseRequest {
    private List<String> genres = new ArrayList<>();
    private List<String> countries = new ArrayList<>();
    private List<String> languages = new ArrayList<>();
    private String year;
    private String rating;
    private String rated;
    private String type;
    private String sort;
    private String title;
}
