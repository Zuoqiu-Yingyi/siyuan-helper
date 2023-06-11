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

import java.time.ZonedDateTime

/**
 * An object which represents an account which can belong to a user. A single user can have
 * multiple accounts.
 */
data class Subject(
    val id: Long, // 主题 ID
    val title: String, // 主题标题
    var description: String, // 主题描述
    var updated: ZonedDateTime = ZonedDateTime.now(), // 更新时间
) {
}
