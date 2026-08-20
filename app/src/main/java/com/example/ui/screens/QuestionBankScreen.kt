package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.Question
import com.example.data.models.QuestionsData
import com.example.ui.components.ChemCard
import com.example.ui.components.TopicChip
import com.example.ui.theme.ChemCyan
import com.example.ui.theme.ChemGreen
import com.example.ui.theme.ChemOrange
import com.example.ui.theme.ChemYellow
import com.example.ui.viewmodels.ChemViewModel
import com.example.ui.viewmodels.ScreenState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun QuestionBankScreen(
    viewModel: ChemViewModel,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTopicFilter by remember { mutableStateOf("All") }
    var showBookmarksOnly by remember { mutableStateOf(false) }

    val bookmarks by viewModel.bookmarks.collectAsState()
    val bookmarkedIds = remember(bookmarks) { bookmarks.map { it.questionId }.toSet() }

    val allQuestions = QuestionsData.ALL_QUESTIONS

    val filteredQuestions = remember(searchQuery, selectedTopicFilter, showBookmarksOnly, bookmarkedIds) {
        allQuestions.filter { q ->
            val matchesTopic = selectedTopicFilter == "All" || q.topic == selectedTopicFilter
            val matchesSearch = searchQuery.isBlank() || q.q.contains(searchQuery, ignoreCase = true) || q.explanation.contains(searchQuery, ignoreCase = true)
            val matchesBookmark = !showBookmarksOnly || bookmarkedIds.contains(q.id)
            matchesTopic && matchesSearch && matchesBookmark
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 650.dp)
        ) {
            // Header Bar
            Surface(
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { viewModel.navigateTo(ScreenState.HOME) }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "Question Bank",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = ChemYellow.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "${filteredQuestions.size} Questions",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ChemYellow,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Search Bar & Filter Row
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search chemistry concept, formula, keyword...") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = ChemCyan)
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ChemCyan,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("question_bank_search")
                    )
                }

                // Filter Chips
                item {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (showBookmarksOnly) ChemYellow.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                            border = BorderStroke(1.2.dp, if (showBookmarksOnly) ChemYellow else MaterialTheme.colorScheme.outline),
                            modifier = Modifier.clickable { showBookmarksOnly = !showBookmarksOnly }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (showBookmarksOnly) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = null,
                                    tint = ChemYellow,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Bookmarked (${bookmarks.size})",
                                    fontSize = 11.sp,
                                    fontWeight = if (showBookmarksOnly) FontWeight.Bold else FontWeight.Normal,
                                    color = if (showBookmarksOnly) ChemYellow else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        val topics = listOf("All") + QuestionsData.TOPICS
                        topics.forEach { t ->
                            val isSelected = selectedTopicFilter == t && !showBookmarksOnly
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (isSelected) ChemCyan.copy(alpha = 0.18f) else MaterialTheme.colorScheme.surfaceVariant,
                                border = BorderStroke(1.2.dp, if (isSelected) ChemCyan else MaterialTheme.colorScheme.outline),
                                modifier = Modifier.clickable {
                                    showBookmarksOnly = false
                                    selectedTopicFilter = t
                                }
                            ) {
                                Text(
                                    text = t,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) ChemCyan else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }

                // Question Cards
                items(filteredQuestions) { q ->
                    val isBookmarked = bookmarkedIds.contains(q.id)
                    QuestionBankItemCard(
                        question = q,
                        isBookmarked = isBookmarked,
                        onToggleBookmark = {
                            if (isBookmarked) viewModel.removeBookmark(q.id) else viewModel.toggleBookmark(q)
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
fun QuestionBankItemCard(
    question: Question,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    ChemCard {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = ChemCyan.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = question.topic,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = ChemCyan,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    if (question.isPastExam) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = ChemYellow.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "QCAA ${question.examYear}",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = ChemYellow,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onToggleBookmark,
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = ChemYellow,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = question.q,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 20.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded }
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isExpanded) "Hide Solution" else "Reveal Answer & Explanation",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = ChemGreen
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = ChemGreen,
                    modifier = Modifier.size(18.dp)
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    val correctAns = if (question.type == "mcq" && question.options.isNotEmpty()) {
                        "(${listOf("A","B","C","D").getOrElse(question.answerIndex) { "" }}) ${question.options.getOrElse(question.answerIndex) { "" }}"
                    } else question.answerShort

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = ChemGreen.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, ChemGreen.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "CORRECT ANSWER: $correctAns",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = ChemGreen
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = question.explanation,
                                fontSize = 11.sp,
                                lineHeight = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}
