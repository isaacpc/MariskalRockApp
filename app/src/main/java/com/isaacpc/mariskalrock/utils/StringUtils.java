package com.isaacpc.mariskalrock.utils;

import java.util.Locale;

public class StringUtils {

        /**
         * 
         * @param string
         * @return
         */
        public static Boolean isEmpty(String string) {

                return (string == null || string.trim().length() == 0);
        }

        /**
         * 
         * @param string
         * @param maxlength
         * @param uppercase
         * @return
         */
        public static String truncString(String string, int maxlength, Boolean uppercase) {

                String result = string;

                if (!isEmpty(string)) {

                        if (string.length() > maxlength) {
                                // no corta las palabras a medias
                                final int posicion = string.indexOf(" ", maxlength);

                                result = string.substring(0, posicion);
                                result += "...";
                        }

                        if (uppercase) {
                                result = result.toUpperCase(Locale.getDefault());
                        }
                }
                return result;
        }

        /**
         * 
         * @param string
         * @return
         */
        public static String replaceSpecialCharacters(String string) {

                String result = string;

                if (result != null && result.length() > 0) {

                        result = result.replaceAll("&#8211;", "-");
                        result = result.replaceAll("&#8216;", "'");
                        result = result.replaceAll("&#8217;", "'");
                        result = result.replaceAll("&#8220;", "\"");
                        result = result.replaceAll("&#8221;", "\"");
                        result = result.replaceAll("&#8222;", "\"");
                        result = result.replaceAll("&#38;", "&");
                        result = result.replaceAll("&#8243;", "\"");
                        result = result.replaceAll("&#8230;", "...");
                        result = result.replaceAll("&#8242;", "'");
                        result = result.replaceAll("&#160;", " ");
                }

                return result;
        }

}
