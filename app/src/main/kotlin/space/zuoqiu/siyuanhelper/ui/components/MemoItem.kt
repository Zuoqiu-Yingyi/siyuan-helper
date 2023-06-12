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
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.meetup.twain.MarkdownEditor
import com.meetup.twain.MarkdownText
import space.zuoqiu.siyuanhelper.R
import space.zuoqiu.siyuanhelper.data.Memo
import space.zuoqiu.siyuanhelper.ui.utils.dateTimeFormatter
import java.time.ZonedDateTime

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class,
)
@Composable
fun MemoItem(
    memo: Memo,
    modifier: Modifier = Modifier,
) {
    /* markdown 文本 */
    val markdown = rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(memo.content))
    }
    var editing by remember { mutableStateOf(false) } // 是否处于编辑状态
    var title = remember { mutableStateOf(memo.title) } // 标题
    var updated = remember { mutableStateOf(memo.updated) } // 更新时间
    var description = remember { mutableStateOf(memo.description) } // 描述

    /* 切换编辑状态 */
    val toggle = {
        editing = !editing
        if (!editing) {
            updated.value = ZonedDateTime.now() // 更新更改日期

            memo.title = title.value // 标题
            memo.updated = updated.value // 更新时间
            memo.description = description.value // 描述
            memo.content = markdown.value.text // 内容

            memo.subject.updated = updated.value // 主题更新时间
        }
    }

    Card(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .combinedClickable(
                /* 双击切换编辑状态 */
                onDoubleClick = toggle,
                onClick = { /*TODO*/ },
            ),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        val roundedCornerShape = RoundedCornerShape(8.dp) // 圆角

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                // ProfileImage(
                //     drawableResource = memo.subject.avatar,
                //     description = memo.subject.fullName,
                // )
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
                                    modifier = Modifier.padding(bottom = 4.dp),
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
                        text = updated.value.format(dateTimeFormatter),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                /* 编辑/保存按钮 */
                IconButton(
                    onClick = toggle,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(color = MaterialTheme.colorScheme.background),
                ) {
                    Crossfade(targetState = editing) {
                        when (it) {
                            true -> {
                                Icon(
                                    imageVector = Icons.Default.Save,
                                    contentDescription = stringResource(id = R.string.save),
                                )
                            }

                            false -> {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = stringResource(id = R.string.edit),
                                )
                            }
                        }
                    }
                }

                /* 收藏按钮 */
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface),
                ) {
                    Icon(
                        imageVector = Icons.Default.StarBorder,
                        contentDescription = "Favorite",
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            }

            /* 描述 */
            Crossfade(targetState = editing) {
                when (it) {
                    true -> {
                        /* 描述编辑框 */
                        OutlinedTextField(
                            value = description.value,
                            onValueChange = { value ->
                                description.value = value
                            },
                            modifier = Modifier.padding(bottom = 8.dp),
                            label = {
                                Text(
                                    text = stringResource(id = R.string.description),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.outline,
                                )
                            },
                            textStyle = MaterialTheme.typography.labelMedium,
                            shape = roundedCornerShape,
                        )
                    }

                    false -> {
                        /* 描述 */
                        Text(
                            text = description.value,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                        )
                    }
                }
            }


            /* 主体内容 */
            Crossfade(targetState = editing) {
                when (it) {
                    true -> {
                        /* markdown 实时渲染编辑器 */
                        MarkdownEditor(
                            value = markdown.value,
                            onValueChange = { value ->
                                markdown.value = value.copy(text = value.text)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.outline,
                                    shape = roundedCornerShape,
                                )
                                .background(
                                    color = Color.White,
                                    shape = roundedCornerShape,
                                )
                                .padding(horizontal = 8.dp),
                        )
                    }

                    false -> {
                        /* markdown 渲染结果 */
                        MarkdownText(
                            markdown = markdown.value.text,
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        )
                    }
                }
            }

            /* 下方按钮 */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.inverseOnSurface
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.reply_all),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

