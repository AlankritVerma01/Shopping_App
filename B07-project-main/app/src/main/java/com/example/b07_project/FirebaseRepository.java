package com.example.b07_project;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.b07_project.data_classes.CustomerData;
import com.example.b07_project.data_classes.Cart;
import com.example.b07_project.data_classes.CartItem;
import com.example.b07_project.data_classes.ProductData;
import com.example.b07_project.data_classes.ProductOrderData;
import com.example.b07_project.data_classes.SellerData;
import com.example.b07_project.data_classes.StoreData;
import com.example.b07_project.data_classes.customer_register_data;
import com.example.b07_project.data_classes.seller_register_data;
import com.example.b07_project.data_classes.OrderData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class FirebaseRepository {
    public static final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    public static final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private static DatabaseReference getSellerReference(String uid) {
        return db.child("sellers").child(uid);
    }

    private static DatabaseReference getStoreReference(String shopId) {
        return db.child("shops").child(shopId);
    }

    public static DatabaseReference getProductReference(String productId) {
        return db.child("products").child(productId);
    }

    private static DatabaseReference getOrderReference(String orderId) {
        return db.child("orders").child(orderId);
    }

    private static DatabaseReference getCustomerReference(String uid) {
        return db.child("customers").child(uid);
    }

    /**
     * Returns user id of current user, or null if user is not signed in
     *
     * @return user id string or null
     */
    public static String getUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return null;
        }
        return user.getUid();
    }

    /**
     * Signs out current user
     */
    public static void logout() {
        mAuth.signOut();
    }

    /**
     * Attempts to login seller with email and password credentials
     * <p>
     * After login attempt, asynchronously runs onSuccess if login is successful, or onFailure if login fails
     *
     * @param email     Email of seller
     * @param password  Password of seller
     * @param onSuccess Method to run if login successful
     * @param onFailure Method to run if login fails
     */
    public static void loginSeller(String email, String password, Runnable onSuccess, Consumer<Exception> onFailure) {
        // Login user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> {
                    // Check if seller account exists for user
                    getSellerReference(getUserId()).get()
                            .addOnSuccessListener(
                                    snapshot -> {
                                        if (snapshot.exists()) {
                                            onSuccess.run();
                                        } else {
                                            logout();
                                            onFailure.accept(new FirebaseException("User does not have seller account - must register"));
                                        }
                                    }
                            ).addOnFailureListener(exception -> onFailure.accept(exception));
                })
                .addOnFailureListener(result -> onFailure.accept(result));
    }

    /**
     * Attempts to register seller with email and password.
     * <p>
     * After registration attempt, asynchronously adds sellerData to database and runs onSuccess if registration is successful,
     * or runs onFailure if registration fails.
     *
     * @param email      Email of seller
     * @param password   Password of seller
     * @param sellerData Data to add to database upon successful registration
     * @param onSuccess Method to run if registration successful
     * @param onFailure Method to run if registration fails
     */
    public static void registerSeller(String email, String password, seller_register_data sellerData, Runnable onSuccess, Consumer<Exception> onFailure) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> {
                    addSellerData(sellerData);
                    addShopData(sellerData.getShopid(), sellerData.getShopname());
                    onSuccess.run();
                })
                .addOnFailureListener(result -> onFailure.accept(result));
    }

    private static void addSellerData(seller_register_data sellerData) {
        if (getUserId() != null) {
            getSellerReference(getUserId()).setValue(sellerData);
        }
    }

    private static void addShopData(String shopId, String shopName) {
        DatabaseReference shopReference = getStoreReference(shopId);
        shopReference.child("name").setValue(shopName);
    }

    /**
     * Retrieves current seller data from database and runs method depending on success
     *
     * @param onDataRetrieved    Method to run if data retrieved
     * @param onDataDoesNotExist Method to run if data does not exist
     */
    public static void getCurrentSellerData(Consumer<SellerData> onDataRetrieved, Runnable onDataDoesNotExist) {
        if (getUserId() != null) {
            getSellerData(getUserId(), onDataRetrieved, onDataDoesNotExist);
        }
    }

    /**
     * Retrieves data of seller specified by uid from database and runs method depending on success
     *
     * @param uid                Id of seller to retrieve data from
     * @param onDataRetrieved    Method to run if data retrieved
     * @param onDataDoesNotExist Method to run if data does not exist
     */
    public static void getSellerData(String uid, Consumer<SellerData> onDataRetrieved, Runnable onDataDoesNotExist) {
        getSellerReference(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    onDataRetrieved.accept(new SellerData(
                            snapshot.child("firstName").getValue(String.class),
                            snapshot.child("lastName").getValue(String.class),
                            snapshot.child("email").getValue(String.class),
                            snapshot.child("shopid").getValue(String.class)));
                } else {
                    onDataDoesNotExist.run();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Updates data of current seller in database. The contents of data is specified in {@link SellerData}.
     *
     * @param newData Data to update current seller to
     */
    public static void updateCurrentSellerData(SellerData newData) {
        if (getUserId() != null) {
            updateSellerData(getUserId(), newData);
        }
    }

    /**
     * Updates data of seller specified by uid in database. The contents of the data is specified in {@link SellerData}.
     *
     * @param uid     Id of data to update data for
     * @param newData Data to update seller to
     */
    public static void updateSellerData(String uid, SellerData newData) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("firstName", newData.getFirstName());
        updates.put("lastName", newData.getLastName());
        updates.put("email", newData.getEmail());

        getSellerReference(uid).updateChildren(updates);
    }

    /**
     * Attempts to login customer with email and password credentials
     * <p>
     * After login attempt, asynchronously runs onSuccess if login is successful, or onFailure if login fails
     *
     * @param email     Email of customer
     * @param password  Password of customer
     * @param onSuccess Method to run if login successful
     * @param onFailure Method to run if login fails
     */
    public static void loginCustomer(String email, String password, Runnable onSuccess, Consumer<Exception> onFailure) {
        // Login user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> {
                    // Check if seller account exists for user
                    getCustomerReference(getUserId()).get()
                            .addOnSuccessListener(
                                    snapshot -> {
                                        if (snapshot.exists()) {
                                            onSuccess.run();
                                        } else {
                                            logout();
                                            onFailure.accept(new FirebaseException("User does not have customer account - must register"));
                                        }
                                    }
                            ).addOnFailureListener(exception -> onFailure.accept(exception));
                })
                .addOnFailureListener(result -> onFailure.accept(result));
    }

    /**
     * Retrieves current customer data from database and runs method depending on success
     * @param onDataRetrieved Method to run if data retrieved
     * @param onDataDoesNotExist Method to run if data does not exist
     */
    public static void getCurrentCustomerData(Consumer<CustomerData> onDataRetrieved, Runnable onDataDoesNotExist) {
        if (getUserId() != null) {
            getCustomerData(getUserId(), onDataRetrieved, onDataDoesNotExist);
        }
    }

    /**
     * Retrieves data of customer specified by uid from database and runs method depending on success
     * @param uid Id of customer to retrieve data from
     * @param onDataRetrieved Method to run if data retrieved
     * @param onDataDoesNotExist Method to run if data does not exist
     */
    public static void getCustomerData(String uid, Consumer<CustomerData> onDataRetrieved, Runnable onDataDoesNotExist) {
        getCustomerReference(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    onDataRetrieved.accept(new CustomerData(
                            snapshot.child("firstName").getValue(String.class),
                            snapshot.child("lastName").getValue(String.class),
                            snapshot.child("email").getValue(String.class)));
                } else {
                    onDataDoesNotExist.run();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Updates data of current customer in database. The contents of data is specified in {@link CustomerData}.
     * @param newData Data to update current customer to
     */
    public static void updateCurrentCustomerData(CustomerData newData) {
        if (getUserId() != null) {
            updateCustomerData(getUserId(), newData);
        }
    }

    /**
     * Updates data of customer specified by uid in database. The contents of the data is specified in {@link CustomerData}.
     * @param uid Id of data to update data for
     * @param newData Data to update customer to
     */
    public static void updateCustomerData(String uid, CustomerData newData) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("firstName", newData.getFirstName());
        updates.put("lastName", newData.getLastName());

        getCustomerReference(uid).updateChildren(updates);
    }

    /**
     * Retrieves data of store specified by store id from database and runs method depending on success
     *
     * @param storeId         Id of store to retrieve data for
     * @param onDataRetrieved Method to run if data retrieved
     * @param onException     Method to run if data does not exist
     */
    public static void getStoreData(String storeId, Consumer<StoreData> onDataRetrieved, Consumer<Exception> onException) {
        getStoreReference(storeId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<String> productIds = new ArrayList<>();
                    for (DataSnapshot product : snapshot.child("productIds").getChildren()) {
                        productIds.add(product.getKey());
                    }
                    onDataRetrieved.accept(new StoreData(
                            storeId,
                            snapshot.child("name").getValue(String.class),
                            snapshot.child("description").getValue(String.class),
                            productIds));
                } else {
                    onException.accept(new Exception("Store id " + storeId + " does not exist!"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onException.accept(error.toException());
            }
        });
    }

    /**
     * Updates data of store specified by store id from database and runs method depending on success
     *
     * @param storeId     Id of store to update data for
     * @param newData     Data to update product with
     * @param onSuccess   Method to run if product updated
     * @param onException Method to run if exception happens
     */
    public static void updateStoreData(String storeId, StoreData newData, Runnable onSuccess, Consumer<Exception> onException) {
        if (!storeId.equals(newData.getStoreId())) {
            onException.accept(new IllegalArgumentException("Store id parameter and newData store id have differing values"));
        }
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", newData.getStoreName());
        updates.put("description", newData.getStoreDesc());

        getStoreReference(storeId).updateChildren(updates, (error, ref) -> {
            if (error != null) {
                onException.accept(error.toException());
            } else {
                onSuccess.run();
            }
        });
    }

    /**
     * Retrieves list of product data specified by product ids and runs method depending on success
     *
     * @param productIds      List of ids to retrieve data for
     * @param onDataRetrieved Method to run if data retrieved
     * @param onException     Method to run if exception happens
     */
    public static void getProductListData(List<String> productIds, Consumer<Map<String, ProductData>> onDataRetrieved, Consumer<Exception> onException) {
        db.child("products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, ProductData> productMap = new HashMap<>();
                for (String id : productIds) {
                    DataSnapshot product = snapshot.child(id);
                    productMap.put(id, new ProductData(id,
                            product.child("name").getValue(String.class),
                            product.child("description").getValue(String.class),
                            product.child("price").getValue(Integer.class),
                            product.child("quantity").getValue(Integer.class)));
                }
                onDataRetrieved.accept(productMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onException.accept(error.toException());
            }
        });
    }

    /**
     * Retrieves data of product specified by product id from database and runs method depending on success
     *
     * @param productId          Id of product to retrieve data for
     * @param onProductRetrieved Method to run if product retrieved
     * @param onException        Method to run if exception happens
     */
    public static void getProductData(String productId, Consumer<ProductData> onProductRetrieved, Consumer<Exception> onException) {
        getProductReference(productId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    onProductRetrieved.accept(new ProductData(
                            productId,
                            snapshot.child("name").getValue(String.class),
                            snapshot.child("description").getValue(String.class),
                            snapshot.child("price").getValue(Integer.class),
                            snapshot.child("quantity").getValue(Integer.class)
                    ));
                } else {
                    onException.accept(new Exception("Product id " + productId + " does not exist"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onException.accept(error.toException());
            }
        });
    }

    /**
     * Updates data of product specified by product id from database and runs method depending on success
     *
     * @param productId           Id of product to update data for
     * @param newProductData      Data to update product with
     * @param onProductUpdated    Method to run if product updated
     * @param onProductNotUpdated Method to run if product not updated
     */
    public static void updateProductData(String productId, ProductData newProductData, Runnable onProductUpdated, Consumer<Exception> onProductNotUpdated) {
        if (!productId.equals(newProductData.getProductId())) {
            onProductNotUpdated.accept(new IllegalArgumentException("Product id parameter and newProductData id have differing values"));
        }
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", newProductData.getProductName());
        updates.put("description", newProductData.getProductDescription());
        updates.put("price", newProductData.getProductPrice());
        updates.put("quantity", newProductData.getProductQuantity());
        getProductReference(productId).updateChildren(updates, (error, ref) -> {
            if (error != null) {
                onProductNotUpdated.accept(error.toException());
            } else {
                onProductUpdated.run();
            }
        });
    }

    public static void createItem(String productId, String shopID, String name, String description, double price, int quantity) {
        DatabaseReference productsRef = db.child("products");
        DatabaseReference shopsRef = db.child("shops");

        // Push the new product to the "products" section
        DatabaseReference newProductRef = productsRef.child(productId);

        // Map to represent the product data
        Map<String, Object> productData = new HashMap<>();
        productData.put("name", name);
        productData.put("description", description);
        productData.put("price", price);
        productData.put("quantity", quantity);

        newProductRef.setValue(productData)
                .addOnSuccessListener(aVoid -> {
                    Log.e("FirebaseRepository", "New Product added successfully");

                    // now add this product ID under the child node of the shop
                    DatabaseReference shopRef = shopsRef.child(shopID).child("productIds");
                    Map<String, Object> productIds = new HashMap<>();
                    productIds.put(productId, true);
                    shopRef.updateChildren(productIds)
                            .addOnSuccessListener(aVoid1 -> {
                                Log.e("FirebaseRepository", "Product ID added to shop successfully");
                            })
                            .addOnFailureListener(e -> {
                                Log.e("FirebaseRepository", "Error in adding productId to shop");
                            });

                })
                .addOnFailureListener(e -> {
                    Log.e("FirebaseRepository", "Error adding new product");
                });
    }

    /**
     * Attempts to register customer with email and password.
     * <p>
     * After registration attempt, asynchronously adds customerData to database and runs onSuccess if registration is successful,
     * or runs onFailure if registration fails.
     * @param email Email of customer
     * @param password Password of customer
     * @param customerData Data to add to database upon successful registration
     * @param onSuccess Method to run if login successful
     * @param onFailure Method to run if login fails
     */
    public static void registerCustomer(String email, String password, customer_register_data customerData, Runnable onSuccess, Consumer<Exception> onFailure) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(result -> {
                    addCustomerData(customerData);
                    onSuccess.run();
                })
                .addOnFailureListener(result -> onFailure.accept(result));
    }
    private static void addCustomerData(customer_register_data customerData) {
        if (getUserId() != null) {
            getCustomerReference(getUserId()).setValue(customerData);
        }
    }

    // CART FUNCTIONS

    // use this function to get all info within the cart put into a HashMap
    public static void getCartData(String customerID, Consumer<Cart> onCartRetrieved) {
        DatabaseReference cartRef = db.child("customers").child(customerID).child("cart");
        Cart cart = new Cart();
        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot storeCart : dataSnapshot.getChildren()) {
                        String storeID = storeCart.getKey();
                        List<CartItem> cartItems = new ArrayList<>();

                        if (storeCart.hasChild("items")) {
                            for (DataSnapshot item : storeCart.child("items").getChildren()) {
                                String itemID = item.getKey();
                                int price = (int) ((long) item.child("price").getValue());
                                int quantity = (int) ((long) item.child("quantity").getValue());
                                String name = (String) item.child("name").getValue();

                                CartItem cartItem = new CartItem(itemID, price, quantity, name);
                                cartItems.add(cartItem);
                            }
                        }
                        cart.addStoreItems(storeID, cartItems);
                    }
                }
                // Pass the populated cart object to the Consumer for further processing
                onCartRetrieved.accept(cart);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error if needed
            }
        });
    }


    // use this function to finalize the cart i.e shifting all cart items into orders for stores
    public static void finalizeCart(String customerID, Runnable onSuccess, Consumer<Exception> onException) {
        DatabaseReference cartRef = db.child("customers").child(customerID).child("cart");
        // check if the cart exists for the user else nothing to do
        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    // create an bigOrderID (the id that is the aggregate of smaller orders)
                    String bigOrderID = UUID.randomUUID().toString().replaceAll("_", "");
                    HashMap<String, Object> suborders = new HashMap<>();

                    // loop through each storeID in the cart and convert these into orders by generating an ID
                    for (DataSnapshot storeIDSnapshot : snapshot.getChildren()) {

                        // create an OrderID for the sub orders
                        String subOrderID = UUID.randomUUID().toString().replaceAll("_", "");
                        suborders.put(subOrderID, true);
                        String storeID = storeIDSnapshot.getKey();

                        // create an order based off the storeID we're at
                        createOrder(storeID, subOrderID, customerID);

                    }

                    // now that all Orders have been added we can now add the order into the customers orders
                    HashMap<String, Object> bigOrder = new HashMap<>();
                    bigOrder.put("complete", false);
                    bigOrder.put("date", getCurrentDate());

                    // now update children
                    DatabaseReference customerOrderRef = db.child("customers").child(customerID).child("orders").child(bigOrderID);
                    customerOrderRef.updateChildren(bigOrder)
                            .addOnSuccessListener(
                                    _unused -> customerOrderRef.child("suborders").updateChildren(suborders)
                                            .addOnSuccessListener(_unused2 -> onSuccess.run())
                                            .addOnFailureListener(exception -> onException.accept(exception))
                            )
                            .addOnFailureListener(exception -> onException.accept(exception));
                    ;

                    // delete cart now that it's all been converted
                    // deleteCart(customerID);


                } else {
                    // nothing needs to be done
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // use this function if the user wants to remove an item from the cart
    public static void removeItemFromCart(String customerID, String storeID, String itemID) {
        // this will delete the item at that ID
        DatabaseReference itemRef = db.child("customers")
                .child(customerID).child("cart")
                .child(storeID).child("items");
        DatabaseReference storeRef = db.child("customers").child(customerID).child("cart").child(storeID);

        // attempt to use removeValue to delete the item from the cart
        itemRef.child(itemID).removeValue();

        // now since a deletion occurred we need to check if the store has become empty i.e no items section
        storeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild("items")) {
                    // do nothing we have an item for that store

                } else {
                    // since no items we delete the storeID from the Firebase
                    // ensure that the snapshot exists
                    storeRef.removeValue();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // handle errors here
            }
        });

    }

    // use this function once the cart has pushed so we have a clean cart to work with
    public static void deleteCart(String customerID, Runnable onSuccess, Consumer<Exception> onException) {
        DatabaseReference cartRef = db.child("customers").child(customerID).child("cart");
        cartRef.removeValue().addOnSuccessListener(_unused -> onSuccess.run()).addOnFailureListener(exception -> onException.accept(exception));

    }

    public static void addItemToCart(String customerID, String storeID, String itemID, int price, int quantity, String name,
                                     OnSuccessListener<Void> onSuccessListener,
                                     OnFailureListener onFailureListener) {
        DatabaseReference cartRef = db.child("customers").child(customerID).child("cart");

        cartRef.child(storeID).child("complete").setValue(false, (databaseError, databaseReference) -> {
            if (databaseError != null) {
                onFailureListener.onFailure(databaseError.toException());
            } else {
                Log.e("FirebaseRepository", "addItemToCart storeID successful");
                Map<String, Object> itemData = new HashMap<>();
                itemData.put("name", name);
                itemData.put("price", price);
                itemData.put("quantity", quantity);

                DatabaseReference itemReference = cartRef.child(storeID).child("items").child(itemID);
                itemReference.updateChildren(itemData)
                        .addOnSuccessListener(aVoid -> {
                            Log.e("FirebaseRepository", "New item added successfully to cart");
                            onSuccessListener.onSuccess(null);
                        })
                        .addOnFailureListener(e -> {
                            Log.e("FirebaseRepository", "Error adding new item to cart");
                            onFailureListener.onFailure(e);
                        });
            }
        });
    }

    // this function calculates the total value of the current cart
    public static void totalCartValue(String customerID, Consumer<Integer> price){
        DatabaseReference cartRef = db.child("customers").child(customerID).child("cart");

        cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalValue = calculateTotalCartValue(snapshot);
                price.accept(totalValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                price.accept(0);
            }
        });
    }

    private static int calculateTotalCartValue(DataSnapshot cartSnapshot) {
        int totalValue = 0;

        // Check if the cart exists and has items
        for (DataSnapshot storeShot : cartSnapshot.getChildren()) {
            if (storeShot.exists() && storeShot.hasChild("items")) {
                for (DataSnapshot itemSnapshot : storeShot.child("items").getChildren()) {
                    int price = itemSnapshot.child("price").getValue(Integer.class);
                    int quantity = itemSnapshot.child("quantity").getValue(Integer.class);
                    totalValue += price * quantity;
                }
            }
        }

        return totalValue;
    }

    public static void changeItemInfo(String customerID, String storeID, String itemID, double new_price, int new_quantity, String new_name) {
        DatabaseReference itemRef = db.child("customers").child(customerID).child("cart").child(storeID).child("items").child(itemID);
        HashMap<String, Object> itemInfo = new HashMap<>();
        itemInfo.put("quantity", new_quantity);
        itemInfo.put("price", new_price);
        itemInfo.put("name", new_name);
        itemRef.updateChildren(itemInfo);
    }

    public static void changeItemQuantity(String customerID, String storeID, String itemID, int change, Runnable ifRun) {
        DatabaseReference itemRef = db.child("customers").child(customerID).child("cart").child(storeID).child("items").child(itemID);

        // set up a listener to find what the current value of the item is
        itemRef.child("quantity").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer currentQuantity = snapshot.getValue(Integer.class);
                if (currentQuantity != null) {
                    // ensure that the difference is non-negative before changing the value
                    int newQuantity = currentQuantity + change;
                    if (newQuantity >= 0) {
                        HashMap<String, Object> itemQuantity = new HashMap<>();
                        itemQuantity.put("quantity", newQuantity);
                        itemRef.updateChildren(itemQuantity).addOnSuccessListener(nothing->ifRun.run());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if needed
            }
        });
    }

    public static void getItemIDs(String storeID, Consumer<List<String>> itemIDs) {
        DatabaseReference productIDs = db.child("shops").child(storeID).child("productIds");

        productIDs.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> itemList = new ArrayList<>();
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    String itemID = snapshot.getKey();
                    itemList.add(itemID);
                }
                itemIDs.accept(itemList);
            } else {
                // Handle the error case
                System.out.println("Error fetching item IDs: " + task.getException());
            }
        });
    }


    // ORDER FUNCTIONS

    // create an order with a date within the Firebase
    public static void createOrder(String storeID, String newOrderID, String customerID) {
        // go to shops and put this OrderId along with true under the orders section
        DatabaseReference storeOrders = db.child("shops").child(storeID).child("orderIds");
        DatabaseReference orders = db.child("orders");
        DatabaseReference storeIDRef = db.child("customers").child(customerID).child("cart").child(storeID);
        DatabaseReference storeName = db.child("shops").child(storeID).child("name");

        final String[] store_name = new String[1];
        // now get the store name
        storeName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                store_name[0] = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Map<String, Object> orderData = new HashMap<>();
        orderData.put(newOrderID, true);

        storeOrders.updateChildren(orderData);

        // take a snapshot of storeID and shift what inside into newOrderID branch
        storeIDRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orders.child(newOrderID).setValue(snapshot.getValue());

                // now add in the customerID field and the date
                Map<String, Object> extra_info = new HashMap<>();
                extra_info.put("name", store_name[0]);
                extra_info.put("date", getCurrentDate());
                extra_info.put("customerID", customerID);
                orders.child(newOrderID).updateChildren(extra_info);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void checkBigOrderCompletion(String customerID, String bigOrderID, Consumer<Boolean> completionCallback) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference subOrderRef = rootRef.child("customers").child(customerID).child("orders").child(bigOrderID).child("suborders");

        // Counter to track the number of suborders checked
        final int[] subOrderCount = {0};

        // Flag to determine if all suborders are completed
        final boolean[] allSubOrdersCompleted = {true};

        subOrderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long childrenCount = snapshot.getChildrenCount();
                for (DataSnapshot suborderSnapshot : snapshot.getChildren()) {
                    String subOrderID = suborderSnapshot.getKey();
                    DatabaseReference orderRef = rootRef.child("orders").child(subOrderID).child("complete");

                    orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            Boolean completion = snapshot.getValue(Boolean.class);
                            if (completion != null && !completion) {
                                allSubOrdersCompleted[0] = false;
                            }

                            // Increment the counter
                            subOrderCount[0]++;

                            // Check if all suborders are checked
                            if (subOrderCount[0] == childrenCount) {
                                // Call the completionCallback with the result
                                completionCallback.accept(allSubOrdersCompleted[0]);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Handle error if needed
                            completionCallback.accept(false);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error if needed
                completionCallback.accept(false);
            }
        });
    }

    public static void markBigOrderComplete(String customerID, String bigOrderID) {
        db.child("customers").child(customerID).child("orders").child(bigOrderID).child("complete")
                .setValue(true);

    }

    public static void findCustomerID(String subOrderID, Consumer<String> callback){
        DatabaseReference ordersRef = db.child("orders");

        ordersRef.child(subOrderID).child("customerID").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String customerID = dataSnapshot.getValue(String.class);
                    callback.accept(customerID);

                } else {
                    callback.accept(null); // SubOrderID not found
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.accept(null); // Error occurred
            }
        });
    }

    /**
     * Retrieves data of order specified by order id from database and runs method depending on success
     *
     * @param orderId          Id of order to retrieve data for
     * @param onOrderRetrieved Method to run if order retrieved
     * @param onException      Method to run if exception happens
     */

    public static void getOrderData(String orderId, Consumer<OrderData> onOrderRetrieved, Consumer<Exception> onException) {
        getOrderReference(orderId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Create productId list
                    List<ProductOrderData> products = new ArrayList<>();
                    snapshot.child("items").getChildren().forEach(product -> products.add(new ProductOrderData(
                            product.getKey(),
                            product.child("name").getValue(String.class),
                            snapshot.child("name").getValue(String.class),
                            product.child("quantity").getValue(Integer.class),
                            product.child("price").getValue(Integer.class)
                    )));
                    // Get complete boolean, so we can perform null check on it
                    Boolean complete = snapshot.child("complete").getValue(Boolean.class);

                    onOrderRetrieved.accept(new OrderData(
                            orderId,
                            snapshot.child("customerId").getValue(String.class),
                            snapshot.child("date").getValue(String.class),
                            products,
                            complete != null && complete // Default value of false
                    ));
                } else {
                    onException.accept(new Exception("Order id " + orderId + " does not exist"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onException.accept(error.toException());
            }
        });
    }

    private static Task getSuborderListDataSnapshots(Set<String> suborderIds, Consumer<List<DataSnapshot>> onSuccess, Consumer<Exception> onException) {
        return db.child("orders").get()
            .addOnSuccessListener(
                    result -> {
                        List<DataSnapshot> suborderSnapshots = new ArrayList<>();
                        for (DataSnapshot orderSnapshot : result.getChildren()) {
                            if (suborderIds.contains(orderSnapshot.getKey())) {
                                suborderSnapshots.add(orderSnapshot);
                            }
                        }
                        onSuccess.accept(suborderSnapshots);
                    }
            )
            .addOnFailureListener(exception -> onException.accept(exception));
    }

    public static void getFlattenedOrderItems(String customerId, String orderId, Consumer<List<ProductOrderData>> onSuccess, Consumer<Exception> onException) {
        // Generate order list
        getCustomerReference(customerId).child("orders").child(orderId).child("suborders").get()
                .addOnSuccessListener(
                        snapshot -> {
                            Set<String> suborderIds = new HashSet<>();
                            for (DataSnapshot suborderId : snapshot.getChildren()) {
                                suborderIds.add(suborderId.getKey());
                            }
                            getSuborderListDataSnapshots(suborderIds,
                                    orders -> {
                                        List<ProductOrderData> products = new ArrayList<>();
                                        orders.forEach(
                                                order -> order.child("items").getChildren().forEach(
                                                        product -> products.add(new ProductOrderData(
                                                                product.getKey(),
                                                                product.child("name").getValue(String.class),
                                                                order.child("name").getValue(String.class),
                                                                product.child("quantity").getValue(Integer.class),
                                                                product.child("price").getValue(Integer.class)
                                        ))));
                                        onSuccess.accept(products);
                                    },
                                    exception -> onException.accept(exception));
                        }
                ).addOnFailureListener(
                        exception -> onException.accept(exception)
                );
    }

    /**
     * Mark order given by orderId as complete
     *
     * @param orderId Id of order to mark as complete
     */
    public static void markOrderComplete(String orderId, Runnable onSuccess) {
        getOrderReference(orderId).child("complete").setValue(true).addOnSuccessListener(_unused -> onSuccess.run());
    }

    /**
     * Retrieves orders for store specified by store id from database and runs method depending on success
     *
     * @param storeId           Id of store to retrieve orders for
     * @param onOrdersRetrieved Method to run if orders retrieved
     * @param onException       Method to run if exception happens
     */

    public static void getStoreOrders(String storeId, Consumer<List<OrderData>> onOrdersRetrieved, Consumer<Exception> onException) {
        DatabaseReference storeOrderIDRef = db.child("shops").child(storeId).child("orderIds");
        storeOrderIDRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> orderIds = new HashSet<>();
                for (DataSnapshot orderIDSnapshot : dataSnapshot.getChildren()) {
                    orderIds.add(orderIDSnapshot.getKey());
                }

                getOrderDataSnapshots(orderIds,
                        orderSnapshots -> {
                            List<OrderData> orders = new ArrayList<>();
                            for (DataSnapshot orderSnapshot : orderSnapshots) {
                                OrderData orderData = new OrderData();
                                orderData.setOrderId(orderSnapshot.getKey());
                                orderData.setCustomerId(orderSnapshot.child("customerId").getValue(String.class));
                                orderData.setDate(orderSnapshot.child("date").getValue(String.class));
                                orderData.setComplete(orderSnapshot.child("complete").getValue(Boolean.class));
                                orderData.setStoreName(orderSnapshot.child("name").getValue(String.class));

                                List<ProductOrderData> items = new ArrayList<>();
                                DataSnapshot itemsSnapshot = orderSnapshot.child("items");
                                for (DataSnapshot itemSnapshot : itemsSnapshot.getChildren()) {
                                    String productId = itemSnapshot.child("productId").getValue(String.class);
                                    String productName = itemSnapshot.child("productName").getValue(String.class);
                                    Integer quantity = itemSnapshot.child("quantity").getValue(Integer.class);
                                    Integer price = itemSnapshot.child("price").getValue(Integer.class);

                                    ProductOrderData productOrderData = new ProductOrderData(productId, productName, orderData.getStoreName(), quantity, price);
                                    items.add(productOrderData);
                                }

                                orderData.setItems(items);
                                orders.add(orderData);
                            }
                            onOrdersRetrieved.accept(orders);
                        },
                        onException);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if needed
                onException.accept(databaseError.toException());
            }
        });
    }

    private static void getOrderDataSnapshots(Set<String> orderIds, Consumer<List<DataSnapshot>> onSuccess, Consumer<Exception> onFailure) {
        db.child("orders").get()
                .addOnSuccessListener(
                        result -> {
                            List<DataSnapshot> suborderSnapshots = new ArrayList<>();
                            for (DataSnapshot orderSnapshot : result.getChildren()) {
                                if (orderIds.contains(orderSnapshot.getKey())) {
                                    suborderSnapshots.add(orderSnapshot);
                                }
                            }
                            onSuccess.accept(suborderSnapshots);
                        }
                )
                .addOnFailureListener(exception -> onFailure.accept(exception));
    }


    //    All Store
    public static void getStoreIds(Consumer<String> onStoreIdRetrieved, Consumer<Exception> onException) {
        DatabaseReference storesReference = FirebaseDatabase.getInstance().getReference("shops");

        storesReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot storeSnapshot : snapshot.getChildren()) {
                    String storeId = storeSnapshot.getKey();
                    onStoreIdRetrieved.accept(storeId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onException.accept(error.toException());
            }
        });
    }

    public static void getUserData(String userId, Consumer<customer_register_data> onDataRetrieved, Consumer<Exception> onException) {
        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("customers").child(userId);

        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    customer_register_data userData = snapshot.getValue(customer_register_data.class);
                    onDataRetrieved.accept(userData);
                } else {
                    onException.accept(new Exception("User data not found for user ID: " + userId));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onException.accept(error.toException());
            }
        });
    }

    public static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date currentDate = new Date(System.currentTimeMillis());
        return dateFormat.format(currentDate);
    }

    public static void findBigOrderID(String subOrderId, BiConsumer<String, String> callback) {
        findCustomerID(subOrderId,
                customerId -> {
                    getCustomerReference(customerId).child("orders").get().addOnSuccessListener(
                        snapshot -> {
                            for (DataSnapshot order : snapshot.getChildren()) {
                                if (suborderInOrder(order, subOrderId)) {
                                    callback.accept(order.getKey(), customerId);
                                }
                            }
                        }
                    );
                });
    }

    public static void updateBigOrderCompleted(String customerId, String bigOrderId) {
        DatabaseReference bigOrderReference = getCustomerReference(customerId).child("orders").child(bigOrderId);
        bigOrderReference.child("suborders").get().addOnSuccessListener(
                snapshot -> {
                    Set<String> suborderIds = new HashSet<>();
                    for (DataSnapshot suborderId : snapshot.getChildren()) {
                        suborderIds.add(suborderId.getKey());
                    }
                    FirebaseRepository.getSuborderListDataSnapshots(suborderIds,
                            suborders -> {
                                for (DataSnapshot order : suborders) {
                                    if (!order.hasChild("complete") || order.child("complete").getValue(Boolean.class) == false) {
                                        return;
                                    }
                                }
                                bigOrderReference.child("complete").setValue(true);
                            },
                            exception -> {});
                }
        );
    }

    public static boolean suborderInOrder(DataSnapshot order, String suborderId) {
        for (DataSnapshot suborder : order.child("suborders").getChildren()) {
            if (suborder.getKey().equals(suborderId)) {
                return true;
            }
        }
        return false;
    }
}