package se233.chapter1.model.item;

import javafx.scene.input.DataFormat;

import java.io.Serial;
import java.io.Serializable;

public class BasedEquipment implements Serializable {
    public static final DataFormat DATA_FORMAT = new DataFormat(
            "src.main.java. se233.chapter1.model.item.BasedEquipment");
    protected String name ;
    protected String impath ;
    public String getName() { return name ; }
    public String getImagepath() { return impath ; }
    public void setImagepath(String imagepath){ this.impath = imagepath ; }
}
