package cz.mendelu.pef.xsvobo.projekt.model

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "sets")
data class Set(var name: String){

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
    var latest: Int = 0
    var cardsCount: Int = 0
    var icon: String? =null
}
