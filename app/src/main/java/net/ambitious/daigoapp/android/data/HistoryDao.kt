package net.ambitious.daigoapp.android.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoryDao {
  @Query("select * from histories order by created_at desc limit 50")
  fun getAll(): List<History>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(history: History)
}