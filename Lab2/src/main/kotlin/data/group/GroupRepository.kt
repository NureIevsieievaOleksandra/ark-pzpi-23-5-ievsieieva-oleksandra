package ua.nure.data.group

interface GroupRepository {
    suspend fun getGroups(): List<Group>
    suspend fun create(group: Group): Group
    suspend fun delete(groupId: Int)
    suspend fun update(group: Group)
    suspend fun getGroupById(groupId: Int): Group?
}