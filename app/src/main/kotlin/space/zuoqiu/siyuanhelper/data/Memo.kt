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

package space.zuoqiu.siyuanhelper.data

import space.zuoqiu.siyuanhelper.data.local.LocalSubjectDataProvider
import java.time.ZonedDateTime

/**
 * A simple data class to represent an Email.
 */
data class Memo(
    val id: Long, // 备忘录 ID
    var subject: Subject = LocalSubjectDataProvider.getDefaultSubject(), // 所属主题
    var title: String = "", // 标题
    var description: String = "", // 描述
    var content: String = "", // 内容
    var attachments: List<MemoAttachment> = emptyList(), // 附件
    var isImportant: Boolean = false, // 是否重要
    var isStarred: Boolean = false, // 是否星标
    var memoType: MemoType = MemoType.INBOX, // 备忘录类型
    var updated: ZonedDateTime = ZonedDateTime.now(), // 更新时间
    var referenced: List<Memo> = emptyList(), // 被引用列表
    var tags: List<Tag> = emptyList(), // 标签列表
)
