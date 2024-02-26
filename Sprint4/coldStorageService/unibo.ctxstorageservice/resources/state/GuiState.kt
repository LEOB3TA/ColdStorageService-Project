package state
import com.google.gson.Gson
import resources.TicketEvaluationResponse

// Tutti i dati necessari per update delle gui
data class GuiState(
    private val MAXW :Double = 100.0,
    private var CurrW :Double = 0.0,
    //private val xLength: Int = 7, //contando da 0
    //private val yLength: Int = 5,
    private var TicketStatus: TicketEvaluationResponse = TicketEvaluationResponse.VALID,
    private var rejected : Int = 0,

    private var pos : TTPosition = state.TTPosition.HOME ,
    private var act : CurrStateTrolley = state.CurrStateTrolley.IDLE,
    private var ticketN: Int = 0, // 0 is accepted

    //precise position
    private var ttPos : IntArray = intArrayOf(0, 0)
){
    fun setCurrW(weight: Double){
        CurrW += weight
    }
    fun getCurrW():Double{
        return CurrW
    }

    fun setTicketStatus(status : TicketEvaluationResponse){
        TicketStatus = status
    }
    fun getTicketStatus():TicketEvaluationResponse{
        return TicketStatus
    }

    fun setRejected(){
        rejected+=1
    }
    fun getReejected():Int{
        return rejected
    }

    fun setPos(newP : TTPosition){
        pos = newP
    }
    fun getPos():TTPosition{
        return pos
    }
    fun setAct(newA : CurrStateTrolley){
        act = newA
    }
    fun getAct():CurrStateTrolley{
        return act
    }
    fun setTN(TN:Int){
        ticketN = TN
    }
    fun getTN():Int{
        return ticketN
    }

    fun getTTP(): IntArray{
        return ttPos
    }

    fun setTTP( newp: IntArray ){
        ttPos = newp
    }

    companion object{
        private val gson = Gson()
        fun fromJsonString(str : String) : GuiState{
            return gson.fromJson(str, GuiState::class.java)
        }
    }

    fun toJsonString() : String{
        return gson.toJson(this)
    }

}