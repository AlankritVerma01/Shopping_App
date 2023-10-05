package com.example.b07_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Image storage format: /itemId/(image1.jpg, image2.jpg, ...)
 */
public class ImageRepository {
    private static final StorageReference imageDb = FirebaseStorage.getInstance().getReference();
    private static final long IMAGE_SIZE_LIMIT = 1024 * 1024; // Limit is 1MB

    private static StorageReference getItemReference(String itemId) {
        return imageDb.child(itemId);
    }

    public static int getDefaultImageResource() {
        return R.drawable.default_item;
    }

    public static Bitmap convertBytesToBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static byte[] convertBitmapToBytes(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public static String getImageFilename(int index) {
        return "image" + (index + 1) + ".jpg";
    }

    public static void getItemThumbnail(String itemId, Consumer<Bitmap> onSuccess, Runnable onDataDoesNotExist, Consumer<Exception> onException) {
        getItemReference(itemId).listAll().addOnSuccessListener(
                result -> {
                    if (result.getItems().size() > 0) {
                        getItemImage(itemId, getImageFilename(0), onSuccess, onException);
                    } else {
                        onDataDoesNotExist.run();
                    }
                }
        ).addOnFailureListener(exception -> onException.accept(exception));
    }

    public static void getItemImages(String itemId, Consumer<List<Bitmap>> onSuccess, Consumer<Exception> onException) {
        getItemReference(itemId).listAll().addOnSuccessListener(
            result -> {
                List<StorageReference> imageReferences = result.getItems();
                // Sort alphabetically to put in appropriate place in bitmap list
                imageReferences.sort((item1, item2) -> item1.getName().compareTo(item2.getName()));

                // Create bitmap list
                List<Bitmap> images = new ArrayList<>(Collections.nCopies(imageReferences.size(), null));
                // Create list of tasks to download each image
                List<Task> tasks = new ArrayList<>();
                for (int i = 0; i < imageReferences.size(); i++) {
                    final int finalI = i;
                    tasks.add(imageReferences.get(i).getBytes(IMAGE_SIZE_LIMIT)
                            .addOnSuccessListener(
                                    bytes -> images.set(finalI, convertBytesToBitmap(bytes))
                            ).addOnFailureListener(
                                    exception -> onException.accept(exception)
                            ));
                }
                Tasks.whenAllSuccess(tasks).addOnSuccessListener(
                        _unused -> onSuccess.accept(images)
                );
            }
        ).addOnFailureListener(
            exception -> onException.accept(exception)
        );
    }

    public static void getItemImage(String itemId, String filename, Consumer<Bitmap> onSuccess, Consumer<Exception> onException) {
        getItemReference(itemId).child(filename).getBytes(IMAGE_SIZE_LIMIT).addOnSuccessListener(
            bytes -> {
                // Convert the byte array to a Bitmap
                Bitmap bmp = convertBytesToBitmap(bytes);
                onSuccess.accept(bmp);
            }
        ).addOnFailureListener(
            exception -> onException.accept(exception)
        );
    }

    public static void setImages(String itemId, List<Bitmap> images, Runnable onSuccess, Consumer<Exception> onException) {
        getItemReference(itemId).listAll()
                .addOnSuccessListener(
                        result -> {
                            int numDatabaseImages = result.getItems().size();
                            List<Task> tasks = new ArrayList<>();
                            // Overwrite existing files
                            for (int i = 0; i < images.size(); i++) {
                                tasks.add(getItemReference(itemId)
                                        .child(getImageFilename(i))
                                        .putBytes(convertBitmapToBytes(images.get(i))));
                            }
                            // Delete leftover files
                            for (int i = images.size(); i < numDatabaseImages; i++) {
                                tasks.add(getItemReference(itemId)
                                        .child(getImageFilename(i))
                                        .delete());
                            }
                            Tasks.whenAllSuccess(tasks)
                                    .addOnSuccessListener(_unused -> onSuccess.run())
                                    .addOnFailureListener(exception -> onException.accept(exception));
                        }
                )
                .addOnFailureListener(exception -> onException.accept(exception));
    }
}
