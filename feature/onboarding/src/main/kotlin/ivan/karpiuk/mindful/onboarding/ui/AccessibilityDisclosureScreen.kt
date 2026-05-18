package ivan.karpiuk.mindful.onboarding.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Mandatory Google Play policy compliance screen for the Accessibility permission declaration.
 *
 * IMPORTANT — Play Store compliance rules:
 * - Do NOT modify the disclosure text without updating the Play Console Permission Declaration Form.
 * - The video submission for the Permission Declaration Form must show this exact screen.
 * - The primary button label "I understand and agree" is required by Play policy for explicit
 *   affirmative consent — do not rename it.
 */
@Composable
fun AccessibilityDisclosureScreen(
    modifier: Modifier = Modifier,
    onAgree: () -> Unit,
    onDecline: () -> Unit,
) {
    BackHandler(enabled = true, onBack = onDecline)

    Scaffold(
        modifier = modifier,
        bottomBar = {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
            ) {
                Button(
                    onClick = onAgree,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("I understand and agree")
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onDecline,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "Don't allow",
                        color = MaterialTheme.colorScheme.outline,
                    )
                }
            }
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 24.dp),
        ) {
            Text(
                text = "Accessibility permission",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(24.dp))

            DisclosureSection(
                title = "What Mindful detects",
                body =
                    "Mindful uses the Accessibility Service to detect which app is currently " +
                        "displayed on your screen. This happens in real time so the app can show a " +
                        "blocking screen the moment you open an app that has reached its daily limit.",
            )
            Spacer(modifier = Modifier.height(20.dp))

            DisclosureSection(
                title = "How this data is used",
                body =
                    "The foreground app name is checked against your configured limits. " +
                        "It is used only to decide whether to show the blocking overlay. It is " +
                        "never stored beyond the current session, never uploaded to any server, and " +
                        "never shared with any third party.",
            )
            Spacer(modifier = Modifier.height(20.dp))

            DisclosureSection(
                title = "What Mindful does NOT do",
                body =
                    "Mindful does not read any text displayed on your screen. It does not " +
                        "access your passwords, messages, emails, or any other content. It does not " +
                        "monitor your keystrokes. It does not record or screenshot your screen.",
            )
            Spacer(modifier = Modifier.height(20.dp))

            DisclosureSection(
                title = "You are in control",
                body =
                    "You can disable this permission at any time in Settings → " +
                        "Accessibility → Mindful. Disabling it will stop the app from blocking any " +
                        "apps until re-enabled.",
            )
        }
    }
}

@Composable
private fun DisclosureSection(
    title: String,
    body: String,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
    )
    Spacer(modifier = Modifier.height(6.dp))
    Text(
        text = body,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Preview(showBackground = true)
@Composable
private fun AccessibilityDisclosureScreenPreview() {
    AccessibilityDisclosureScreen(
        onAgree = {},
        onDecline = {},
    )
}
