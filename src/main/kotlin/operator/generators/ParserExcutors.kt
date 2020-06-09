package operator.generators

import DimensRatio
import com.sun.jna.platform.win32.COM.Dispatch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.DimenDataModel
import operator.Excutors
import operator.ExportDimenXML
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.ByteArrayInputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.Executors
import javax.xml.parsers.DocumentBuilderFactory


object ParserExcutors : Excutors, MyOperator<File>() {
    private const val PATTERN_DP: String = "dp"
    private const val PATTERN_PX: String = "px"
    private const val ELEMENT_NAME_DIMEN = "dimen"
    private const val ATTR_NAME_NAME = "name"

    private lateinit var inputStream: InputStream
    private lateinit var parentPath: String
    private var callback: Callback? = null

    private val parseArray = ArrayList<DimenDataModel>()

    fun setCallback(callback: Callback) {
        ParserExcutors.callback = callback
    }

    override fun initialize(doing: File) {
        inputStream = ByteArrayInputStream(doing.readBytes())
        parentPath = doing.parentFile.parentFile.path
    }

    override fun start() {
        Executors.newSingleThreadExecutor().apply {
            execute {
                parserXML(inputStream)
                    ?.run {
                        getElementsByTagName(ELEMENT_NAME_DIMEN).run {
                            (0..length).map { item(it) }.filterIsInstance<Element>().forEach {
                                with(it) {
                                    getAttribute(ATTR_NAME_NAME).run {
                                        println("$this : ${firstChild.textContent}")
                                        parseArray.add(DimenDataModel(this, firstChild.textContent))
                                    }
                                }
                            }

                        }
                        buildXMLFile(documentElement.nodeName)
                        return@execute
                    }
                println("Error parser")
            }
        }.shutdown()
    }

    override fun finish() {
        inputStream.close()
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                callback?.onCreateFinish()

            }
        }
    }

    private fun buildXMLFile(docNodeName: String) {
        for (ratio: DimensRatio in DimensRatio.values()) {
            ExportDimenXML(
                parentPath,
                ratio.pathName()
            ).run {
                createRootElement(docNodeName).let {
                    for (model: DimenDataModel in parseArray) {
                        createChildNode(
                            it,
                            ELEMENT_NAME_DIMEN,
                            ATTR_NAME_NAME, model.name,
                            calculatorToString(
                                model.value,
                                ratio.getRatio()
                            )
                        )
                    }
                }
                exportXMLFile()
            }
        }
        finish()
    }

    @Throws(IOException::class)
    private fun parserXML(inputStream: InputStream): Document? =
        DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream)

    private fun calculatorToString(objectString: String, ratio: Float): String =
        if (objectString.contains(PATTERN_PX)) {
            println("$objectString : contains px")
            objectString
        } else {
            (objectString.replace(PATTERN_DP, "").toFloat() * ratio).run {
                (Math.round(this * 100.0) / 100.0).toString().plus(PATTERN_DP)
            }
        }


    @FunctionalInterface
    interface Callback {
        fun onCreateFinish()
    }
}