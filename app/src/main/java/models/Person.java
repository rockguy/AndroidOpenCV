package models;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by vinnik on 06.12.2016.
 */

public class Person extends SugarRecord {
    private Long id;
    public String firstName;
    public String lastName;
    public String secondName;
    public String fio = lastName + " " + firstName + " " + secondName + " ";
    public Category category;
    public List<Image> imageList;

    public Person() {
    }

//    public void setId(Long id)
//    {
//        this.id=id;
//    }
//    public void setFirstName(String firstName)
//    {
//        this.firstName=firstName;
//    }
//    public void setLastName(String lastName)
//    {
//        this.lastName=lastName;
//    }
//    public void setSecondName(String secondName)
//    {
//        this.secondName=secondName;
//    }
//    public void setCategory(String category)
//    {
//        this.category=new Category(category);
//    }
//    public void setImages(Image image)
//    {
//        this.imageList.add(image);
//    }
//    public Long getId()
//    {
//        return this.id;
//    }
//    public String getFirstName()
//    {
//        return this.firstName;
//    }
//    public String getLastName()
//    {
//        return this.lastName;
//    }
//    public String getSecondName()
//    {
//        return this.secondName;
//    }
//    public Category getCategory()
//    {
//        return this.category;
//    }
//    public String getFIO()
//    {
//        return this.lastName+" "+this.firstName+" "+this.secondName;
//    }


}
