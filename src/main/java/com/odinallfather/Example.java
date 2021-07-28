package com.odinallfather;

import com.google.gson.stream.JsonReader;
import com.odinallfather.json.JsonObject;
import com.odinallfather.json.JsonParser;
import com.odinallfather.json.JsonPrinter;
import com.odinallfather.json.JsonValue;

import java.io.FileReader;
import java.io.FileWriter;

public class Example {

    public void example() throws Exception {
        //Writing a object into a file
        JsonObject objectToWrite = new JsonObject();
        //Filling the object
        objectToWrite.put("name", "Odin").put("age", 5000).put("god-type", "war").put("allfather", true);
        //Writing this object into the file
        JsonPrinter printer = new JsonPrinter(new FileWriter("somefile.json"));
        //Set pretty printing to true, because we want it pretty
        printer.setPrettyPrint(true);
        //Then print the object
        printer.print(objectToWrite);
        //After you print everything close the printer
        printer.close();

        //Read the object
        //Create a json parser
        JsonParser parser = new JsonParser(new FileReader("somefile.json"));
        //Read the object or the array, or whatever you wrote into the file
        JsonObject objectToRead = parser.readObject();
        //Change the object
        objectToRead.put("weapon", new JsonObject().put("name", "gungnir").put("damage", 100000));
        objectToRead.replace("name", new JsonValue("Odin the Allfather"));
        //Then create a new printer
        JsonPrinter newPrinter = new JsonPrinter(new FileWriter("somefile.json"));
        //And do the same as above
        printer.setPrettyPrint(true); //Optional
        printer.print(objectToRead);
        printer.close();

    }

}
