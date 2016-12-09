package models;

import android.graphics.Bitmap;

/**
 * Created by vinnik on 06.12.2016.
 */
public class Image{
     int id;
     byte[] image;
     int isMain;
     String owner;

    public Image(){}
    public Image(Bitmap image){}

    public void setId(int id)
    {
        this.id=id;
    }
    public void setImage(byte[] img)
    {
        this.image=img;
    }
    public void setIsMain(int isMain)
    {
        this.isMain=isMain;
    }
    public void setOwner(String person)
    {
        this.owner=person;
    }
    public int getId()
    {
        return this.id;
    }
    public byte[] getImage()
    {
        return this.image;
    }
    public int getIsMain()
    {
        return this.isMain;
    }
    public String getOwner()
    {
        return this.owner;
    }

}
