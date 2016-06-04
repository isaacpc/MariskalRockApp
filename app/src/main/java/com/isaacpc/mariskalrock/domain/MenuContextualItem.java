package com.isaacpc.mariskalrock.domain;


public class MenuContextualItem {

    private String title;
    private String url;

    public MenuContextualItem() {
        super();
    }

    public MenuContextualItem(final String title1, final String url1) {
        super();
        title = title1;
        url = url1;
    }

    public final String getTitle() {
        return title;
    }

    public final void setTitle(final String title1) {
        title = title1;
    }

    public final String getUrl() {
        return url;
    }

    public final void setUrl(final String url1) {
        url = url1;
    }


}
