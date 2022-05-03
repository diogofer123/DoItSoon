package com.example.doitsoon.di.modules

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.doitsoon.LaunchesApplication
import com.example.doitsoon.data.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): LaunchesApplication {
        return app as LaunchesApplication
    }

    @Provides
    @Singleton
    fun providesDatabase(app: Application) =
        Room.databaseBuilder(app,TaskDatabase::class.java,"tasks_database")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun providesTaskDao(db: TaskDatabase) = db.taskDao()
}