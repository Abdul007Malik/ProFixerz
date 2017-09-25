package com.profixerz.util;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuthException;

/**
 * Created by shawn on 18/9/17.
 *
 *
 * */

public final class FailureHandler implements OnFailureListener{

    @Override
    public void onFailure(@NonNull Exception e) {

        if(e instanceof FirebaseAuthException){

            String errorCode = ((FirebaseAuthException) e).getErrorCode();

        }
        else if(e instanceof FirebaseNetworkException){

        }
        else {

        }
    }

}
