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

package space.zuoqiu.siyuanhelper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.adaptive.calculateDisplayFeatures

import space.zuoqiu.siyuanhelper.data.local.LocalMemosDataProvider
import space.zuoqiu.siyuanhelper.data.local.LocalSubjectDataProvider
import space.zuoqiu.siyuanhelper.ui.App
import space.zuoqiu.siyuanhelper.ui.HelperHomeUIState
import space.zuoqiu.siyuanhelper.ui.HomeViewModel
import space.zuoqiu.siyuanhelper.ui.theme.HelperTheme

class MainActivity : ComponentActivity() {

    private val viewModel: HomeViewModel by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelperTheme {
                val windowSize = calculateWindowSizeClass(this)
                val displayFeatures = calculateDisplayFeatures(this)
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                App(
                    windowSize = windowSize,
                    displayFeatures = displayFeatures,
                    helperHomeUIState = uiState,
                    closeDetailScreen = {
                        viewModel.closeDetailScreen()
                    },
                    navigateToDetail = { subjectId, pane ->
                        viewModel.setOpenedSubject(subjectId, pane)
                    },
                    toggleSelectedSubject = { subjectId ->
                        viewModel.toggleSelectedSubject(subjectId)
                    }
                )
            }
        }
    }
}

/* 移动设备 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true)
@Composable
fun HelperAppPreview() {
    HelperTheme {
        App(
            helperHomeUIState = HelperHomeUIState(
                subjects = LocalSubjectDataProvider.allSubjects,
                memos = LocalMemosDataProvider.allMemos,
            ),
            windowSize = WindowSizeClass.calculateFromSize(DpSize(400.dp, 900.dp)),
            displayFeatures = emptyList(),
        )
    }
}

/* 平板设备-横屏 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 800, heightDp = 600)
@Composable
fun HelperAppPreviewTablet() {
    HelperTheme {
        App(
            helperHomeUIState = HelperHomeUIState(
                subjects = LocalSubjectDataProvider.allSubjects,
                memos = LocalMemosDataProvider.allMemos,
            ),
            windowSize = WindowSizeClass.calculateFromSize(DpSize(800.dp, 600.dp)),
            displayFeatures = emptyList(),
        )
    }
}

/* 平板设备-竖屏 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 600, heightDp = 800)
@Composable
fun HelperAppPreviewTabletPortrait() {
    HelperTheme {
        App(
            helperHomeUIState = HelperHomeUIState(
                subjects = LocalSubjectDataProvider.allSubjects,
                memos = LocalMemosDataProvider.allMemos,
            ),
            windowSize = WindowSizeClass.calculateFromSize(DpSize(600.dp, 800.dp)),
            displayFeatures = emptyList(),
        )
    }
}

/* 桌面设备-横屏 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 1080, heightDp = 720)
@Composable
fun HelperAppPreviewDesktop() {
    HelperTheme {
        App(
            helperHomeUIState = HelperHomeUIState(
                subjects = LocalSubjectDataProvider.allSubjects,
                memos = LocalMemosDataProvider.allMemos,
            ),
            windowSize = WindowSizeClass.calculateFromSize(DpSize(1080.dp, 720.dp)),
            displayFeatures = emptyList(),
        )
    }
}

/* 桌面设备-竖屏 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 720, heightDp = 1080)
@Composable
fun HelperAppPreviewDesktopPortrait() {
    HelperTheme {
        App(
            helperHomeUIState = HelperHomeUIState(
                subjects = LocalSubjectDataProvider.allSubjects,
                memos = LocalMemosDataProvider.allMemos,
            ),
            windowSize = WindowSizeClass.calculateFromSize(DpSize(720.dp, 1080.dp)),
            displayFeatures = emptyList(),
        )
    }
}
