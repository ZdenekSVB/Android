package cz.mendelu.pef.xsvobo.projekt.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class Card(var name: String){

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

}
