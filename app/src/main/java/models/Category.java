package models;

/**
 * Created by vinnik on 06.12.2016.
 */

public class Category{
    private Long id;
    String category;

    public Category(){}
    public Category(String category){
        this.category = category;
    }

    public void setId(Long id)
    {
        this.id=id;
    }
    public void setCategory(String category)
    {
        this.category=category;
    }
    public Long getId()
    {
        return this.id;
    }
    public String getCategory()
    {
        return this.category;
    }
}
