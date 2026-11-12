package com.kyrx.mypresence.feature.presence

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideogameAsset
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyrx.mypresence.ui.components.GlassCard
import com.kyrx.mypresence.ui.components.PremiumTextField
import com.kyrx.mypresence.ui.theme.Accent
import com.kyrx.mypresence.ui.theme.Background
import com.kyrx.mypresence.ui.theme.Dimens
import com.kyrx.mypresence.ui.theme.Error
import com.kyrx.mypresence.ui.theme.Success
import com.kyrx.mypresence.ui.theme.Surface
import com.kyrx.mypresence.ui.theme.SurfaceBorder
import com.kyrx.mypresence.ui.theme.TextDisabled
import com.kyrx.mypresence.ui.theme.TextPrimary
import com.kyrx.mypresence.ui.theme.TextSecondary
import com.kyrx.mypresence.ui.theme.TextTertiary
import com.kyrx.mypresence.ui.theme.Warning

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PresenceEditorScreen(
    viewModel: PresenceEditorViewModel,
    onNavigateBack: () -> Unit
) {
    val activityType by viewModel.activityType.collectAsState()
    val name by viewModel.name.collectAsState()
    val details by viewModel.details.collectAsState()
    val state by viewModel.state.collectAsState()
    val largeImage by viewModel.largeImage.collectAsState()
    val smallImage by viewModel.smallImage.collectAsState()
    val largeText by viewModel.largeText.collectAsState()
    val smallText by viewModel.smallText.collectAsState()
    val startTimestamp by viewModel.startTimestamp.collectAsState()
    val endTimestamp by viewModel.endTimestamp.collectAsState()
    val partySize by viewModel.partySize.collectAsState()
    val partyMax by viewModel.partyMax.collectAsState()
    val button1Label by viewModel.button1Label.collectAsState()
    val button1Url by viewModel.button1Url.collectAsState()
    val button2Label by viewModel.button2Label.collectAsState()
    val button2Url by viewModel.button2Url.collectAsState()
    val platform by viewModel.platform.collectAsState()
    val streamUrl by viewModel.streamUrl.collectAsState()
    val joinSecret by viewModel.joinSecret.collectAsState()
    val spectateSecret by viewModel.spectateSecret.collectAsState()
    val matchSecret by viewModel.matchSecret.collectAsState()
    val status by viewModel.status.collectAsState()
    val livePreview by viewModel.livePreview.collectAsState()

    var showSaveDialog by remember { mutableStateOf(false) }
    var presetNameInput by remember { mutableStateOf("") }

    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text("Save as Preset", color = TextPrimary) },
            text = {
                PremiumTextField(
                    value = presetNameInput,
                    onValueChange = { presetNameInput = it },
                    label = "Preset Name",
                    placeholder = name.ifBlank { "My Preset" }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val finalName = presetNameInput.ifBlank { name.ifBlank { "Custom Preset" } }
                        viewModel.saveAsNewPreset(finalName)
                        showSaveDialog = false
                        presetNameInput = ""
                    }
                ) {
                    Text("Save", color = Accent, fontWeight = FontWeight.W600)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) {
                    Text("Cancel", color = TextTertiary)
                }
            },
            containerColor = Surface
        )
    }

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Presence Editor",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                },
                actions = {
                    TextButton(onClick = { showSaveDialog = true }) {
                        Text("Save Preset", color = Accent, fontWeight = FontWeight.W600)
                    }
                    TextButton(onClick = onNavigateBack) {
                        Text("Done", color = TextSecondary, fontWeight = FontWeight.W600)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        },
        bottomBar = {
            Surface(color = Background) {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.lg, vertical = Dimens.sm),
                    contentPadding = PaddingValues(Dimens.md)
                ) {
                    PresencePreview(
                        name = name,
                        details = details,
                        state = state,
                        activityType = activityType,
                        status = status,
                        largeImage = largeImage,
                        smallImage = smallImage,
                        button1Label = button1Label,
                        button2Label = button2Label,
                        startTimestamp = startTimestamp,
                        endTimestamp = endTimestamp
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Dimens.lg)
        ) {
            Spacer(Modifier.height(Dimens.sm))

            SectionHeader("Activity Type")
            Spacer(Modifier.height(Dimens.sm))
            ActivityTypePicker(
                selected = activityType,
                onSelect = { 
                    viewModel.activityType.value = it
                    viewModel.onFieldChanged()
                }
            )

            Spacer(Modifier.height(Dimens.sectionSpacing))

            SectionHeader("Details")
            Spacer(Modifier.height(Dimens.sm))
            PremiumTextField(
                value = name,
                onValueChange = { 
                    viewModel.name.value = it
                    viewModel.onFieldChanged()
                },
                label = "Activity Name",
                placeholder = "My Presence"
            )
            Spacer(Modifier.height(Dimens.sm))
            PremiumTextField(
                value = details,
                onValueChange = { 
                    viewModel.details.value = it
                    viewModel.onFieldChanged()
                },
                label = "Details",
                placeholder = "What are you doing?"
            )
            Spacer(Modifier.height(Dimens.sm))
            PremiumTextField(
                value = state,
                onValueChange = { 
                    viewModel.state.value = it
                    viewModel.onFieldChanged()
                },
                label = "State",
                placeholder = "Your current state"
            )
            Spacer(Modifier.height(Dimens.sm))
            PremiumTextField(
                value = largeImage,
                onValueChange = { 
                    viewModel.largeImage.value = it
                    viewModel.onFieldChanged()
                },
                label = "Large Image URL",
                placeholder = "https://example.com/image.png"
            )
            Spacer(Modifier.height(Dimens.sm))
            PremiumTextField(
                value = largeText,
                onValueChange = { 
                    viewModel.largeText.value = it
                    viewModel.onFieldChanged()
                },
                label = "Large Image Text",
                placeholder = "Tooltip for large image"
            )
            Spacer(Modifier.height(Dimens.sm))
            PremiumTextField(
                value = smallImage,
                onValueChange = { 
                    viewModel.smallImage.value = it
                    viewModel.onFieldChanged()
                },
                label = "Small Image URL",
                placeholder = "https://example.com/small.png"
            )
            Spacer(Modifier.height(Dimens.sm))
            PremiumTextField(
                value = smallText,
                onValueChange = { 
                    viewModel.smallText.value = it
                    viewModel.onFieldChanged()
                },
                label = "Small Image Text",
                placeholder = "Tooltip for small image"
            )

            Spacer(Modifier.height(Dimens.sectionSpacing))

            SectionHeader("Timestamps")
            Spacer(Modifier.height(Dimens.sm))
            TimestampEditor(
                label = "Start Time",
                timestamp = startTimestamp,
                isStart = true,
                onSet = { 
                    viewModel.startTimestamp.value = it
                    viewModel.onFieldChanged()
                },
                onClear = { 
                    viewModel.startTimestamp.value = null
                    viewModel.onFieldChanged()
                }
            )
            Spacer(Modifier.height(Dimens.sm))
            TimestampEditor(
                label = "End Time",
                timestamp = endTimestamp,
                isStart = false,
                onSet = { 
                    viewModel.endTimestamp.value = it
                    viewModel.onFieldChanged()
                },
                onClear = { 
                    viewModel.endTimestamp.value = null
                    viewModel.onFieldChanged()
                }
            )

            Spacer(Modifier.height(Dimens.sectionSpacing))

            CollapsibleSection("Party") {
                Spacer(Modifier.height(Dimens.sm))
                PremiumTextField(
                    value = partySize,
                    onValueChange = { 
                        viewModel.partySize.value = it
                        viewModel.onFieldChanged()
                    },
                    label = "Party Size",
                    placeholder = "0",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(Modifier.height(Dimens.sm))
                PremiumTextField(
                    value = partyMax,
                    onValueChange = { 
                        viewModel.partyMax.value = it
                        viewModel.onFieldChanged()
                    },
                    label = "Party Max",
                    placeholder = "0",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(Modifier.height(Dimens.sm))

            CollapsibleSection("Buttons & Links") {
                Spacer(Modifier.height(Dimens.sm))
                PremiumTextField(
                    value = button1Label,
                    onValueChange = {
                        viewModel.button1Label.value = it
                        viewModel.onFieldChanged()
                    },
                    label = "Button 1 Label",
                    placeholder = "Open profile"
                )
                Spacer(Modifier.height(Dimens.sm))
                PremiumTextField(
                    value = button1Url,
                    onValueChange = {
                        viewModel.button1Url.value = it
                        viewModel.onFieldChanged()
                    },
                    label = "Button 1 URL",
                    placeholder = "https://example.com"
                )
                Spacer(Modifier.height(Dimens.sm))
                PremiumTextField(
                    value = button2Label,
                    onValueChange = {
                        viewModel.button2Label.value = it
                        viewModel.onFieldChanged()
                    },
                    label = "Button 2 Label",
                    placeholder = "Join"
                )
                Spacer(Modifier.height(Dimens.sm))
                PremiumTextField(
                    value = button2Url,
                    onValueChange = {
                        viewModel.button2Url.value = it
                        viewModel.onFieldChanged()
                    },
                    label = "Button 2 URL",
                    placeholder = "https://example.com"
                )
                Spacer(Modifier.height(Dimens.sm))
                PremiumTextField(
                    value = platform,
                    onValueChange = {
                        viewModel.platform.value = it
                        viewModel.onFieldChanged()
                    },
                    label = "Platform",
                    placeholder = "android"
                )
                Spacer(Modifier.height(Dimens.sm))
                PremiumTextField(
                    value = streamUrl,
                    onValueChange = {
                        viewModel.streamUrl.value = it
                        viewModel.onFieldChanged()
                    },
                    label = "Streaming URL",
                    placeholder = "https://twitch.tv/example"
                )
            }

            Spacer(Modifier.height(Dimens.sm))

            CollapsibleSection("Secrets") {
                Spacer(Modifier.height(Dimens.sm))
                PremiumTextField(
                    value = joinSecret,
                    onValueChange = { 
                        viewModel.joinSecret.value = it
                        viewModel.onFieldChanged()
                    },
                    label = "Join Secret",
                    placeholder = "Secret key for joining"
                )
                Spacer(Modifier.height(Dimens.sm))
                PremiumTextField(
                    value = spectateSecret,
                    onValueChange = { 
                        viewModel.spectateSecret.value = it
                        viewModel.onFieldChanged()
                    },
                    label = "Spectate Secret",
                    placeholder = "Secret key for spectating"
                )
                Spacer(Modifier.height(Dimens.sm))
                PremiumTextField(
                    value = matchSecret,
                    onValueChange = { 
                        viewModel.matchSecret.value = it
                        viewModel.onFieldChanged()
                    },
                    label = "Match Secret",
                    placeholder = "Secret key for match"
                )
            }

            Spacer(Modifier.height(Dimens.sectionSpacing))

            SectionHeader("Status")
            Spacer(Modifier.height(Dimens.sm))
            StatusPicker(
                selected = status,
                onSelect = { 
                    viewModel.status.value = it
                    viewModel.onFieldChanged()
                }
            )

            Spacer(Modifier.height(Dimens.xxxl))
        }
    }
}

private data class ActivityTypeOption(
    val value: Int,
    val label: String,
    val icon: ImageVector
)

private val activityTypeOptions = listOf(
    ActivityTypeOption(0, "Playing", Icons.Filled.VideogameAsset),
    ActivityTypeOption(1, "Streaming", Icons.Filled.Videocam),
    ActivityTypeOption(2, "Listening", Icons.Filled.Headphones),
    ActivityTypeOption(3, "Watching", Icons.Filled.Visibility),
    ActivityTypeOption(5, "Competing", Icons.Filled.EmojiEvents)
)

private data class StatusOption(
    val value: String,
    val label: String,
    val color: Color
)

private val statusOptions = listOf(
    StatusOption("online", "Online", Success),
    StatusOption("idle", "Idle", Warning),
    StatusOption("dnd", "DND", Error),
    StatusOption("invisible", "Invisible", TextTertiary)
)

private fun activityTypeLabel(type: Int): String =
    activityTypeOptions.find { it.value == type }?.label ?: "Playing"

private fun activityTypeIcon(type: Int): ImageVector =
    activityTypeOptions.find { it.value == type }?.icon ?: Icons.Filled.VideogameAsset

private fun statusColor(value: String): Color =
    statusOptions.find { it.value == value }?.color ?: Success

private fun formatRelativeTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    val isPast = diff >= 0
    val absDiff = if (isPast) diff else -diff
    val totalSeconds = absDiff / 1000
    val seconds = totalSeconds % 60
    val totalMinutes = totalSeconds / 60
    val minutes = totalMinutes % 60
    val hours = totalMinutes / 60

    val timeStr = when {
        hours >= 24 -> "${hours / 24}d ${hours % 24}h ${minutes}m"
        hours > 0 -> "${hours}h ${minutes}m"
        minutes > 0 -> "${minutes}m ${seconds}s"
        else -> "${seconds}s"
    }

    return if (isPast) "${timeStr} ago" else "in ${timeStr}"
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = Accent,
        fontWeight = FontWeight.W600
    )
}

