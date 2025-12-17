package ua.nure.data.user

interface UserRepository {
    suspend fun getUserById(userId: Int): User?
    suspend fun getUserByUserName(name: String): User?
    suspend fun insertUser(user: User): User?
    suspend fun updateUser(userId: Int, role: Role): User?
    suspend fun deleteUserById(userId: Int)
    suspend fun getAllUsers(): List<User>
}