package edu.cmu.adroitness.client.services.nell.model;

/**
 *
 * @author lftomazini
 */
public class NellCategory {
    private String category;
    private String entity;

    /**
     * Creates a NellCategory object consisting of an entity and a category
     *
     * @param category the category that the entity belongs to
     * @param entity the term
     */
    public NellCategory(String category, String entity) {
        this.category = category;
        this.entity = entity;
    }

    public String getCategory() {
        return category;
    }

    public String getEntity() {
        return entity;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return this.getCategory() + ":" + this.getEntity();
    }
}