@Composable
private fun ActivityTypePicker(
    selected: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(Dimens.sm)
    ) {
        activityTypeOptions.forEach { option ->
            val isSelected = option.value == selected
            Surface(
                modifier = Modifier.clickable { onSelect(option.value) },
                shape = RoundedCornerShape(Dimens.buttonCorner),
                color = if (isSelected) Accent.copy(alpha = 0.2f) else Surface,
                border = if (isSelected) null
                         else androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = Dimens.md, vertical = Dimens.sm),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = option.icon,
                        contentDescription = null,
                        tint = if (isSelected) Accent else TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(Dimens.sm))
                    Text(
                        text = option.label,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (isSelected) Accent else TextSecondary,
                        fontWeight = if (isSelected) FontWeight.W600 else FontWeight.W400
                    )
                }
            }
        }
    }
}

@Composable
private fun TimestampEditor(
    label: String,
    timestamp: Long?,
    isStart: Boolean,
    onSet: (Long) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showFields by remember { mutableStateOf(false) }
    var hours by remember { mutableStateOf("") }
    var minutes by remember { mutableStateOf("") }
    var seconds by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = label,
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.W500
            )
            Spacer(Modifier.width(Dimens.sm))
            if (timestamp != null) {
                Text(
                    text = formatRelativeTime(timestamp),
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(Modifier.height(Dimens.sm))

        if (timestamp != null) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (isStart) "Started" else "Ends",
                    color = TextTertiary,
                    fontSize = 12.sp
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = formatRelativeTime(timestamp),
                    color = TextSecondary,
                    fontSize = 12.sp
                )
                Spacer(Modifier.weight(1f))
                TextButton(onClick = onClear) {
                    Text("Clear", color = Error, fontSize = 13.sp)
                }
            }
        } else if (!showFields) {
            TextButton(onClick = { showFields = true }) {
                Text(
                    text = "Set $label",
                    color = Accent,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500
                )
            }
        }

        AnimatedVisibility(
            visible = showFields && timestamp == null,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Dimens.sm)
                ) {
                    PremiumTextField(
                        value = hours,
                        onValueChange = { hours = it.filter { c -> c.isDigit() }.take(2) },
                        placeholder = "HH",
                        modifier = Modifier.weight(1f),
                        label = "Hours",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    PremiumTextField(
                        value = minutes,
                        onValueChange = { minutes = it.filter { c -> c.isDigit() }.take(2) },
                        placeholder = "MM",
                        modifier = Modifier.weight(1f),
                        label = "Minutes",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    PremiumTextField(
                        value = seconds,
                        onValueChange = { seconds = it.filter { c -> c.isDigit() }.take(2) },
                        placeholder = "SS",
                        modifier = Modifier.weight(1f),
                        label = "Seconds",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
                Spacer(Modifier.height(Dimens.sm))
                Row(horizontalArrangement = Arrangement.spacedBy(Dimens.sm)) {
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                val h = hours.toLongOrNull() ?: 0
                                val m = minutes.toLongOrNull() ?: 0
                                val s = seconds.toLongOrNull() ?: 0
                                val offset = h * 3600000L + m * 60000L + s * 1000L
                                onSet(
                                    if (isStart) System.currentTimeMillis() - offset
                                    else System.currentTimeMillis() + offset
                                )
                                showFields = false
                            },
                        shape = RoundedCornerShape(Dimens.buttonCorner),
                        color = Accent.copy(alpha = 0.2f)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Apply",
                                color = Accent,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W600
                            )
                        }
                    }
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                onSet(System.currentTimeMillis())
                                showFields = false
                            },
                        shape = RoundedCornerShape(Dimens.buttonCorner),
                        color = Surface
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Now",
                                color = TextSecondary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W500
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CollapsibleSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(vertical = Dimens.sm),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = Accent,
                fontWeight = FontWeight.W600
            )
            Spacer(Modifier.weight(1f))
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = if (expanded) "Collapse" else "Expand",
                tint = TextTertiary,
                modifier = Modifier.size(Dimens.iconMedium)
            )
        }
        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(content = content)
        }
    }
}

