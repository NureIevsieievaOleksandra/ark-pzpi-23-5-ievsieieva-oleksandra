package ua.nure.data.iotsocket

interface IotStatisticsRepository {
    suspend fun create(iotStatistics: IotStatistics): IotStatistics
    suspend fun get(): Map<Int, IotStatisticsReview>
}