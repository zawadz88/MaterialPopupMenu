package com.github.zawadz88.materialpopupmenu

import com.github.zawadz88.materialpopupmenu.internal.MaterialRecyclerViewPopupWindow
import com.github.zawadz88.materialpopupmenu.internal.PopupMenuAdapter
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.junit.ArchUnitRunner
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import org.junit.runner.RunWith

@RunWith(ArchUnitRunner::class)
@AnalyzeClasses(packages = ["com.github.zawadz88.materialpopupmenu"])
class ArchitectureTest {

    @ArchTest
    val internal_api_restriction = classes()
        .that().resideInAPackage("com.github.zawadz88.materialpopupmenu.internal")
        .should()
        .onlyBeAccessed().byClassesThat().resideInAPackage("com.github.zawadz88.materialpopupmenu.internal")
        .orShould()
        .onlyBeAccessed().byClassesThat().belongToAnyOf(MaterialPopupMenu::class.java, MaterialRecyclerViewPopupWindow::class.java, PopupMenuAdapter::class.java)
}
