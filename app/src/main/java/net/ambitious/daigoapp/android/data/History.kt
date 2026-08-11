package net.ambitious.daigoapp.android.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "histories")
data class History(
  @PrimaryKey(autoGenerate = true) var id: Long? = null,
  val text: String,
  val abbreviation: String,
  @ColumnInfo(name = "created_at") val createdAt: Long = Date().time
)
