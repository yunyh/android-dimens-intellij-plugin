package action

import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.ui.Messages
import ui.CustomModelDialog

class ConversionPopupAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
       /* Messages.showMessageDialog(
            "${e.dataContext.getData(CommonDataKeys.VIRTUAL_FILE)?.name}",
            "Test",
            Messages.getInformationIcon()
        )*/
          CustomModelDialog().setFilePath(e.dataContext.getData(CommonDataKeys.VIRTUAL_FILE)?.path ?: return).showAndGet()
        e.project?.run {

            /* Messages.showMessageDialog(
                 "file path : ${e.dataContext.getData(CommonDataKeys.VIRTUAL_FILE)?.path}\n" +
                         "file type : ${e.dataContext.getData(CommonDataKeys.VIRTUAL_FILE)?.fileType?.name}",
                 "File Message",
                 Messages.getInformationIcon()
             )*/
            // PropertiesComponent.getInstance(this).set
        }
    }

    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.isVisible = e.dataContext.getData(CommonDataKeys.VIRTUAL_FILE)?.run {
            fileType.name.equals("XML", true) && name.equals("dimens.xml", true)
        } ?: false
    }
}