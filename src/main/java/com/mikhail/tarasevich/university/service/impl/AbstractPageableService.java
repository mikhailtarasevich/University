package com.mikhail.tarasevich.university.service.impl;

public abstract class AbstractPageableService {

    protected static final int ITEMS_PER_PAGE = 10;

    protected int parsePageNumber(String page, long itemsCount, int defaultValue) {
        int pageNumber = defaultValue;

        try {
            pageNumber = Integer.parseInt(page);
        } catch (NumberFormatException e) {
            return pageNumber;
        }

        if (pageNumber <= 0) return defaultValue;

        int pageCount = (int) Math.ceil(((double) itemsCount / (double) ITEMS_PER_PAGE));

        return Math.min(pageNumber, pageCount);
    }

}
