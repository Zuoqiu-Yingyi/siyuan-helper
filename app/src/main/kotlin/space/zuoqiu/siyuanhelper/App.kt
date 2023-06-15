// Copyright (C) 2023 Zuoqiu Yingyi
// 
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as
// published by the Free Software Foundation, either version 3 of the
// License, or (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Affero General Public License for more details.
// 
// You should have received a copy of the GNU Affero General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.

package space.zuoqiu.siyuanhelper.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.DisplayFeature
import androidx.window.layout.FoldingFeature
import space.zuoqiu.siyuanhelper.ui.navigation.ModalNavigationDrawerContent
import space.zuoqiu.siyuanhelper.ui.navigation.PermanentNavigationDrawerContent
import space.zuoqiu.siyuanhelper.ui.navigation.BottomNavigationBar
import space.zuoqiu.siyuanhelper.ui.navigation.NavigationActions
import space.zuoqiu.siyuanhelper.ui.navigation.NavigationRail
import space.zuoqiu.siyuanhelper.ui.navigation.Route
import space.zuoqiu.siyuanhelper.ui.navigation.TopLevelDestination
import space.zuoqiu.siyuanhelper.ui.utils.DevicePosture
import space.zuoqiu.siyuanhelper.ui.utils.HelperContentType
import space.zuoqiu.siyuanhelper.ui.utils.HelperNavigationContentPosition
import space.zuoqiu.siyuanhelper.ui.utils.NavigationType
import space.zuoqiu.siyuanhelper.ui.utils.isBookPosture
import space.zuoqiu.siyuanhelper.ui.utils.isSeparating
import kotlinx.coroutines.launch

/**
 * 应用程序入口
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    windowSize: WindowSizeClass,
    displayFeatures: List<DisplayFeature>,
    helperHomeUIState: HelperHomeUIState,
    closeDetailScreen: () -> Unit = {},
    navigateToDetail: (Long, HelperContentType) -> Unit = { _, _ -> },
    toggleSelectedSubject: (Long) -> Unit = { }
) {
    /**
     * 根据窗口大小和设备的折叠状态选择导航类型和内容类型
     */
    val navigationType: NavigationType
    val contentType: HelperContentType

    /**
     * 获取折叠屏的屏幕折叠状态
     * 折叠屏在半折状态时, 应避免内容显示在折痕/铰链
     */
    val foldingFeature = displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()

    val foldingDevicePosture = when {
        isBookPosture(foldingFeature) ->
            DevicePosture.BookPosture(foldingFeature.bounds)

        isSeparating(foldingFeature) ->
            DevicePosture.Separating(foldingFeature.bounds, foldingFeature.orientation)

        else -> DevicePosture.NormalPosture
    }

    /* 根据窗口宽度设置界面布局类型 */
    when (windowSize.widthSizeClass) {
        /* 小型设备 */
        WindowWidthSizeClass.Compact -> {
            navigationType = NavigationType.BOTTOM_NAVIGATION
            contentType = HelperContentType.SINGLE_PANE
        }

        /* 中等设备 */
        WindowWidthSizeClass.Medium -> {
            navigationType = NavigationType.NAVIGATION_RAIL
            contentType = if (foldingDevicePosture != DevicePosture.NormalPosture) {
                HelperContentType.DUAL_PANE
            } else {
                HelperContentType.SINGLE_PANE
            }
        }

        /* 大型设备 */
        WindowWidthSizeClass.Expanded -> {
            navigationType = if (foldingDevicePosture is DevicePosture.BookPosture) {
                NavigationType.NAVIGATION_RAIL
            } else {
                NavigationType.PERMANENT_NAVIGATION_DRAWER
            }
            contentType = HelperContentType.DUAL_PANE
        }

        /* 其他设备 */
        else -> {
            navigationType = NavigationType.BOTTOM_NAVIGATION
            contentType = HelperContentType.SINGLE_PANE
        }
    }

    /**
     * 导航栏的内容可以放在
     * - 页面底部
     * - 侧边栏顶部
     * - 侧边栏中间
     */
    val navigationContentPosition = when (windowSize.heightSizeClass) {
        WindowHeightSizeClass.Compact -> {
            HelperNavigationContentPosition.TOP
        }
        WindowHeightSizeClass.Medium,
        WindowHeightSizeClass.Expanded -> {
            HelperNavigationContentPosition.CENTER
        }
        else -> {
            HelperNavigationContentPosition.TOP
        }
    }

    HelperNavigationWrapper(
        navigationType = navigationType,
        contentType = contentType,
        displayFeatures = displayFeatures,
        navigationContentPosition = navigationContentPosition,
        helperHomeUIState = helperHomeUIState,
        closeDetailScreen = closeDetailScreen,
        navigateToDetail = navigateToDetail,
        toggleSelectedEmail = toggleSelectedSubject
    )
}

