package ru.tanec.cookhelper.core.constants.status

object TopicStatus {
    const val SUCCESS: Int = 400
    const val EXCEPTION: Int = 499
    const val TOPIC_NOT_FOUND: Int = 401
    const val WRONG_DATA: Int = 402
    const val PARAMETER_MISSED: Int = 404
    const val TOPIC_DELETED: Int = 405
    const val PERMISSION_DENIED: Int = 406
}