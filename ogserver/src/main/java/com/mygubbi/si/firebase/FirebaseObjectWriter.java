package com.mygubbi.si.firebase;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.mygubbi.catalog.ProductJson;

/**
 * Created by test on 21-01-2016.
 */
public class FirebaseObjectWriter
{
    private Firebase fbRef;

    public static void main(String[] args) throws Exception
    {
        ProductJson product = new ProductJson();
        new FirebaseObjectWriter().connectAndWrite(product);
    }

    public void connectAndWrite(ProductJson product)
    {
        System.out.println("Connecting to firebase ...");

        this.fbRef = new Firebase("https://sweltering-fire-6356.firebaseio.com");
        this.fbRef.authWithPassword("mygubbi.fbase@gmail.com", "!@#$VGYHasdf435", new Firebase.AuthResultHandler()
        {
            @Override
            public void onAuthenticated(AuthData authData)
            {
                System.out.println("Firebase authenticated.");
                updateProduct(product);
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError)
            {
                System.out.println("Firebase not authenticated.");
            }
        });
        try
        {
            Thread.sleep(10000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private void updateProduct(ProductJson product)
    {
        Firebase productsFbRef = this.fbRef.child("/products");
        System.out.println("FB ref:" + productsFbRef.getPath());

        try
        {
            Firebase productRef = productsFbRef.child(product.getCategory()).child(product.getSubCategory()).child(product.getProductId());
            System.out.println("Updating product to fb:" + productRef.getPath());
            new FirebaseObjectMapper().fromJsonToFirebase(productRef, product);
            System.out.println("Updated the product.");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
