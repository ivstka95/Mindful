#!/bin/bash
# Runs after Claude edits any file. If it was a Kotlin file, format it.
TOOL_INPUT=$(cat)
FILE=$(echo "$TOOL_INPUT" | jq -r '.tool_input.file_path // ""')

if [[ "$FILE" == *.kt || "$FILE" == *.kts ]]; then
    # ktlint formatter via Gradle. --quiet to keep Claude's output clean.
    ./gradlew ktlintFormat --quiet 2>/dev/null || true
fi
exit 0
