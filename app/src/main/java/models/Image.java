package models;

import android.graphics.Bitmap;

import com.orm.SugarRecord;

/**
 * Created by vinnik on 06.12.2016.
 */
public class Image extends SugarRecord {
    private Long id;
    public Bitmap image;
    public Boolean isMain;
    public Person owner;

    public Image(){}
    public Image(Bitmap image){}

//    public void setId(Long id)
//    {
//        this.id=id;
//    }
//    public void setImage(Bitmap img)
//    {
//        this.image=img;
//    }
//    public void setIsMain(Boolean isMain)
//    {
//        this.isMain=isMain;
//    }
//    public void setOwner(Person person)
//    {
//        this.owner=person;
//    }
//    public Long getId()
//    {
//        return this.id;
//    }
//    public Bitmap getImage()
//    {
//        return this.image;
//    }
//    public Boolean getIsMain()
//    {
//        return this.isMain;
//    }
//    public Person getOwner()
//    {
//        return this.owner;
//    }

}
