package consolegui

import consolegui.Utils.initFrame
import consolegui.Utils.showSystemInfo
import java.awt.BorderLayout
import java.awt.GridLayout
import java.awt.Label
import java.awt.Panel
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.util.*

class ButtonAsGui : Observable(), ActionListener {
    //from ActionListener
    override fun actionPerformed(e: ActionEvent) {
        setChanged()
        this.notifyObservers(e.actionCommand)
    }

    companion object {
 
        //Factory method
        fun createButtons(logo: String?, cmds: Array<String>?): ButtonAsGui {
            val cmd: Array<String>
            cmd = cmds ?: arrayOf("l", "r")
            showSystemInfo()
            val fr = initFrame(300, 300)
            fr!!.add(Label(logo), BorderLayout.NORTH)
            val p = Panel()
            p.layout = GridLayout(2, 3)
            fr.add(p, BorderLayout.CENTER)
            val button = ButtonAsGui()
            for (i in cmd.indices) ButtonBasic(p, cmd[i], button) //button is the listener
            return button
        }

        @JvmStatic
        fun main(args: Array<String>) {
            val cmds = arrayOf("a", "b")
            createButtons("test", cmds)
        }
    }
}