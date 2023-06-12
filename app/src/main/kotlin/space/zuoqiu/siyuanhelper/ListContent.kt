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

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.window.layout.DisplayFeature
import space.zuoqiu.siyuanhelper.R
import space.zuoqiu.siyuanhelper.data.Memo
import space.zuoqiu.siyuanhelper.ui.components.SubjectDetailAppBar
import space.zuoqiu.siyuanhelper.ui.components.MemoItem
import space.zuoqiu.siyuanhelper.ui.utils.HelperContentType
import space.zuoqiu.siyuanhelper.ui.utils.NavigationType
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import space.zuoqiu.siyuanhelper.data.Subject
import space.zuoqiu.siyuanhelper.ui.components.MemoList
import space.zuoqiu.siyuanhelper.ui.components.SubjectList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InboxScreen(
    contentType: HelperContentType,
    homeUIState: HelperHomeUIState,
    navigationType: NavigationType,
    displayFeatures: List<DisplayFeature>,
    closeDetailScreen: () -> Unit,
    navigateToDetail: (Long, HelperContentType) -> Unit,
    toggleSelectedSubject: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    /**
     * 从详情页面返回时应清除所有选择状态
     */
    LaunchedEffect(key1 = contentType) {
        if (contentType == HelperContentType.SINGLE_PANE && !homeUIState.isSubjectOnlyOpen) {
            closeDetailScreen()
        }
    }

    val subjectLazyListState = rememberLazyListState()

    // TODO: Show top app bar over full width of app when in multi-select mode

    if (contentType == HelperContentType.DUAL_PANE) {
        TwoPane(
            first = {
                SubjectList(
                    memos = homeUIState.memos,
                    subjects = homeUIState.subjects,
                    openedSubject = homeUIState.openedSubject,
                    selectedSubjectIds = homeUIState.selectedSubjects,
                    toggleSubjectSelection = toggleSelectedSubject,
                    lazyListState = subjectLazyListState,
                    navigateToDetail = navigateToDetail
                )
            },
            second = {
                val subject = homeUIState.openedSubject ?: homeUIState.defaultSubject
                if (subject != null) {
                    MemoList(
                        memos = homeUIState.openedMemos ?: homeUIState.defaultMemos ?: emptyList(),
                        subject = subject,
                        isFullScreen = false,
                    )
                }
            },
            strategy = HorizontalTwoPaneStrategy(splitFraction = 0.5f, gapWidth = 16.dp),
            displayFeatures = displayFeatures
        )
    } else {
        Box(modifier = modifier.fillMaxSize()) {
            SinglePaneContent(
                homeUIState = homeUIState,
                toggleSubjectSelection = toggleSelectedSubject,
                subjectLazyListState = subjectLazyListState,
                modifier = Modifier.fillMaxSize(),
                closeDetailScreen = closeDetailScreen,
                navigateToDetail = navigateToDetail
            )
            // When we have bottom navigation we show FAB at the bottom end.
            if (navigationType == NavigationType.BOTTOM_NAVIGATION) {
                /* 悬浮的添加按钮 */
                LargeFloatingActionButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .size(64.dp)
                        .align(Alignment.BottomEnd)
                        .padding(8.dp),
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                ) {
                    /* 添加按钮 */
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.add),
                    )
                }
            }
        }
    }
}

@Composable
fun SinglePaneContent(
    homeUIState: HelperHomeUIState,
    toggleSubjectSelection: (Long) -> Unit,
    subjectLazyListState: LazyListState,
    modifier: Modifier = Modifier,
    closeDetailScreen: () -> Unit,
    navigateToDetail: (Long, HelperContentType) -> Unit
) {
    if (homeUIState.openedSubject != null
        && homeUIState.openedMemos != null
        && homeUIState.isSubjectOnlyOpen
    ) {
        BackHandler {
            closeDetailScreen()
        }
        MemoList(
            memos = homeUIState.openedMemos,
            subject = homeUIState.openedSubject,
        ) {
            closeDetailScreen()
        }
    } else {
        SubjectList(
            memos = homeUIState.memos,
            subjects = homeUIState.subjects,
            openedSubject = homeUIState.openedSubject,
            selectedSubjectIds = homeUIState.selectedSubjects,
            toggleSubjectSelection = toggleSubjectSelection,
            lazyListState = subjectLazyListState,
            modifier = modifier,
            navigateToDetail = navigateToDetail
        )
    }
}
