import com.android.tools.idea.gradle.structure.configurables.android.modules.AndroidModuleRootConfigurable
import com.intellij.facet.FacetManager
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.ProjectCoreUtil
import com.intellij.openapi.ui.Messages
import org.jetbrains.android.facet.AndroidFacet
import org.jetbrains.android.facet.AndroidRootUtil
import org.jetbrains.android.sdk.AndroidSdkConfigurable
import org.jetbrains.android.util.AndroidResourceUtil
import org.jetbrains.android.util.AndroidUtils
import org.jetbrains.android.util.ResourceFileData
import org.jetbrains.kotlin.android.model.AndroidModuleInfoProvider
import org.jetbrains.kotlin.android.model.isAndroidModule

class TestAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val path: String = e.project?.let { project ->
            ModuleManager.getInstance(project).modules.find {
                it.isAndroidModule
            }?.let { androidModule ->
                FileEditorManager.getInstance(project)?.run {
                    AndroidFacet.getInstance(androidModule)?.run androidFacet@{

                        AndroidRootUtil.getResourceDirPath(this@androidFacet)
                    }
                }
            }
        } ?: "Not found"
        Messages.showInputDialog(path, "dfdfdf", Messages.getInformationIcon())
    }
}