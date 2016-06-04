package com.isaacpc.mariskalrock.bd;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.isaacpc.mariskalrock.bd.DaoMaster.DevOpenHelper;
import com.isaacpc.mariskalrock.log.Log;

import de.greenrobot.dao.DaoException;

public class DatabaseManager {

        private static final String LOG_TAG = "DatabaseManager";

        private final SQLiteDatabase db;

        private final DaoMaster daoMaster;
        private final DaoSession daoSession;
        private final NoticiaEntityDao noticiaDao;
        private final PodcastEntityDao podcastDao;
        private final VideoEntityDao videoDao;

        /**
         * Obtiene una lista de itemsRSS que hay en la BBDD
         * 
         * @return
         */
        public DatabaseManager(Context context) {
                Log.i(LOG_TAG, "Se crea DatabaseManager");

                final DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "mariskalrock-db", null);
                db = helper.getWritableDatabase();
                daoMaster = new DaoMaster(db);
                daoSession = daoMaster.newSession();

                noticiaDao = daoSession.getNoticiaEntityDao();
                podcastDao = daoSession.getPodcastEntityDao();
                videoDao = daoSession.getVideoEntityDao();
        }

        /**
         * 
         * @return
         */
        public List<NoticiaEntity> getNoticiasEntityList() {

                final List<NoticiaEntity> list = new ArrayList<NoticiaEntity>();

                list.addAll(noticiaDao.queryBuilder().orderDesc(com.isaacpc.mariskalrock.bd.NoticiaEntityDao.Properties.Date).list());

                return list;
        }

        /**
         * Obtiene la última noticia
         * 
         * @return
         */
        public NoticiaEntity getNoticiaMasNueva() {

                NoticiaEntity entity = null;

                try {

                        entity = noticiaDao.queryBuilder().limit(1).orderDesc(com.isaacpc.mariskalrock.bd.NoticiaEntityDao.Properties.Date).uniqueOrThrow();

                } catch (final DaoException e) {
                        Log.w(LOG_TAG, "No hay noticia mas nueva");
                        entity = null;
                }
                return entity;
        }

        /**
         * Obtiene la última noticia
         * 
         * @return
         */
        public NoticiaEntity getNoticiaAnterior(NoticiaEntity noticia) {

                NoticiaEntity entity = null;

                try {
                        entity = noticiaDao.queryBuilder().limit(1).orderDesc(com.isaacpc.mariskalrock.bd.NoticiaEntityDao.Properties.Date)
                                        .where(com.isaacpc.mariskalrock.bd.NoticiaEntityDao.Properties.Date.lt(noticia.getDate())).uniqueOrThrow();
                } catch (final DaoException e) {
                        Log.w(LOG_TAG, "No hay noticia anterior");
                        entity = null;
                }

                return entity;
        }

        /**
         * Obtiene la última noticia
         * 
         * @return
         */
        public NoticiaEntity getNoticiaPosterior(NoticiaEntity noticia) {

                NoticiaEntity entity = null;

                try {

                        if (noticia != null) {
                                entity = noticiaDao.queryBuilder().limit(1).orderAsc(com.isaacpc.mariskalrock.bd.NoticiaEntityDao.Properties.Date)
                                                .where(com.isaacpc.mariskalrock.bd.NoticiaEntityDao.Properties.Date.gt(noticia.getDate())).uniqueOrThrow();
                        }
                } catch (final DaoException e) {
                        Log.w(LOG_TAG, "No hay noticia posterior");
                        entity = null;
                }

                return entity;
        }

        /**
         * Obtiene la última noticia
         * 
         * @return
         */
        public NoticiaEntity getNoticiaActual() {

                NoticiaEntity entity = null;

                try {
                        entity = noticiaDao.queryBuilder().limit(1).where(com.isaacpc.mariskalrock.bd.NoticiaEntityDao.Properties.Widget.eq(true)).uniqueOrThrow();
                } catch (final DaoException e) {
                        Log.w(LOG_TAG, "No hay noticia actual");
                        entity = null;
                }

                return entity;
        }

        /**
         * 
         * @param noticia
         * @param noticia
         */
        public void updateNoticiaActual(final NoticiaEntity anterior, final NoticiaEntity actual) {

                if (anterior != null && actual != null) {

                        final Thread t = new Thread("Thread-ActualizaNoticia") {
                                @Override
                                public void run() {
                                        anterior.setWidget(false);
                                        actual.setWidget(true);

                                        noticiaDao.update(anterior);
                                        noticiaDao.update(actual);
                                }
                        };
                        t.start();
                }
        }

        /**
         * 
         * @param itemList
         * @param borrar
         */
        public void saveNoticiasEntityList(final List<NoticiaEntity> itemList, final boolean borrar) {

                Log.i(LOG_TAG, "Se van a guardar las noticias en BBDD");
                if (borrar) {
                        noticiaDao.deleteAll();
                        Log.d(LOG_TAG, "Se borra la tabla de NoticiaEntity.");
                }

                final int size = itemList.size();
                for (int i = 0; i < size; i++) {
                        final NoticiaEntity item = itemList.get(i);

                        if (i == 0) {
                                item.setWidget(true);
                        } else {
                                item.setWidget(false);
                        }

                        noticiaDao.insert(item);
                }
                Log.i(LOG_TAG, "Se han guardado en BBDD " + itemList.size() + " noticias.");
        }

        /**
         * 
         * @return
         */
        public List<PodcastEntity> getPodcastsEntityList() {

                final List<PodcastEntity> list = new ArrayList<PodcastEntity>();

                list.addAll(podcastDao.queryBuilder().orderDesc(com.isaacpc.mariskalrock.bd.PodcastEntityDao.Properties.Date).list());
                return list;
        }

        /**
         * Guarda en BBDD una lista de items en un hilo a parte
         * 
         * @param itemList
         */
        public void savePodcastsEntityList(final List<PodcastEntity> itemList, final boolean borrar) {

                if (borrar) {
                        podcastDao.deleteAll();
                        Log.d(LOG_TAG, "Se borra la tabla de PodcastEntity.");
                }

                for (final PodcastEntity item : itemList) {
                        podcastDao.insert(item);
                }
                Log.i(LOG_TAG, "Se han guardado en BBDD " + itemList.size() + " podcasts.");
        }

        /**
         * 
         * @return
         */
        public List<VideoEntity> getVideosEntityList() {

                final List<VideoEntity> list = new ArrayList<VideoEntity>();
                list.addAll(videoDao.queryBuilder().orderDesc(com.isaacpc.mariskalrock.bd.VideoEntityDao.Properties.Published).list());
                return list;
        }

        /**
         * Guarda en BBDD una lista de items en un hilo a parte
         * 
         * @param itemList
         */
        public void saveVideosEntityList(final List<VideoEntity> itemList, final boolean borrar) {

                if (borrar) {
                        videoDao.deleteAll();
                        Log.d(LOG_TAG, "Se borra la tabla de VideoEntity.");
                }

                for (final VideoEntity item : itemList) {
                        videoDao.insert(item);

                }
                Log.i(LOG_TAG, "Se han guardado en BBDD " + itemList.size() + " Videos.");
        }
}
