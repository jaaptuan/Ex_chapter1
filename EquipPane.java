package se233.chapter1.view;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import se233.chapter1.Launcher;
import se233.chapter1.controller.AllCustomHandler;
import se233.chapter1.model.character.BasedCharacter;
import se233.chapter1.model.item.*;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;

import java.util.ArrayList;

import static se233.chapter1.controller.AllCustomHandler.onDragDropped;
import static se233.chapter1.controller.AllCustomHandler.onDragOver;

public class EquipPane extends ScrollPane {
    private Weapon equippedWeapon;
    private Armor equippedArmor;
    Button unequips = new Button("Unequip");
    public EquipPane(){}
    private Pane getDetailsPane(){
        Pane equipmentInfoPane = new VBox(10);
        equipmentInfoPane.setBorder(null);
        ((VBox) equipmentInfoPane).setAlignment(Pos.CENTER);
        equipmentInfoPane.setPadding(new Insets(25,25,25,25));
        Label weaponLbl, armorLbl;
        StackPane weaponImgGroup = new StackPane();
        StackPane armorImgGroup = new StackPane();
        ImageView bg1 = new ImageView();
        ImageView bg2 = new ImageView();
        ImageView weaponImg = new ImageView();
        ImageView armorImg = new ImageView();
        bg1.setImage(new Image(Launcher.class.getResource("assets/blank.png").toString()));
        bg2.setImage(new Image(Launcher.class.getResource("assets/blank.png").toString()));
        weaponImgGroup.getChildren().add(bg1);
        armorImgGroup.getChildren().add(bg2);



        if (equippedWeapon != null){
            weaponLbl = new Label("Weapon:\n"+equippedWeapon.getName());
            weaponImg.setImage(new Image(Launcher.class.getResource(equippedWeapon.getImagepath()).toString()));
            weaponImgGroup.getChildren().add(weaponImg);
        }else{
            weaponLbl = new Label("Weapon:");
            weaponImg.setImage(new Image(Launcher.class.getResource("assets/blank.png").toString()));
        }
        if(equippedArmor != null){
            armorLbl = new Label("Armor: \n" + equippedArmor.getName());
            armorImg.setImage(new Image(Launcher.class.getResource(equippedArmor.getImagepath()).toString()));
            armorImgGroup.getChildren().add(armorImg);
        }else{
            armorLbl = new Label("Armor: ");
            armorImg.setImage(new Image(Launcher.class.getResource("assets/blank.png").toString()));
        }
        weaponImgGroup.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent e) {onDragOver(e, "Weapon");}
        });
        armorImgGroup.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent e) {onDragOver(e, "Armor");}
        });
        weaponImgGroup.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent e) {onDragDropped(e,weaponLbl,weaponImgGroup);}
        });
        armorImgGroup.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent e) {onDragDropped(e,armorLbl,armorImgGroup);}
        });
        equipmentInfoPane.getChildren().addAll(weaponLbl, weaponImgGroup, armorLbl,armorImgGroup);

        equipmentInfoPane.getChildren().add(unequips);
        unequips.setOnAction(e -> {
            BasedCharacter character = Launcher.getMainCharacter();
            ArrayList<BasedEquipment> allEquipments = Launcher.getAllEquipments();

            if (equippedWeapon != null) {
                allEquipments.add(equippedWeapon);
                equippedWeapon = null;
            }
            if (equippedArmor != null) {
                allEquipments.add(equippedArmor);
                equippedArmor = null;
            }

            character.equipWeapon(null);
            character.equipArmor(null);
            Launcher.setEquippedWeapon(null);
            Launcher.setEquippedArmor(null);

            Launcher.setAllEquipments(allEquipments);
            Launcher.setMainCharacter(character);

            Launcher.refreshPane();
        });
        return equipmentInfoPane;
    }
    public void drawPane(Weapon equippedWeapon, Armor equippedArmor){
        this.equippedWeapon = equippedWeapon;
        this.equippedArmor = equippedArmor;
        unequips.setOnAction(e -> {
            BasedCharacter character = Launcher.getMainCharacter();
            ArrayList<BasedEquipment> allEquipments = Launcher.getAllEquipments();

            if (Launcher.getEquippedWeapon() != null) {
                allEquipments.add(Launcher.getEquippedWeapon());
                character.equipWeapon(null);
                Launcher.setEquippedWeapon(null);
                this.equippedWeapon = null;
            }
            if (Launcher.getEquippedArmor() != null) {
                allEquipments.add(Launcher.getEquippedArmor());
                character.equipArmor(null);
                Launcher.setEquippedArmor(null);
                this.equippedArmor = null;
            }

            Launcher.setAllEquipments(allEquipments);
            Launcher.setMainCharacter(character);
            Launcher.refreshPane();
        });
        Pane equipmentInfo = getDetailsPane();
        this.setStyle("-fx-background-color:Red;");
        this.setContent(equipmentInfo);
    }
}