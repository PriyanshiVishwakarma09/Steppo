package com.example.seppo.di

import android.content.Context
import androidx.room.Room
import com.example.seppo.data.StepDao
import com.example.seppo.data.StepDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideStepDatabase(
        @ApplicationContext context: Context
    ): StepDatabase {
        return Room.databaseBuilder(
            context,
            StepDatabase::class.java,
            "step_db"
        ).build()
    }

    @Provides
    fun provideStepDao(db: StepDatabase): StepDao {
        return db.stepDao()
    }
}
