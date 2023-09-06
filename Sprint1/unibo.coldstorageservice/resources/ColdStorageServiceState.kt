package resources

import com.google.gson.Gson

enum class ColdStorageServiceStateEnum {
    IDLE,
    SETUP,
    REQUESTEVALUATION,
    ACCEPTREQUEST,
    REJECTREQUEST,
    HANDLETICKETEXPIRED,
    DROPOUT,
    CHARGED,
    TICKETEVALUATION
}

class ColdStorageServiceState (private var currentState : ColdStorageServiceStateEnum = ColdStorageServiceStateEnum.IDLE) {

}