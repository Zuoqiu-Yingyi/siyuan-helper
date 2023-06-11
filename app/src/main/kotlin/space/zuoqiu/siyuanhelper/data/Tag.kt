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
data class Tag(
    val id: Long, // 标签 ID
    val name: String = "", // 标签名称
    val description: String = "", // 标签描述
)