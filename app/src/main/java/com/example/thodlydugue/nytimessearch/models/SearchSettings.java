package com.example.thodlydugue.nytimessearch.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Thodly on 7/30/2017.
 */

public class SearchSettings implements Serializable {

    SimpleDateFormat beginDate;


    public enum Sort {
        newest, oldest, none
    }
    Sort sortOrder;

    ArrayList<String> filters;

    public SearchSettings() {
        // apply default value to settings
        this.sortOrder = Sort.none;
        filters = new ArrayList<String>();
    }



    public Sort getSortOrder()
    {
        return sortOrder;
    }

    public SimpleDateFormat getBeginDate() {
        return beginDate;
    }

    public ArrayList<String> getFilters() {
        return filters;
    }

    public void setSortOrder(Sort sortOrder) {

        this.sortOrder = sortOrder;
    }

    public void setBeginDate(SimpleDateFormat beginDate) {
        this.beginDate = beginDate;
    }

    // Add a String to apply Filter
    public void addFilter(String filter) {

        filters.add(filter);
    }

    // generate a string formatted for NY times search filters using Lucene Syntax
    public String generateNewsDeskFiltersOR() {
        String luceneSyntax = "news_desk:(";

        for(String filter : filters) {
            luceneSyntax = luceneSyntax.concat("\""+filter+"\" ");
        }

        luceneSyntax = luceneSyntax.concat(")");

        return luceneSyntax;
    }

    public String formatBeginDate() {
        return beginDate.format(beginDate.getCalendar().getTime());
    }
}