@Composable
private fun StatusPicker(
    selected: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimens.sm)
    ) {
        statusOptions.forEach { option ->
            val isSelected = option.value == selected
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onSelect(option.value) },
                shape = RoundedCornerShape(Dimens.buttonCorner),
                color = if (isSelected) option.color.copy(alpha = 0.2f) else Surface,
                border = if (isSelected) null
                         else androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Dimens.sm),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(option.color)
                    )
                    Spacer(Modifier.width(Dimens.sm))
                    Text(
                        text = option.label,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (isSelected) option.color else TextSecondary,
                        fontWeight = if (isSelected) FontWeight.W600 else FontWeight.W400
                    )
                }
            }
        }
    }
}

@Composable
private fun PresencePreview(
    name: String,
    details: String,
    state: String,
    activityType: Int,
    status: String,
    largeImage: String,
    smallImage: String,
    button1Label: String,
    button2Label: String,
    startTimestamp: Long?,
    endTimestamp: Long?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(Dimens.sm))
                    .background(Surface)
            ) {
                if (largeImage.isNotBlank() && (largeImage.startsWith("http://", ignoreCase = true) || largeImage.startsWith("https://", ignoreCase = true))) {
                    AsyncImage(
                        model = largeImage,
                        contentDescription = "Large Artwork",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize().background(Accent.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = activityTypeIcon(activityType),
                            contentDescription = null,
                            tint = Accent,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                if (smallImage.isNotBlank() && (smallImage.startsWith("http://", ignoreCase = true) || smallImage.startsWith("https://", ignoreCase = true))) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 2.dp, y = 2.dp)
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(Background)
                            .padding(2.dp)
                    ) {
                        AsyncImage(
                            model = smallImage,
                            contentDescription = "Small Artwork",
                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            Spacer(Modifier.width(Dimens.md))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(statusColor(status))
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = activityTypeLabel(activityType).uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = statusColor(status),
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = name.ifBlank { "My Presence" },
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 1
                )
                if (details.isNotBlank()) {
                    Text(
                        text = details,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        maxLines = 1
                    )
                }
                if (state.isNotBlank()) {
                    Text(
                        text = state,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextTertiary,
                        maxLines = 1
                    )
                }
                if (startTimestamp != null || endTimestamp != null) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = if (startTimestamp != null) formatRelativeTime(startTimestamp) else formatRelativeTime(endTimestamp ?: 0L),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextTertiary
                    )
                }
            }
        }

        if (button1Label.isNotBlank() || button2Label.isNotBlank()) {
            Spacer(Modifier.height(Dimens.sm))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Dimens.sm)
            ) {
                if (button1Label.isNotBlank()) {
                    Surface(
                        modifier = Modifier.weight(1f).height(30.dp),
                        shape = RoundedCornerShape(6.dp),
                        color = SurfaceBorder
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = button1Label,
                                style = MaterialTheme.typography.labelSmall,
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
                if (button2Label.isNotBlank()) {
                    Surface(
                        modifier = Modifier.weight(1f).height(30.dp),
                        shape = RoundedCornerShape(6.dp),
                        color = SurfaceBorder
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = button2Label,
                                style = MaterialTheme.typography.labelSmall,
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}
