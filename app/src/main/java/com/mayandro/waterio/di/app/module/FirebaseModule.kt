package com.mayandro.waterio.di.app.module

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mayandro.waterio.di.app.scope.AppScoped
import dagger.Module
import dagger.Provides

@Module
class FirebaseModule {

    @Provides
    @AppScoped
    internal fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @AppScoped
    internal fun provideFirebaseUser(firebaseAuth: FirebaseAuth): FirebaseUser? {
        return firebaseAuth.currentUser
    }
}