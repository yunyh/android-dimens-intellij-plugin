package ui

import com.intellij.openapi.ui.DialogWrapper
import operator.generators.ParserExcutors
import java.awt.FlowLayout
import java.awt.event.ActionEvent
import java.io.File
import java.text.DecimalFormat
import javax.swing.*


class CustomModelDialog : DialogWrapper(true) {

    init {
        title = "Dimensions"
        init()
    }

    private var path: String? = null

    fun setFilePath(path: String): CustomModelDialog {
        this.path = path
        return this
    }

    override fun createCenterPanel(): JComponent? {
        return JPanel().apply rootPanel@{
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            DimensRatio.values().forEach {

                add(CreateInputRow(it.name, DecimalFormat("0.#").format(it.getRatio())))
            }
            /*  add(CreateInputRow("mdpi"))
              add(CreateInputRow("hdpi"))
              add(CreateInputRow("xdpi"))
              add(CreateInputRow("xxdpi"))
              add(CreateInputRow("xxxdpi"))*/
            /* add(JButton("Add row").apply {
                 this.addActionListener {
                     Messages.showInputDialog(
                         "Input dimens",
                         "",
                         Messages.getQuestionIcon(),
                         null,
                         object : InputValidator {
                             override fun checkInput(inputString: String?): Boolean = true

                             override fun canClose(inputString: String?): Boolean {
                                 if (inputString.isNullOrEmpty()) {
                                     return true
                                 }
                                 this@rootPanel.add(CreateInputRow(inputString))
                                 this@rootPanel.revalidate()
                                 return true
                             }

                         })
                 }
             })*/
        }.also {
            isAutoAdjustable = true
        }
    }

    @Suppress("FunctionName")
    private fun CreateInputRow(rowTitle: String, defaultPercent: String = "1"): JPanel = JPanel(FlowLayout()).apply {
        add(JLabel(rowTitle))
        add(JTextField(defaultPercent, 20))
    }

    override fun getOKAction(): Action {
        return object : OkAction() {
            override fun actionPerformed(e: ActionEvent?) {
                path?.let {
                    ParserExcutors.initialize(File(it))
                    ParserExcutors.start()
                }
            }
        }
    }

}