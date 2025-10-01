package se233.chapter1.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import se233.chapter1.Launcher;
import se233.chapter1.model.character.BasedCharacter;
import se233.chapter1.model.item.Armor;
import se233.chapter1.model.item.BasedEquipment;
import se233.chapter1.model.item.Weapon;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;

// Import Log4j2
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class AllCustomHandler {

    // ใช้ Log4j2 Logger
    private static final Logger logger = LogManager.getLogger(AllCustomHandler.class);

    public static class GenCharacterHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            Weapon equippedWeapon = Launcher.getEquippedWeapon();
            Armor equippedArmor = Launcher.getEquippedArmor();

            ArrayList<BasedEquipment> allEquipments = Launcher.getAllEquipments();

            if (equippedWeapon != null) {
                allEquipments.add(equippedWeapon);
                logger.info("Added weapon back to inventory during character generation: {}", equippedWeapon.getName());
            }

            if (equippedArmor != null) {
                allEquipments.add(equippedArmor);
                logger.info("Added armor back to inventory during character generation: {}", equippedArmor.getName());
            }

            BasedCharacter newCharacter = GenCharacter.setUpCharacter();

            newCharacter.equipWeapon(null);
            newCharacter.equipArmor(null);

            Launcher.setEquippedWeapon(null);
            Launcher.setEquippedArmor(null);

            Launcher.setAllEquipments(allEquipments);
            Launcher.setMainCharacter(newCharacter);

            logger.info("Generated new character: {}", newCharacter.getName());

            Launcher.refreshPane();
        }
    }

    public static class UnequipHandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event){
            BasedCharacter character = Launcher.getMainCharacter();
            ArrayList<BasedEquipment> allEquipments = Launcher.getAllEquipments();

            if (Launcher.getEquippedWeapon() != null) {
                logger.info("Unequipped weapon via button: {} from character: {}",
                        Launcher.getEquippedWeapon().getName(), character.getName());
                character.equipWeapon(null);
                Launcher.setEquippedWeapon(null);
            }

            if (Launcher.getEquippedArmor() != null) {
                logger.info("Unequipped armor via button: {} from character: {}",
                        Launcher.getEquippedArmor().getName(), character.getName());
                character.equipArmor(null);
                Launcher.setEquippedArmor(null);
            }

            Launcher.setAllEquipments(allEquipments);
            Launcher.setMainCharacter(character);
            Launcher.refreshPane();
        }
    }

    public static void onDragDetected(MouseEvent event, BasedEquipment equipment, ImageView imgView){
        Dragboard db = imgView.startDragAndDrop(TransferMode.ANY);
        db.setDragView(imgView.getImage());
        ClipboardContent content = new ClipboardContent();
        content.put(equipment.DATA_FORMAT, equipment);
        db.setContent(content);
        event.consume();

        logger.debug("Drag detected for equipment: {}", equipment.getName());

        imgView.setOnDragDone(dragEvent -> {
            dragEvent.consume();
        });
    }

    public static void onDragOver(DragEvent event, String type){
        Dragboard dragboard = event.getDragboard();
        BasedEquipment retrievedEquipment = (BasedEquipment)dragboard.getContent(BasedEquipment.DATA_FORMAT);
        if(dragboard.hasContent(BasedEquipment.DATA_FORMAT) && retrievedEquipment.getClass().getSimpleName().equals(type)) {
            event.acceptTransferModes(TransferMode.MOVE);
            logger.trace("Drag over accepted for type: {}", type);
        }
    }

    public static void onDragDropped(DragEvent event, Label lbl, StackPane imgGroup) {
        boolean dragCompleted = false;
        Dragboard dragboard = event.getDragboard();
        ArrayList<BasedEquipment> allEquipments = Launcher.getAllEquipments();

        if (dragboard.hasContent(BasedEquipment.DATA_FORMAT)) {
            BasedEquipment retrievedEquipment = (BasedEquipment) dragboard.getContent(BasedEquipment.DATA_FORMAT);
            BasedCharacter character = Launcher.getMainCharacter();

            boolean success = false;
            String equipmentType = retrievedEquipment.getClass().getSimpleName();
            String characterName = character.getName();

            // Log the attempt to equip
            logger.info("Attempting to equip {}: '{}' to character: '{}'",
                    equipmentType, retrievedEquipment.getName(), characterName);

            if (retrievedEquipment instanceof Weapon) {
                success = character.equipWeapon((Weapon) retrievedEquipment);

                if (success) {
                    if (Launcher.getEquippedWeapon() != null) {
                        allEquipments.add(Launcher.getEquippedWeapon());
                        logger.info("Unequipped previous weapon: '{}'", Launcher.getEquippedWeapon().getName());
                    }
                    Launcher.setEquippedWeapon((Weapon) retrievedEquipment);
                    logger.info("Successfully equipped weapon: '{}' to character: '{}'",
                            retrievedEquipment.getName(), characterName);
                } else {
                    logger.warn("Failed to equip weapon: '{}' to character: '{}' - Type mismatch",
                            retrievedEquipment.getName(), characterName);
                }
            } else if (retrievedEquipment instanceof Armor) {
                success = character.equipArmor((Armor) retrievedEquipment);

                if (success) {
                    if (Launcher.getEquippedArmor() != null) {
                        allEquipments.add(Launcher.getEquippedArmor());
                        logger.info("Unequipped previous armor: '{}'", Launcher.getEquippedArmor().getName());
                    }
                    Launcher.setEquippedArmor((Armor) retrievedEquipment);
                    logger.info("Successfully equipped armor: '{}' to character: '{}'",
                            retrievedEquipment.getName(), characterName);
                } else {
                    logger.warn("Failed to equip armor: '{}' to character: '{}' - Battlemage cannot equip armor",
                            retrievedEquipment.getName(), characterName);
                }
            }

            if (success) {
                allEquipments.remove(retrievedEquipment);
                logger.debug("Removed equipment from inventory: {}", retrievedEquipment.getName());

                if (imgGroup.getChildren().size() != 1) {
                    imgGroup.getChildren().remove(1);
                }
                lbl.setText(retrievedEquipment.getClass().getSimpleName() + ":\n" + retrievedEquipment.getName());
                ImageView imgView = new ImageView(new Image(Launcher.class.getResource(retrievedEquipment.getImagepath()).toString()));
                imgGroup.getChildren().add(imgView);

                dragCompleted = true;
                logger.debug("UI updated successfully for equipment: {}", retrievedEquipment.getName());
            }

            Launcher.setAllEquipments(allEquipments);
            Launcher.setMainCharacter(character);
            Launcher.refreshPane();
        }
        event.setDropCompleted(dragCompleted);
        event.consume();
    }

    public static void onEquipDone(DragEvent event){
        Dragboard dragboard = event.getDragboard();
        ArrayList<BasedEquipment> allEquipments = Launcher.getAllEquipments();
        BasedEquipment retrievedEquipment = (BasedEquipment)dragboard.getContent(BasedEquipment.DATA_FORMAT);
        int pos = -1;
        for(int i = 0; i<allEquipments.size();i++){
            if(allEquipments.get(i).getName().equals(retrievedEquipment.getName())){
                pos = i;
            }
        }
        if(pos != -1){
            allEquipments.remove(pos);
            logger.debug("Removed equipped item from inventory list: {}", retrievedEquipment.getName());
        }

        Launcher.setAllEquipments(allEquipments);
        Launcher.refreshPane();
    }
}