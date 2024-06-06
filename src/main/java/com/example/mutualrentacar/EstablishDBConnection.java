package com.example.mutualrentacar;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Binary;

import javax.print.Doc;
import javax.swing.*;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class EstablishDBConnection {
    public static String loggedUsername;
    static MongoDatabase database = MongoClients.create(MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(
                    "mongodb+srv://Kaloyan:Miroslav1921@cluster1.2st8xsv.mongodb.net/?retryWrites=true&w=majority&appName=Cluster1"))
            .build()).getDatabase("RentACar");
    static MongoCollection<Document> collection;

    public static void addApprovedReq(String reqId){
        collection = database.getCollection("ApprovedRequests");
        Document document = new Document().append("ReqId", reqId);
        collection.insertOne(document);
    }

    public static String findResult(String setCollection, String searchBy, String value, String target) {
        collection = database.getCollection(setCollection);
        Document query = new Document(searchBy, value);
        Document result = collection.find(query)
                .projection(Projections.fields(Projections.include(target, "_id")))
                .first();
        return String.valueOf(result.get(target));
    }

    public static String findViaId(String setCollection, String id, String target) {
        collection = database.getCollection(setCollection);
        FindIterable<Document> documents = collection.find();
        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                if(String.valueOf(document.get("_id")).equals(id))
                    return String.valueOf(document.get(target));
            }
        }
        return null;

    }


    public static ArrayList<String> findViaIdArr(String id, Boolean flag) {
        collection = database.getCollection("Cars");
        FindIterable<Document> documents = collection.find();
        ArrayList<String> arr = new ArrayList<>();
        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                if(String.valueOf(document.get("_id")).equals(id)){
                    if(flag) {
                        arr.add(String.valueOf(document.get("Engine")));    //0
                        arr.add(String.valueOf(document.get("Year")));      //1
                        arr.add(String.valueOf(document.get("Fuels")));     //2
                        arr.add(String.valueOf(document.get("Description")));   //3
                        arr.add(String.valueOf(document.get("Brand")));     //4
                        arr.add(String.valueOf(document.get("Model")));     //5
                        return arr;
                    }else {
                        arr.add(String.valueOf(document.get("Description")));   //3
                        arr.add(String.valueOf(document.get("Brand")));     //4
                        arr.add(String.valueOf(document.get("Model")));     //5
                        return arr;
                    }
                }

            }
        }
        return null;
    }


    //TODO test this method
    public static ArrayList<String> findCarViaId(String setCollection, String id){
        collection = database.getCollection(setCollection);
        FindIterable<Document> documents = collection.find();
        ArrayList<String> arr = new ArrayList<>();
        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                if(String.valueOf(document.get("_id")).equals(id))
                {
                    arr.add(String.valueOf(document.get("Brand")));
                    arr.add(String.valueOf(document.get("Model")));
                    arr.add(String.valueOf(document.get("Engine")));
                    arr.add(String.valueOf(document.get("Year")));
                    arr.add(String.valueOf(document.get("Fuels")));
                    arr.add(String.valueOf(document.get("Description")));
                    return arr;
                }

            }
        }
        return null;
    }



    public static Date findDateViaId(String setCollection, String id, String target) {
         collection = database.getCollection(setCollection);
        FindIterable<Document> documents = collection.find();
        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                if(String.valueOf(document.get("_id")).equals(id)){
                    return (Date) document.get(target);
                }
            }
        }
        return null;
    }

    public static ArrayList<String> findId(String value, String conn, String target) {
        collection = database.getCollection(conn);
        ArrayList<String> arr = new ArrayList<>();
        FindIterable<Document> documents = collection.find();
        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                if(String.valueOf(document.get(value)).equals(EstablishDBConnection.loggedUsername))
                    arr.add(String.valueOf(document.get(target)));
            }
        }
        return arr;
    }

    public static ArrayList<String> findIdArr(String coll, String target) {
        collection = database.getCollection(coll);
        collection.createIndex(new Document(target, 1));
        ArrayList<String> arr = new ArrayList<>();

        FindIterable<Document> documents = collection.find();
        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                        arr.add(String.valueOf(document.get(target)));
            }
        }
        return arr;
    }



    public static ArrayList<MenuItem> findOwnerCars(SplitMenuButton splitMenu, String removeId){
        collection = database.getCollection("Cars");
        ArrayList<MenuItem> arr = new ArrayList<>();
        FindIterable<Document> documents = collection.find();
        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                if(String.valueOf(document.get("OwnerUsername")).equals(EstablishDBConnection.loggedUsername)
                        && removeId.equals(String.valueOf(document.get("_id"))) == false ) {
                    MenuItem menuItem = new MenuItem((document.get("Brand")) + " " + document.get("Model"));
                    menuItem.setOnAction(actionEvent ->
                    {
                            splitMenu.setText(menuItem.getText());

                            CreateRequestController.clientCarId = String.valueOf(document.get("_id"));
                    });
                    arr.add(menuItem);
                }
            }
        }
        return arr;
    }

    public static void insertUser(String email, String username, String password) throws Exception{
        collection = database.getCollection("User");
        FindIterable<Document> documents = collection.find().projection(new Document("_id", 0).append("Username", 1));
        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                if (document.get("Username").equals(username)){
                    JOptionPane.showMessageDialog(null,
                            "Това потребителско име вече съществува!", "Грешка!", JOptionPane.ERROR_MESSAGE);
                    throw new Exception("Това потребителско име вече съществува!");
                }
            }
        }
        Document document = new Document().append("Username", username)
                .append("Password", password).append("Email", email);
        collection.insertOne(document);
    }

    public static ArrayList<Binary> getImages(String setCollection, String id, String target) {
        collection = database.getCollection(setCollection);
        collection.createIndex(new Document("_id", 1));
        collection.createIndex(new Document(target, 1));
        ArrayList<Binary> arr = new ArrayList<>();
        FindIterable<Document> documents = collection.find();
        if(id==null)
            try (MongoCursor<Document> cursor = documents.iterator()) {
                while (cursor.hasNext()) {
                    Document document = cursor.next();
                    arr.add((Binary) document.get(target));
                }
            }
        else
            try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                if(String.valueOf(document.get("_id")).equals(id))
                    arr.add((Binary) document.get(target));
            }
        }
        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                arr.add((Binary) document.get(target));
            }
        }
        return arr;
    }


    public static ArrayList<String> getItem(String coll ,String target){
        collection = database.getCollection(coll);
        collection.createIndex(new Document(target, 1));

        ArrayList<String> arr = new ArrayList<>();

        FindIterable<Document> documents =
        collection.find().projection(new Document("_id", 0).append(target, 1));


        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                arr.add(String.valueOf(document.get(target)));
            }
        }
        return arr;
    }

    public static void editData(String setCollection, String searchBy, String value, String editBy, String newValue){
        collection = database.getCollection(setCollection);
        if(EstablishDBConnection.findResult(setCollection, searchBy, value, searchBy)!=null) {

            collection.updateOne(new Document(searchBy, value),
            new Document("$set", new Document(editBy, newValue)));


            JOptionPane.showMessageDialog(null, "Вашата парола беше променента успешно!",
                    "Променена парола!", JOptionPane.INFORMATION_MESSAGE);
        }else
            JOptionPane.showMessageDialog(null, "Възникна грешка при промяната на паролата!",
                    "Грешка!", JOptionPane.ERROR_MESSAGE);
    }

    protected static void addCar(String ownerUsername, byte[] imageBytes, String brand,
                                 String model, int year, String fuel, String engine, String description) {
        try {
            collection = database.getCollection("Cars");

            Document document = new Document("imageData", new Binary(imageBytes))
                    .append("OwnerUsername", ownerUsername)
                    .append("Brand", brand)
                    .append("Model", model)
                    .append("Year", year)
                    .append("Fuels", fuel)
                    .append("Engine", engine)
                    .append("Description", description);

            collection.insertOne(document);

            System.out.println("Image saved successfully!");
        } catch (Exception ee) {
            System.err.println("Error! Please, try again later!");
            ee.printStackTrace();
        }
    }

    protected static int getNumOfRecords(String col){
            collection = database.getCollection(col);
            return (int) collection.countDocuments();
    }

    public static void addRequest(String Title, LocalDate startDate, LocalDate endDate,
                                  String txtDescription, String clientUsername, String clientCarId,
                                  String hostUsername, String hostCarId, int clientRec, int ownerRec) {
        try {
            collection = database.getCollection("Requests");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            Document document = new Document("Date", LocalDate.now().format(formatter))
                    .append("Title", Title)
                    .append("Start_Date", startDate)
                    .append("End_Date", endDate)
                    .append("Description", txtDescription)
                    .append("Client_Username", clientUsername)
                    .append("Client_Car_Id", clientCarId)
                    .append("Host_Username", hostUsername)
                    .append("Host_Car_Id", hostCarId)
                    .append("Client_Receive", clientRec)
                    .append("Host_Receive", ownerRec);
            collection.insertOne(document);
        } catch (Exception ee) {
            System.err.println("Error! Please, try again later!");
            ee.printStackTrace();
        }
    }

    public static boolean checkLikedCar(String carId) {
        collection = database.getCollection("SavedCars");
        FindIterable<Document> documents = collection.find();
        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                if(String.valueOf(document.get("Car_Id")).equals(carId) && document.get("user").equals(EstablishDBConnection.loggedUsername))
                    return true;
            }
        }
    return false;
    }

    public static void addLikedCar(String carId) {
            collection = database.getCollection("SavedCars");
            Document document = new Document("Car_Id", carId).append("user", EstablishDBConnection.loggedUsername);
            collection.insertOne(document);
    }

    public static ArrayList<String> findSavedIdCars(){
        collection = database.getCollection("SavedCars");
        ArrayList<String> arr = new ArrayList<>();
        FindIterable<Document> documents = collection.find();
        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                if(document.get("user").equals(EstablishDBConnection.loggedUsername))
                    arr.add(String.valueOf(document.get("Car_Id")));
            }
        }
        return arr;
    }

    public static void deleteLikedCar(String id) {
        collection = database.getCollection("SavedCars");
        FindIterable<Document> documents = collection.find();
        try (MongoCursor<Document> cursor = documents.iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                if(String.valueOf(document.get("Car_Id")).equals(id))
                    collection.deleteOne(document);
            }
        }
    }

    public static void delete(String coll, String target, String value) {
            collection = database.getCollection(coll);
            FindIterable<Document> documents = collection.find();
            try (MongoCursor<Document> cursor = documents.iterator()) {
                while (cursor.hasNext()) {
                    Document document = cursor.next();
                    if(String.valueOf(document.get(target)).equals(value))
                        collection.deleteOne(document);
                }
            }
        }

        public static void deleteAppReq(String carId){
            EstablishDBConnection.delete("Cars", "_id", carId);
            EstablishDBConnection.delete("Requests", "Host_Car_Id", carId);
            EstablishDBConnection.delete("SavedCars", "Car_Id", carId);

            collection = database.getCollection("Requests");
            ArrayList<String> arr = new ArrayList<>();
            FindIterable<Document> documents = collection.find();
            try (MongoCursor<Document> cursor = documents.iterator()) {
                while (cursor.hasNext()) {
                        Document document = cursor.next();
                        arr.add(String.valueOf(document.get("_id")));
                }
            }

            collection = database.getCollection("ApprovedRequests");
            FindIterable<Document> documentss = collection.find();
            try (MongoCursor<Document> cursor = documentss.iterator()) {
                while (cursor.hasNext()) {
                        Document document = cursor.next();
                       if(arr.contains(String.valueOf(document.get("ReqId"))) == false)
                           collection.deleteOne(document);
                }
            }
        }

}