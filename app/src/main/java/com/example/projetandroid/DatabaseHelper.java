package com.example.projetandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "projetandroid.db";
    private static final int DATABASE_VERSION = 6;

    // Table Names
    public static final String TABLE_JD = "jd";
    public static final String TABLE_SOCIETE = "societe";
    public static final String TABLE_OFFRES = "offres";
    public static final String TABLE_CANDIDATURES = "candidatures";

    // Common column names
    public static final String KEY_ID = "id";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_DOMAINE = "domaine";

    // JD table columns
    public static final String KEY_JD_USERNAME = "username";
    public static final String KEY_JD_CIN = "cin";
    public static final String KEY_JD_TYPE_DIPLOME = "type_diplome";

    // Societe table columns
    public static final String KEY_SOCIETE_NAME = "nom_societe";
    public static final String KEY_SOCIETE_ADRESSE = "adresse";

    // Offres table columns
    public static final String KEY_OFFRE_METIER = "type_metiers";
    public static final String KEY_OFFRE_FORMATION = "formation_demande";
    public static final String KEY_OFFRE_SALAIRE = "offre_salaire";
    public static final String KEY_OFFRE_COMPETENCES = "competences";
    public static final String KEY_OFFRE_DESCRIPTION = "description";
    public static final String KEY_OFFRE_SOCIETE_ID_FK = "societe_id";
    
    // Candidatures table columns
    public static final String KEY_CANDIDATURE_JD_ID_FK = "jd_id";
    public static final String KEY_CANDIDATURE_SOCIETE_ID_FK = "societe_id";
    public static final String KEY_CANDIDATURE_OFFRE_NAME = "offre_name";
    public static final String KEY_CANDIDATURE_JD_NAME = "jd_name";
    public static final String KEY_CANDIDATURE_JD_PHONE = "jd_phone";

    public enum UserType { JD, SOCIETE }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_JD = "CREATE TABLE " + TABLE_JD + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_EMAIL + " TEXT UNIQUE NOT NULL,"
                + KEY_PASSWORD + " TEXT NOT NULL,"
                + KEY_JD_USERNAME + " TEXT,"
                + KEY_JD_CIN + " TEXT,"
                + KEY_PHONE + " TEXT,"
                + KEY_DOMAINE + " TEXT,"
                + KEY_JD_TYPE_DIPLOME + " TEXT)";

        String CREATE_TABLE_SOCIETE = "CREATE TABLE " + TABLE_SOCIETE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_EMAIL + " TEXT UNIQUE NOT NULL,"
                + KEY_PASSWORD + " TEXT NOT NULL,"
                + KEY_SOCIETE_NAME + " TEXT,"
                + KEY_SOCIETE_ADRESSE + " TEXT,"
                + KEY_PHONE + " TEXT,"
                + KEY_DOMAINE + " TEXT)";

        String CREATE_TABLE_OFFRES = "CREATE TABLE " + TABLE_OFFRES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_OFFRE_METIER + " TEXT,"
                + KEY_OFFRE_FORMATION + " TEXT,"
                + KEY_OFFRE_SALAIRE + " INTEGER,"
                + KEY_OFFRE_COMPETENCES + " TEXT,"
                + KEY_OFFRE_DESCRIPTION + " TEXT,"
                + KEY_OFFRE_SOCIETE_ID_FK + " INTEGER NOT NULL, "
                + "FOREIGN KEY(" + KEY_OFFRE_SOCIETE_ID_FK + ") REFERENCES " + TABLE_SOCIETE + "(" + KEY_ID + "));";
                
        String CREATE_TABLE_CANDIDATURES = "CREATE TABLE " + TABLE_CANDIDATURES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_CANDIDATURE_JD_ID_FK + " INTEGER NOT NULL,"
                + KEY_CANDIDATURE_SOCIETE_ID_FK + " INTEGER NOT NULL,"
                + KEY_CANDIDATURE_OFFRE_NAME + " TEXT NOT NULL,"
                + KEY_CANDIDATURE_JD_NAME + " TEXT NOT NULL,"
                + KEY_CANDIDATURE_JD_PHONE + " TEXT NOT NULL,"
                + "FOREIGN KEY(" + KEY_CANDIDATURE_JD_ID_FK + ") REFERENCES " + TABLE_JD + "(" + KEY_ID + "),"
                + "FOREIGN KEY(" + KEY_CANDIDATURE_SOCIETE_ID_FK + ") REFERENCES " + TABLE_SOCIETE + "(" + KEY_ID + "));";

        db.execSQL(CREATE_TABLE_JD);
        db.execSQL(CREATE_TABLE_SOCIETE);
        db.execSQL(CREATE_TABLE_OFFRES);
        db.execSQL(CREATE_TABLE_CANDIDATURES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This method is now intentionally left empty to prevent data loss during debug.
    }

    public boolean addJd(String email, String password, String username, String cin, String phone, String domaine, String typeDiplome) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, email);
        values.put(KEY_PASSWORD, password);
        values.put(KEY_JD_USERNAME, username);
        values.put(KEY_JD_CIN, cin);
        values.put(KEY_PHONE, phone);
        values.put(KEY_DOMAINE, domaine);
        values.put(KEY_JD_TYPE_DIPLOME, typeDiplome);
        return db.insert(TABLE_JD, null, values) != -1;
    }

    public boolean addSociete(String email, String password, String nomSociete, String adresse, String phone, String domaine) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EMAIL, email);
        values.put(KEY_PASSWORD, password);
        values.put(KEY_SOCIETE_NAME, nomSociete);
        values.put(KEY_SOCIETE_ADRESSE, adresse);
        values.put(KEY_PHONE, phone);
        values.put(KEY_DOMAINE, domaine);
        return db.insert(TABLE_SOCIETE, null, values) != -1;
    }

    public Pair<UserType, Integer> checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {KEY_ID};
        String selection = KEY_EMAIL + "=? AND " + KEY_PASSWORD + "=?";
        String[] selectionArgs = {email, password};

        try (Cursor jdCursor = db.query(TABLE_JD, columns, selection, selectionArgs, null, null, null)) {
            if (jdCursor != null && jdCursor.moveToFirst()) {
                int userId = jdCursor.getInt(jdCursor.getColumnIndexOrThrow(KEY_ID));
                return new Pair<>(UserType.JD, userId);
            }
        }

        try (Cursor societeCursor = db.query(TABLE_SOCIETE, columns, selection, selectionArgs, null, null, null)) {
            if (societeCursor != null && societeCursor.moveToFirst()) {
                int userId = societeCursor.getInt(societeCursor.getColumnIndexOrThrow(KEY_ID));
                return new Pair<>(UserType.SOCIETE, userId);
            }
        }

        return null;
    }

    public boolean addOffre(String typeMetiers, String formationDemande, int offreSalaire, String competences, String description, int societeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_OFFRE_METIER, typeMetiers);
        values.put(KEY_OFFRE_FORMATION, formationDemande);
        values.put(KEY_OFFRE_SALAIRE, offreSalaire);
        values.put(KEY_OFFRE_COMPETENCES, competences);
        values.put(KEY_OFFRE_DESCRIPTION, description);
        values.put(KEY_OFFRE_SOCIETE_ID_FK, societeId);
        return db.insert(TABLE_OFFRES, null, values) != -1;
    }
    
    public boolean addCandidature(int jdId, int societeId, String offreName, String jdName, String jdPhone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CANDIDATURE_JD_ID_FK, jdId);
        values.put(KEY_CANDIDATURE_SOCIETE_ID_FK, societeId);
        values.put(KEY_CANDIDATURE_OFFRE_NAME, offreName);
        values.put(KEY_CANDIDATURE_JD_NAME, jdName);
        values.put(KEY_CANDIDATURE_JD_PHONE, jdPhone);
        return db.insert(TABLE_CANDIDATURES, null, values) != -1;
    }
    
    private Offre cursorToOffre(Cursor cursor) {
        return new Offre(
                cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_SOCIETE_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_OFFRE_METIER)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_OFFRE_FORMATION)),
                cursor.getInt(cursor.getColumnIndexOrThrow(KEY_OFFRE_SALAIRE)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_OFFRE_COMPETENCES)),
                cursor.getString(cursor.getColumnIndexOrThrow(KEY_OFFRE_DESCRIPTION))
        );
    }

    public List<Offre> getOffresBySociete(int societeId) {
        List<Offre> offres = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT o.*, s." + KEY_SOCIETE_NAME + " FROM " + TABLE_OFFRES + " o"
                + " INNER JOIN " + TABLE_SOCIETE + " s ON o." + KEY_OFFRE_SOCIETE_ID_FK + " = s." + KEY_ID
                + " WHERE o." + KEY_OFFRE_SOCIETE_ID_FK + " = " + societeId;

        try (Cursor cursor = db.rawQuery(selectQuery, null)) {
            if (cursor.moveToFirst()) {
                do {
                    offres.add(cursorToOffre(cursor));
                } while (cursor.moveToNext());
            }
        }
        return offres;
    }

    public List<Offre> getAllOffres() {
        List<Offre> offres = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT o.*, s." + KEY_SOCIETE_NAME + " FROM " + TABLE_OFFRES + " o"
                + " INNER JOIN " + TABLE_SOCIETE + " s ON o." + KEY_OFFRE_SOCIETE_ID_FK + " = s." + KEY_ID;

        try (Cursor cursor = db.rawQuery(selectQuery, null)) {
            if (cursor.moveToFirst()) {
                do {
                    offres.add(cursorToOffre(cursor));
                } while (cursor.moveToNext());
            }
        }
        return offres;
    }

    public Offre getOffreById(int offreId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT o.*, s." + KEY_SOCIETE_NAME + " FROM " + TABLE_OFFRES + " o"
                + " INNER JOIN " + TABLE_SOCIETE + " s ON o." + KEY_OFFRE_SOCIETE_ID_FK + " = s." + KEY_ID
                + " WHERE o." + KEY_ID + " = " + offreId;

        try (Cursor cursor = db.rawQuery(selectQuery, null)) {
            if (cursor.moveToFirst()) {
                return cursorToOffre(cursor);
            }
        }
        return null;
    }

    public Pair<String, String> getJdNameAndPhone(int jdId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {KEY_JD_USERNAME, KEY_PHONE};
        String selection = KEY_ID + "=?";
        String[] selectionArgs = {String.valueOf(jdId)};

        try (Cursor cursor = db.query(TABLE_JD, columns, selection, selectionArgs, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_JD_USERNAME));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(KEY_PHONE));
                return new Pair<>(name, phone);
            }
        }
        return null;
    }

    public int getSocieteIdForOffre(int offreId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {KEY_OFFRE_SOCIETE_ID_FK};
        String selection = KEY_ID + "=?";
        String[] selectionArgs = {String.valueOf(offreId)};

        try (Cursor cursor = db.query(TABLE_OFFRES, columns, selection, selectionArgs, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndexOrThrow(KEY_OFFRE_SOCIETE_ID_FK));
            }
        }
        return -1;
    }
    
    public List<Candidat> getCandidatsForSociete(int societeId) {
        List<Candidat> candidats = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        String selectQuery = "SELECT " + KEY_CANDIDATURE_JD_NAME + ", " + KEY_CANDIDATURE_JD_PHONE + ", " + KEY_CANDIDATURE_OFFRE_NAME
                + " FROM " + TABLE_CANDIDATURES
                + " WHERE " + KEY_CANDIDATURE_SOCIETE_ID_FK + " = " + societeId;

        try (Cursor cursor = db.rawQuery(selectQuery, null)) {
            if (cursor.moveToFirst()) {
                do {
                    candidats.add(new Candidat(
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_CANDIDATURE_JD_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_CANDIDATURE_JD_PHONE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_CANDIDATURE_OFFRE_NAME))
                    ));
                } while (cursor.moveToNext());
            }
        }
        return candidats;
    }
}
