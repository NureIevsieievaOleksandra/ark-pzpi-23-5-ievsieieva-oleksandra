package ua.nure.data.lamp

interface LampRepository {
    suspend fun getLamps(): List<Lamp>
    suspend fun create(lamp: Lamp): Lamp
    suspend fun delete(lampId: Int)
    suspend fun update(lamp: Lamp): Lamp?
    suspend fun getLampById(id: Int): Lamp?
}