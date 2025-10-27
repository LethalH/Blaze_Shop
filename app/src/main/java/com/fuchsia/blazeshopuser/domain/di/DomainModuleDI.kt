package com.fuchsia.blazeshopuser.domain.di

import com.fuchsia.blazeshopuser.data.repoImp.RepoImp
import com.fuchsia.blazeshopuser.domain.repo.Repo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DomainModuleDI {

    @Provides
    fun provideRepo(
        auth: FirebaseAuth,
        fireStore: FirebaseFirestore,

        ): Repo {
        return RepoImp(
            auth = auth,
            fireStore = fireStore,
            storage = FirebaseStorage.getInstance()
        )

    }
}