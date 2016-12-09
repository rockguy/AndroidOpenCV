package models;

import java.util.List;

/**
 * Created by vinnik on 06.12.2016.
 */

public class Person{
   int id;
   String firstName;
   String lastName;
   String secondName;
   String fio = lastName + " " + firstName + " " + secondName + " ";
   String category;
   List<Image> imageList;

    public Person() {
    }



    public void setId(int id)
    {
        this.id=id;
    }
    public void setFirstName(String firstName)
    {
        this.firstName=firstName;
    }
    public void setLastName(String lastName)
    {
        this.lastName=lastName;
    }
    public void setSecondName(String secondName)
    {
        this.secondName=secondName;
    }
    public void setCategory(String category)
    {
        this.category=category;
    }
    public void setImages(Image image)
    {
        this.imageList.add(image);
    }
    public int getId()
    {
        return this.id;
    }
    public String getFirstName()
    {
        return this.firstName;
    }
    public String getLastName()
    {
        return this.lastName;
    }
    public String getSecondName()
    {
        return this.secondName;
    }
    public String getCategory()
    {
        return this.category;
    }
    public String getFIO()
    {
        return this.lastName+" "+this.firstName+" "+this.secondName;
    }


}
