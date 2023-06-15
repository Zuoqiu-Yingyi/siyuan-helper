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

package space.zuoqiu.siyuanhelper.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import space.zuoqiu.siyuanhelper.R
import space.zuoqiu.siyuanhelper.data.Subject
import space.zuoqiu.siyuanhelper.data.local.LocalSubjectDataProvider
import space.zuoqiu.siyuanhelper.ui.theme.HelperTheme
import space.zuoqiu.siyuanhelper.ui.utils.dateTimeFormatter

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun SubjectItem(
    subject: Subject,
    navigateToDetail: (Long) -> Unit,
    toggleSelection: (Long) -> Unit,
    toggleDefault: (Long) -> Unit,
    modifier: Modifier = Modifier,
    isOpened: Boolean = false,
    isSelected: Boolean = false,
) {
    var editing by rememberSaveable { mutableStateOf(false) } // 是否处于编辑状态
    val title = rememberSaveable { mutableStateOf(subject.title) } // 标题
    val description = rememberSaveable { mutableStateOf(subject.description) } // 描述

    /* 切换编辑状态 */
    val toggle = {
        editing = !editing
        if (!editing) {
            subject.title = title.value // 标题
            subject.description = description.value // 描述
        }
    }

    Card(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .semantics { selected = isSelected }
            .clip(CardDefaults.shape)
            .combinedClickable(
                /* 长按选择 */
                onLongClick = { toggleSelection(subject.id) },
                /* 双击编辑 */
                onDoubleClick = toggle,
                /* 点击查看对应的备忘录列表 */
                onClick = { navigateToDetail(subject.id) },
            )
            .clip(CardDefaults.shape),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
            else if (isOpened) MaterialTheme.colorScheme.secondaryContainer
            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        val roundedCornerShape = RoundedCornerShape(8.dp) // 圆角

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                // val clickModifier = Modifier.clickable(
                //     interactionSource = remember { MutableInteractionSource() },
                //     indication = null
                // ) { toggleSelection(subject.id) }
                // AnimatedContent(targetState = isSelected, label = "avatar") { selected ->
                //     if (selected) {
                //         SelectedProfileImage(clickModifier)
                //     } else {
                //         ProfileImage(
                //             memo.subject.avatar,
                //             memo.subject.fullName,
                //             clickModifier
                //         )
                //     }
                // }

                Column(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    /* 标题 */
                    Crossfade(targetState = editing) {
                        when (it) {
                            true -> {
                                /**
                                 * 标题编辑框
                                 * REF: https://developer.android.com/jetpack/compose/text/user-input
                                 */
                                OutlinedTextField(
                                    value = title.value,
                                    onValueChange = { value ->
                                        title.value = value
                                    },
                                    modifier = Modifier.absolutePadding(
                                        right = 16.dp,
                                        bottom = 4.dp,
                                    ),
                                    label = {
                                        Text(
                                            text = stringResource(id = R.string.title),
                                            style = MaterialTheme.typography.labelSmall,
                                        )
                                    },
                                    textStyle = MaterialTheme.typography.titleMedium,
                                    shape = roundedCornerShape,
                                )
                            }

                            false -> {
                                /* 标题文本 */
                                Text(
                                    text = title.value,
                                    style = MaterialTheme.typography.titleMedium,
                                )
                            }
                        }
                    }

                    /* 更新时间 */
                    Text(
                        text = subject.updated.format(dateTimeFormatter),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                var defaultSubject by rememberSaveable { mutableStateOf(subject.isDefault) } // 是否为默认主题
                /* 置顶按钮(设置为默认主题) */
                IconButton(
                    onClick = {
                        defaultSubject = !defaultSubject
                        toggleDefault(subject.id)
                    },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    Icon(
                        imageVector = Icons.Default.PushPin,
                        contentDescription = stringResource(id = R.string.top),
                        modifier = if (defaultSubject) Modifier.alpha(1f) else Modifier.alpha(0.5f),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            /* 描述 */
            Crossfade(targetState = editing) { it ->
                when (it) {
                    true -> {
                        /* 描述编辑框 */
                        OutlinedTextField(
                            value = description.value,
                            onValueChange = { value ->
                                description.value = value
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = {
                                Text(
                                    text = stringResource(id = R.string.description),
                                    style = MaterialTheme.typography.labelSmall,
                                )
                            },
                            textStyle = MaterialTheme.typography.bodyMedium,
                            shape = roundedCornerShape,
                        )
                    }

                    false -> {
                        /* 描述 */
                        Text(
                            text = description.value,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SelectedProfileImage(modifier: Modifier = Modifier) {
    Box(
        modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Icon(
            Icons.Default.Check,
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.Center),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SubjectItemPreview() {
    HelperTheme {
        SubjectItem(
            subject = LocalSubjectDataProvider.getDefaultSubject(),
            navigateToDetail = {},
            toggleSelection = {},
            toggleDefault = {},
        )
    }
}
