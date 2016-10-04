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
//    private static final String FB_URL = "https://sweltering-fire-6356.firebaseio.com";
//    private static final String FB_LOGIN = "mygubbi.fbase@gmail.com";
//    private static final String FB_PASSWORD = "!@#$VGYHasdf435";

//    private static final String FB_URL = "https://mygubbi-cep.firebaseio.com";
//    private static final String FB_LOGIN = "mygubbi.uat@gmail.com";
//    private static final String FB_PASSWORD = "uat@mygubbi";

    private static final String FB_URL = "https://dazzling-heat-1615.firebaseio.com";
    private static final String FB_LOGIN = "sunil@bizosys.com";
    private static final String FB_PASSWORD = "qwert12";

    private Firebase fbRef;

    public static void main(String[] args) throws Exception
    {
        ProductJson product = new ProductJson();
        new FirebaseObjectWriter().connectAndWrite();
    }

    public void connectAndWrite()
    {
        System.out.println("Connecting to firebase db: " + FB_URL + " with user: " + FB_LOGIN);

        this.fbRef = new Firebase(FB_URL);
        this.fbRef.authWithPassword(FB_LOGIN, FB_PASSWORD, new Firebase.AuthResultHandler()
        {
            @Override
            public void onAuthenticated(AuthData authData)
            {
                System.out.println("Firebase authenticated.");
                //updateProduct(product);
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError)
            {
                System.out.println("Firebase not authenticated." + firebaseError.toString());
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
        System.exit(0);
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
