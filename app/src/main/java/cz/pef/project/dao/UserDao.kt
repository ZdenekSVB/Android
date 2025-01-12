package cz.pef.project.dao

import android.util.Log
import androidx.room.*
import cz.pef.project.DB.PlantEntity
import cz.pef.project.DB.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlant(plant: PlantEntity): Long

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): UserEntity?

    @Query("SELECT * FROM plants WHERE userId = :userId")
    suspend fun getPlantsByUserId(userId: Int): List<PlantEntity>

    @Update
    suspend fun updateUser(user: UserEntity)

    @Update
    suspend fun updatePlant(plant: PlantEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Delete
    suspend fun deletePlant(plant: PlantEntity)

    @Query("SELECT * FROM users WHERE userName = :userName LIMIT 1")
    suspend fun getUserByUserName(userName: String): UserEntity?

    @Query("SELECT id FROM users WHERE userName = :userName")
    suspend fun getUserIdByUsername(userName: String): Int?

    @Query("SELECT * FROM plants WHERE name = :plantName")
    suspend fun getPlantByName(plantName: String): PlantEntity?

    @Query("UPDATE plants SET latitude = :latitude, longitude = :longitude WHERE id = :plantId")
    suspend fun updatePlantLocation(plantId: Int, latitude: Double, longitude: Double) {
        Log.d("DAO", "Updating plant $plantId with lat: $latitude, lng: $longitude")
    }


    @Query("SELECT * FROM plants WHERE id = :plantId")
    suspend fun getPlantById(plantId: Int): PlantEntity?
}
