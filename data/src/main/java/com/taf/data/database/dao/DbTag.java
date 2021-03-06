package com.taf.data.database.dao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "DB_TAG".
 */
public class DbTag {

    private Long id;
    private String title;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public DbTag() {
    }

    public DbTag(Long id) {
        this.id = id;
    }

    public DbTag(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
