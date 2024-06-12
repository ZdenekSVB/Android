package cz.mendelu.pef.xsvobo.projekt.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "cards",foreignKeys = [
    ForeignKey(entity=Set::class, parentColumns = ["id"], childColumns = ["setsId"], onDelete = CASCADE, onUpdate = CASCADE)
])
data class Card(var name: String){

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
    var question: String? = null
    var answer: String? = null
    var setsId: Long? = null

}
/*
@Entity(tableName = "sets")
data class Set(var name: String){

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

 */