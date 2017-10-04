package com.example.leapfrog.movielistingmvp.data.local;


public class DatabaseConfig {

    public static final String DB_NAME = "movie_db";

    public static final class MovieColumns {

        public static final String TABLENAME = "movie";
        public static final String ID = TABLENAME + "_id";

    }


    public static final class CategoryColumns {

        public static final String TABLENAME = "category";
        public static final String ID = TABLENAME + "_id";
        public static final String CATNAME = TABLENAME + "_cat_name";

    }


    public static final class MovieCategoryColumns {
        public static final String TABLENAME = "movie_category";
        public static final String ID = TABLENAME + "_id";
        public static final String MOVIE_ID = TABLENAME + "_movie_id";
        public static final String CAT_ID = TABLENAME + "_cat_id";

    }

}