/**
 * 页面导航
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HelperNavigationWrapper(
    navigationType: NavigationType,
    contentType: HelperContentType,
    displayFeatures: List<DisplayFeature>,
    navigationContentPosition: HelperNavigationContentPosition,
    helperHomeUIState: HelperHomeUIState,
    closeDetailScreen: () -> Unit,
    navigateToDetail: (Long, HelperContentType) -> Unit,
    toggleSelectedEmail: (Long) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        NavigationActions(navController)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination =
        navBackStackEntry?.destination?.route ?: Route.INBOX

    if (navigationType == NavigationType.PERMANENT_NAVIGATION_DRAWER) {
        // TODO check on custom width of PermanentNavigationDrawer: b/232495216
        /* 常显导航栏抽屉 */
        PermanentNavigationDrawer(drawerContent = {
            /* 常显导航栏抽屉内容 */
            PermanentNavigationDrawerContent(
                selectedDestination = selectedDestination,
                navigationContentPosition = navigationContentPosition,
                navigateToTopLevelDestination = navigationActions::navigateTo,
            )
        }) {
            /* 应用内容 */
            HelperAppContent(
                navigationType = navigationType,
                contentType = contentType,
                displayFeatures = displayFeatures,
                navigationContentPosition = navigationContentPosition,
                helperHomeUIState = helperHomeUIState,
                navController = navController,
                selectedDestination = selectedDestination,
                navigateToTopLevelDestination = navigationActions::navigateTo,
                closeDetailScreen = closeDetailScreen,
                navigateToDetail = navigateToDetail,
                toggleSelectedEmail = toggleSelectedEmail
            )
        }
    } else {
        /* 模态导航栏抽屉 */
        ModalNavigationDrawer(
            drawerContent = {
                /* 模态导航栏抽屉内容 */
                ModalNavigationDrawerContent(
                    selectedDestination = selectedDestination,
                    navigationContentPosition = navigationContentPosition,
                    navigateToTopLevelDestination = navigationActions::navigateTo,
                    onDrawerClicked = {
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
            },
            drawerState = drawerState
        ) {
            /* 应用内容 */
            HelperAppContent(
                navigationType = navigationType,
                contentType = contentType,
                displayFeatures = displayFeatures,
                navigationContentPosition = navigationContentPosition,
                helperHomeUIState = helperHomeUIState,
                navController = navController,
                selectedDestination = selectedDestination,
                navigateToTopLevelDestination = navigationActions::navigateTo,
                closeDetailScreen = closeDetailScreen,
                navigateToDetail = navigateToDetail,
                toggleSelectedEmail = toggleSelectedEmail
            ) {
                scope.launch {
                    drawerState.open()
                }
            }
        }
    }
}

/**
 * 应用内容
 */
@Composable
fun HelperAppContent(
    modifier: Modifier = Modifier,
    navigationType: NavigationType,
    contentType: HelperContentType,
    displayFeatures: List<DisplayFeature>,
    navigationContentPosition: HelperNavigationContentPosition,
    helperHomeUIState: HelperHomeUIState,
    navController: NavHostController,
    selectedDestination: String,
    navigateToTopLevelDestination: (TopLevelDestination) -> Unit,
    closeDetailScreen: () -> Unit,
    navigateToDetail: (Long, HelperContentType) -> Unit,
    toggleSelectedEmail: (Long) -> Unit,
    onDrawerClicked: () -> Unit = {}
) {
    Row(modifier = modifier.fillMaxSize()) {
        AnimatedVisibility(visible = navigationType == NavigationType.NAVIGATION_RAIL) {
            NavigationRail(
                selectedDestination = selectedDestination,
                navigationContentPosition = navigationContentPosition,
                navigateToTopLevelDestination = navigateToTopLevelDestination,
                onDrawerClicked = onDrawerClicked,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            HelperNavHost(
                navController = navController,
                contentType = contentType,
                displayFeatures = displayFeatures,
                helperHomeUIState = helperHomeUIState,
                navigationType = navigationType,
                closeDetailScreen = closeDetailScreen,
                navigateToDetail = navigateToDetail,
                toggleSelectedEmail = toggleSelectedEmail,
                modifier = Modifier.weight(1f),
            )
            AnimatedVisibility(visible = navigationType == NavigationType.BOTTOM_NAVIGATION) {
                /* 底部导航栏 */
                BottomNavigationBar(
                    selectedDestination = selectedDestination,
                    navigateToTopLevelDestination = navigateToTopLevelDestination
                )
            }
        }
    }
}

@Composable
private fun HelperNavHost(
    navController: NavHostController,
    contentType: HelperContentType,
    displayFeatures: List<DisplayFeature>,
    helperHomeUIState: HelperHomeUIState,
    navigationType: NavigationType,
    closeDetailScreen: () -> Unit,
    navigateToDetail: (Long, HelperContentType) -> Unit,
    toggleSelectedEmail: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Route.INBOX,
    ) {
        composable(Route.INBOX) {
            /* 收集箱页面 */
            InboxScreen(
                contentType = contentType,
                homeUIState = helperHomeUIState,
                navigationType = navigationType,
                displayFeatures = displayFeatures,
                closeDetailScreen = closeDetailScreen,
                navigateToDetail = navigateToDetail,
                toggleSelectedSubject = toggleSelectedEmail
            )
        }
        composable(Route.ARCHIVE) {
            EmptyComingSoon()
        }
        composable(Route.TAG) {
            EmptyComingSoon()
        }
        composable(Route.SETTING) {
            EmptyComingSoon()
        }
    }
}
