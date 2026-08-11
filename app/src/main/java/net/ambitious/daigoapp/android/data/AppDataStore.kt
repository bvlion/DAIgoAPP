package net.ambitious.daigoapp.android.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppDataStore(context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
    private val settingsDataStore = context.dataStore

    val getViewMode: Flow<ViewMode> = settingsDataStore.data.map { pref ->
        ViewMode.values().first { it.type == (pref[VIEW_MODE_KEY] ?: 0) }
    }
    suspend fun setViewMode(mode: ViewMode) = settingsDataStore.edit {
        it[VIEW_MODE_KEY] = mode.type
    }

    companion object {
        private val VIEW_MODE_KEY = intPreferencesKey("view_mode")

        private var dataStore: AppDataStore? = null
        fun getDataStore(context: Context) = dataStore ?: AppDataStore(context).also { dataStore = it }
    }

    enum class ViewMode(val type: Int) {
        DEFAULT(0),
        LIGHT(1),
        DARK(2)
    }
}