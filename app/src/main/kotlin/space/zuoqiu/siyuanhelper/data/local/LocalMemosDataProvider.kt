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

package space.zuoqiu.siyuanhelper.data.local

import space.zuoqiu.siyuanhelper.R
import space.zuoqiu.siyuanhelper.data.Memo
import space.zuoqiu.siyuanhelper.data.MemoAttachment
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * A static data store of [Memo]s.
 */

object LocalMemosDataProvider {

    val allMemos = listOf(
        Memo(
            id = 0L,
            subject = LocalSubjectDataProvider.getDefaultSubject(),
            title = "memo title 1",
            description = "memo description 1",
            content = """
                Cucumber Mask Facial has shipped.

                Keep an eye out for a package to arrive between this Thursday and next Tuesday. If for any reason you don't receive your package before the end of next week, please reach out to us for details on your shipment.

                As always, thank you for shopping with us and we hope you love our specially formulated Cucumber Mask!
            """.trimIndent(),
            isStarred = true,
            updated = LocalDateTime.of(2023, 1, 2, 3, 4, 5).atZone(ZoneId.systemDefault()),
        ),
        Memo(
            id = 1L,
            subject = LocalSubjectDataProvider.getDefaultSubject(),
            description = "Brunch this weekend?",
            content = """
                I'll be in your neighborhood doing errands and was hoping to catch you for a coffee this Saturday. If you don't have anything scheduled, it would be great to see you! It feels like its been forever.

                If we do get a chance to get together, remind me to tell you about Kim. She stopped over at the house to say hey to the kids and told me all about her trip to Mexico.

                Talk to you soon,

                Ali
            """.trimIndent(),
            updated = LocalDateTime.of(2023, 2, 2, 3, 4, 5).atZone(ZoneId.systemDefault()),
        ),
        Memo(
            id = 2L,
            subject = LocalSubjectDataProvider.getDefaultSubject(),
            description = "Bonjour from Paris",
            content = "Here are some great shots from my trip...",
            attachments = listOf(
                MemoAttachment(R.drawable.paris_1, "Bridge in Paris"),
                MemoAttachment(R.drawable.paris_2, "Bridge in Paris at night"),
                MemoAttachment(R.drawable.paris_3, "City street in Paris"),
                MemoAttachment(R.drawable.paris_4, "Street with bike in Paris")
            ),
            isImportant = true,
            updated = LocalDateTime.of(2023, 3, 2, 3, 4, 5).atZone(ZoneId.systemDefault()),
        ),
    )

    /**
     * Get an [Memo] with the given [id].
     */
    fun getMemoById(id: Long): Memo? {
        return allMemos.firstOrNull { it.id == id }
    }

    /**
     * Create a new [Memo] that is a reply to the email with the given [subjectId].
     */
    fun createMemoTo(subjectId: Long): Memo {
        val subject = LocalSubjectDataProvider.getSubjectById(subjectId) ?: LocalSubjectDataProvider.getDefaultSubject()
        return Memo(
            id = System.nanoTime(),
            subject = subject,
            description = "Monthly hosting party",
            isStarred = true,
            isImportant = true,
            content = "Responding to the above conversation."
        )
    }
}
