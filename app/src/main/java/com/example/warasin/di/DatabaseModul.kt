package com.example.warasin

import android.content.Context
import androidx.room.Room
import com.example.warasin.data.AppDatabase
import com.example.warasin.data.dao.MedicineDao
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
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "medicine_database"
        ).build()
    }

    @Provides
    fun provideMedicineDao(database: AppDatabase): MedicineDao {
        return database.medicineDao()
    }
}