package com.mikhail.tarasevich.university.service.impl;

public abstract class AbstractPageableService {

    public static final int ITEMS_PER_PAGE = 10;

    protected int parsePageNumber(String page, long itemsCount, int defaultValue) {
        int pageNumber = defaultValue - 1;

        try {
            pageNumber = Integer.parseInt(page) - 1;
        } catch (NumberFormatException e) {
            return pageNumber;
        }

        if (pageNumber < 0) return defaultValue;

        int pageCount = (int) Math.ceil(((double) itemsCount / (double) ITEMS_PER_PAGE)) - 1;

        return Math.min(pageNumber, pageCount);
    }

}
