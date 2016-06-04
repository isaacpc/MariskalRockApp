package com.isaacpc.mariskalrock.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

import com.androidquery.util.XmlDom;
import com.isaacpc.mariskalrock.bd.PodcastEntity;
import com.isaacpc.mariskalrock.utils.StringUtils;

public class PodcastsParser {

    private static final String LOG_TAG = "RSSParser";

    public PodcastsParser() {
	super();
    }

    /**
     * 
     * @param dom
     * @return
     */
    public List<PodcastEntity> parseDom(XmlDom dom) {

	List<PodcastEntity> elements = new ArrayList<PodcastEntity>();

	if (dom == null) {
	    Log.w(LOG_TAG, "XmlDom es NULL en parseDom");
	} else {
	    List<XmlDom> entries = dom.child("channel").tags("item");

	    PodcastEntity item = null;

	    for (XmlDom entry : entries) {
		item = parsetItem(entry);
		elements.add(item);
	    }
	}
	return elements;
    }

    /**
     * 
     * @param entry
     * @return
     */
    private PodcastEntity parsetItem(XmlDom entry) {

	PodcastEntity item = new PodcastEntity();

	item.setTitle(StringUtils.replaceSpecialCharacters(entry.text("title")).toUpperCase(Locale.getDefault()));
	item.setDescription(StringUtils.replaceSpecialCharacters(entry.text("description")));
	item.setLink(entry.text("link"));
	item.setDateString(entry.text("pubDate"));
	XmlDom imagenXML = entry.tag("content:encoded");

	String imageURL = null;
	imageURL = getImageURLFromDom(imagenXML);
	item.setImageURL(imageURL);

	return item;
    }

    /**
     * 
     * @param imagenXML
     * @return
     */
    private String getImageURLFromDom(XmlDom imagenXML) {

	String result = null;
	if (imagenXML != null) {

	    Pattern p = Pattern.compile("<img[^>]*src=[\"']([^\"^']*)", Pattern.CASE_INSENSITIVE);
	    Matcher m = p.matcher(imagenXML.text());

	    if (m.find()) {
		result = m.group(1);
	    }
	}
	return result;
    }
}