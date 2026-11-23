package com.ncjappdev.wanderline.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.ncjappdev.wanderline.data.WanderLineDao
import com.ncjappdev.wanderline.data.WanderLineDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    // Le enseñamos a Hilt cómo construir la base de datos
    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): WanderLineDatabase {
        return Room.databaseBuilder(
            context,
            WanderLineDatabase::class.java,
            "wanderline_database"
        )
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    // Le enseñamos a Hilt cómo sacar el DAO de esa base de datos
    @Provides
    @Singleton
    fun provideWanderLineDao(database: WanderLineDatabase): WanderLineDao {
        return database.dao
    }

}