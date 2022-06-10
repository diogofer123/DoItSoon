package com.example.doitsoon.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

enum class SortOrder{
    BY_DATE,
    BY_NAME_DESC
}

private const val TAG = "PreferencesManager"

data class FilterPreferences(val sortOrder: SortOrder,val hideCompleted: Boolean)

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {

    //Better to use JetPack datastore to store this kind of preferences;
    private val Context.dataStore by preferencesDataStore("user_preferences")

    val preferencesFlow = context.dataStore.data
        .catch { exception ->
            if(exception is IOException){
                Log.e(TAG, "Error reading preferences: ", )
                emit(emptyPreferences())
            }
            else{
                throw exception
            }
        }
        .map { preferences ->
            val sortOrder = SortOrder.valueOf(preferences[PreferencesKey.SORT_ORDER] ?: SortOrder.BY_DATE.name)
            val hideCompleted = preferences[PreferencesKey.HIDE_COMPLETE] ?: false

        FilterPreferences(sortOrder,hideCompleted)
    }

    suspend fun updateSortOrder(sortOrder: SortOrder,context: Context){
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.SORT_ORDER] = sortOrder.name
        }
    }

    suspend fun updateHideCompleted(hideCompleted: Boolean,context: Context){
        context.dataStore.edit { preferences ->
            preferences[PreferencesKey.HIDE_COMPLETE] = hideCompleted
        }
    }


    private object PreferencesKey {
        val SORT_ORDER = stringPreferencesKey("sort_order")
        val HIDE_COMPLETE = booleanPreferencesKey("hide_completed")
    }
}
