package com.isaacpc.mariskalrock.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.androidquery.util.XmlDom;
import com.isaacpc.mariskalrock.bd.NoticiaEntity;
import com.isaacpc.mariskalrock.log.Log;
import com.isaacpc.mariskalrock.utils.StringUtils;

public class NoticiasParser {

        private static final String LOG_TAG = "NoticiasParser";

        public NoticiasParser() {
                super();
        }

        /**
         * 
         * @param dom
         * @return
         */
        public List<NoticiaEntity> parseDom(XmlDom dom) {

                Log.i(LOG_TAG, "Se parsean los datos del XML obtenido");

                final List<NoticiaEntity> elements = new ArrayList<NoticiaEntity>();

                if (dom == null) {
                        Log.w(LOG_TAG, "XmlDom es NULL en parseDom");
                } else {
                        final List<XmlDom> entries = dom.child("channel").tags("item");

                        NoticiaEntity item = null;

                        for (final XmlDom entry : entries) {
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
        private NoticiaEntity parsetItem(XmlDom entry) {

                final NoticiaEntity item = new NoticiaEntity();

                item.setTitle(StringUtils.replaceSpecialCharacters(entry.text("title")).toUpperCase(Locale.getDefault()));
                item.setDescription(StringUtils.replaceSpecialCharacters(entry.text("description")));
                item.setLink(entry.text("link"));
                item.setDateString(entry.text("pubDate"));
                final XmlDom imagenXML = entry.tag("content:encoded");

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

                        final Pattern p = Pattern.compile("<img[^>]*src=[\"']([^\"^']*)", Pattern.CASE_INSENSITIVE);
                        final Matcher m = p.matcher(imagenXML.text());

                        if (m.find()) {
                                result = m.group(1);
                        }
                }
                return result;
        }
}